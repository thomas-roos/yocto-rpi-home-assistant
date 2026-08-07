A yocto layer setup to build a Raspberry Pi 64bit home assistant setup.
Including ASTERISK for SIP, knxd for KNX connectivity, mosquitto (MQTT),
a sml2mqtt smart-meter bridge, and a SAX battery Modbus integration.
Updates are delivered via RAUC A/B partitions.

## Architecture

### RAUC A/B updates and the `/data` partition

The rootfs lives on two RAUC slots (`/dev/mmcblk0p2` = A, `/dev/mmcblk0p3` = B).
`bitbake ha-bundle` produces a `.raucb` that `rauc install` writes to the
*inactive* slot; `rauc status` shows which slot is booted/activated.

There is also a separate, persistent `LABEL=data` partition (`/data`),
which RAUC **never touches** - only the rootfs slot is replaced on update.
`ha-image.bb`'s `rootfs_user_fstab_common()` bind-mounts pieces of it over
the live rootfs paths, e.g.:

```
/data/etc/asterisk            -> /etc/asterisk
/data/etc/knxd                -> /etc/knxd
/data/etc/sml2mqtt             -> /etc/sml2mqtt
/data/var/lib/homeassistant    -> /var/lib/homeassistant
/data/var/lib/asterisk         -> /var/lib/asterisk
```

**Important caveat learned the hard way:** the `mv` logic in
`rootfs_user_fstab_common()` that seeds `/data/...` from a recipe's default
files only takes effect when building a **fresh** `/data` partition (e.g.
flashing a brand-new SD card from the full `.wic` image). On an
already-provisioned device, `/data` is real persistent state that a RAUC
rootfs update never re-seeds. So the **first** time a new service needs a
`/data/etc/<service>` directory (or a new file under
`/var/lib/homeassistant`), you must create/seed it directly on the live
device over SSH *before* the new rootfs boots and tries to bind-mount it -
otherwise the mount fails or the service starts with an empty config.
Anything already inside `/data` (custom_components, HA's
`configuration.yaml`, etc.) survives updates untouched.

### Custom Home Assistant integrations must be Yocto recipes

Any `custom_components/<name>` used by Home Assistant is packaged as its
own Yocto recipe under `meta-application/recipes-homeassistant/` (see
`sax-battery-ha/`), even though on an already-provisioned device the files
also need to be placed under `/data/var/lib/homeassistant/custom_components/`
directly (see caveat above). The recipe exists so the whole setup is
reproducible from scratch on a new device/SD card - it is not optional
just because the live device already has a working copy.

### Smart meter -> MQTT -> Home Assistant (sml2mqtt)

The house has a D0/optical (IR) smart-meter reader plugged into the `ha`
device as a USB-serial adapter (`/dev/ttyUSB0`). `sml2mqtt`
(`meta-application/recipes-support/sml2mqtt/`) decodes the SML protocol
frames and publishes to the local mosquitto broker:

- Meter: Iskra/LGZ, device id `0a014c475a00046eca01` (only reports
  cumulative energy counters, no instantaneous power/PF register)
- Topics: `smartmeter/0a014c475a00046eca01/energy_import` and
  `.../energy_export` (Wh, republished at least every 60s)
- CRC: `x25`, framing `8N1` - if you ever see 100% CRC failures on a
  freshly-wired reader, try re-seating/re-angling the IR sensor before
  suspecting the software; that fixed it here.

Home Assistant reads those MQTT topics and derives power from them
(`smartmeter_mqtt.yaml`, `smartmeter_sensor.yaml`, `smartmeter_template.yaml`
under `/var/lib/homeassistant/`, included from `configuration.yaml`):
`sensor.grid_energy_import` / `_export` (MQTT) -> `sensor.grid_power_import_raw`
/ `_export_raw` (derivative) -> `sensor.grid_power` (import minus export,
positive = drawing from the grid) and a fixed `sensor.grid_power_factor`
(1.0 - this meter has no real PF register). These plain YAML files are
live device config (like `automations.yaml`), not custom_components, so
they are not packaged as a recipe - just backed up along with the rest of
`/data`.

### SAX battery integration (Modbus TCP)

`sax-battery-ha` (`meta-application/recipes-homeassistant/sax-battery-ha/`)
packages `matfroh/sax_battery_ha` for `pilot_from_ha` battery steering
against `sensor.grid_power` / `sensor.grid_power_factor` above.

**Known hardware caveat + local fix:** this SAX Power Neo unit only
responds on Modbus unit id 64 (status/SOC/power - all steering actually
needs). Unit id 40, which the integration also polls for capacity/energy
counters and SAX's own internal smartmeter telemetry, never responds -
this installation has no SAX smartmeter accessory paired. Upstream
(matfroh/sax_battery_ha) has open, unresolved issues about exactly this
(#110, #127, #88): the stock integration retries every register on the
dead unit id on every single poll and the config entry never finishes
loading ("stuck on a spinning wheel" in the UI). We carry a local patch
(`files/0001-skip-unresponsive-modbus-slave.patch`) that does one quick
probe per unit id and permanently stops polling any unit id that fails
it - unit 64 keeps working normally, unit 40's sensors just read
`unavailable`. If SAX's smartmeter accessory is ever installed on this
system, this patch should be revisited/dropped.

### Backlog (not started)

Tibber integration, Miele (Waschmaschine/Trockner/Spülmaschine), Zigbee,
and a native `apexcharts-card`-based dashboard are planned but not yet
implemented.

## Building with bitbake-setup

### Quick Start

1. Clone with submodules:
```bash
git clone --recurse-submodules https://github.com/thomas-roos/yocto-rpi-home-assistant
```

Or if already cloned:
```bash
git submodule update --init --recursive
```

2. Initialize the build environment:

For Raspberry Pi:
```bash
cd bitbake/bin/ && \
./bitbake-setup --setting default top-dir-prefix $PWD/../../ \
  init \
  $PWD/../../bitbake-setup.conf.json \
  homeassistantrpi distro/poky-altcfg --non-interactive && \
  cd -
```

For QEMU x86-64 (debugging):
```bash
cd bitbake/bin/ && \
./bitbake-setup --setting default top-dir-prefix $PWD/../../ \
  init \
  $PWD/../../bitbake-setup.conf.json \
  homeassistantqemu qemu distro/poky-altcfg --non-interactive && \
  cd -
```

3. Source the build environment:

For Raspberry Pi:
```bash
. ./bitbake-builds/setupqemux86-64/build/init-build-env
```

For QEMU x86-64 (debugging):
```bash
. ./bitbake-builds/setupraspberrypi-armv8/build/init-build-env
```

4. Build the image:
```bash
bitbake ha-image
```

Or rauch bundle:

```bash
bitbake ha-bundle
```

5. Resulting image:

```bash
./bitbake-builds/setup/build/tmp/deploy/images/raspberrypi-armv8/ha-image-raspberrypi-armv8.rootfs.wic.bz2
```

Or rauc bundle:

```bash
./bitbake-builds/setup/build/tmp/deploy/images/raspberrypi-armv8/ha-bundle-raspberrypi-armv8.raucb
```

### Tips and Tricks

Testing / Debugging with qemu:

```bash
runqemu snapshot nographic wic ovmf slirp
```

Flashing using [bmaptool](https://github.com/yoctoproject/bmaptool) is strongly recommend as this is faster and more secure

```bash
sudo bmaptool copy tmp/deploy/images/raspberrypi-armv8/ha-image-raspberrypi-armv8.rootfs.wic /dev/sde
```

Disable host checking

```bash
ssh -o StrictHostKeyChecking=no root@ha
```

Watch logs
```bash
journalctl -xfeu homeassistant
```

Todo:
- Tibber integration
- Miele appliances (Waschmaschine, Trockner, Spülmaschine)
- Zigbee
- Native dashboard (apexcharts-card + long-term statistics)

