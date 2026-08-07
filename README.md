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

### Patching Home Assistant core (Tibber OAuth token)

`meta-homeassistant`'s `python3-homeassistant` recipe builds Home Assistant
from a git checkout and already applies `0001`-`0004`, so core integrations can
be patched from this layer by dropping a numbered patch in
`meta-application/recipes-homeassistant/homeassistant/files/` and adding it to
`SRC_URI` in `python3-homeassistant_%.bbappend`. Unlike a `custom_components`
integration there is no fork-and-`SRCREV` path here - the patch is the
mechanism.

`0005-tibber-refresh-oauth-token-before-api-calls.patch` is a **backport with a
known expiry date**: upstream fixed this in
[#164295](https://github.com/home-assistant/core/pull/164295) ("Fix Tibber
update token", merged 2026-03-17), first released in **2026.4.0**, and never
backported it to a 2026.3.x point release (2026.3.1-2026.3.4 all still carry
the bug). Since `meta-homeassistant` pins 2026.3.0, we hit it. **Drop this
patch the moment that recipe moves to 2026.4.0 or newer** - it will fail to
apply, which is the intended signal.

It fixes the Tibber integration reloading its config entry once an hour. Since Tibber moved to
OAuth2 (the `cloud` auth implementation), the access token is valid for 3600 s.
`TibberRuntimeData.async_get_client()` refreshes the session and pushes the
current token into the shared `tibber.Tibber` client - but nothing called it
after setup: `async_setup_entry()` resolved the client once, and both
`TibberDataCoordinator` and `TibberSensorElPrice` then kept using that captured
object with its setup-time token. An hour later the API answered

```
UNAUTHENTICATED / "exp" claim timestamp check failed
```

which pyTibber raises as `FatalHttpExceptionError`; the coordinator's handler
for that reloads the whole config entry, and *that* is what refreshed the
token - so the integration limped along reloading itself hourly. Symptoms were
`sensor.schuhkarton_monthly_*` sitting at `unknown` for the ~20 min between the
reload and the next 20-minute coordinator run, and the price sensor blipping
`unavailable` every hour. The patch re-resolves the client through
`runtime_data` before touching the API in both places, which is what
`TibberDataAPICoordinator` in the same file already did.

Note the price sensor itself was never really broken - it kept serving values -
so the visible damage was limited to the monthly sensors and log noise.

Because `/usr/lib/python3.14/site-packages` lives on the RAUC rootfs slot and
not on `/data`, a core patch like this is *not* subject to the `/data` caveat
above: it arrives with the next rootfs update and needs no manual seeding. It
does mean the reverse, though - hand-copying the patched `.py` files onto a
running device (useful to test a fix without a full image cycle) is undone by
the next `rauc install`.

### Lovelace dashboards and custom cards

Dashboards are storage-mode (edited in the UI, stored as
`/var/lib/homeassistant/.storage/lovelace.*`), so like `automations.yaml` they
are live device state on `/data` rather than something a recipe produces. The
`overview` dashboard (`http://ha:8123/dashboard-overview/0`) is a single
`sections` view: the Reolink camera and the two KNX Haustür openers are its
only interactive controls, everything else is graphs and read-only status.

Custom Lovelace cards, on the other hand, *are* packaged - `apexcharts-card`
(`meta-application/recipes-homeassistant/apexcharts-card/`) fetches the
upstream release bundle pinned by sha256 and installs it into
`${localstatedir}/lib/homeassistant/www/`, which Home Assistant serves as
`/local/`. Upstream ships only a prebuilt minified bundle, so there is nothing
to compile; the recipe just pins and installs it.

A card file on its own does nothing: Home Assistant only loads it if a matching
entry exists in `.storage/lovelace_resources` (Settings > Dashboards >
Resources, requires advanced mode):

```json
{"id": "<hex>", "type": "module", "url": "/local/apexcharts-card.js?v=2.2.3"}
```

That file is runtime state on `/data`, so it is not packaged - and per the
`/data` caveat above, on an already-provisioned device both the `www/` file and
the resource entry have to be created over SSH once. Bump the `?v=` query
string whenever the recipe's `PV` changes, otherwise browsers keep serving the
stale cached bundle.

### Backlog

- **Miele** (Waschmaschine/Trockner/Spülmaschine) - not started. The pip
  requirement (`python3-pymiele`) is already packaged and in the image, but no
  config entry exists yet.
- **Zigbee** - partially done. ZHA is configured against an SLZB-06 (CC2652)
  coordinator and the `python3-zigpy*` stack is in the image, but no devices
  are paired yet, so it currently contributes zero entities.
- **PV production sensor** - the 15 kWp array sits on its own production meter
  that the SAX/ADW200 cannot see (`sensor.sax_pv_leistung` is `unavailable`,
  SunSpec PV power reads a constant 0), so the Energy dashboard has no solar
  source and dashboards have no real PV graph.

Tibber (dynamic import pricing, `sensor.schuhkarton_electricity_price`) and the
`apexcharts-card` packaging are done.

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
. ./bitbake-builds/setupraspberrypi-armv8/build/init-build-env
```

For QEMU x86-64 (debugging):
```bash
. ./bitbake-builds/setupqemux86-64/build/init-build-env
```

4. Build the image:
```bash
bitbake ha-image
```

Or rauch bundle:

```bash
bitbake ha-bundle
```

5. Resulting image (`IMAGE_FSTYPES` is `wic wic.bmap ext4`, so there is no
   compressed `.wic.bz2` - flash the `.wic` with its `.bmap`):

```bash
./bitbake-builds/setupraspberrypi-armv8/build/tmp/deploy/images/raspberrypi-armv8/ha-image-raspberrypi-armv8.rootfs.wic
```

Or rauc bundle:

```bash
./bitbake-builds/setupraspberrypi-armv8/build/tmp/deploy/images/raspberrypi-armv8/ha-bundle-raspberrypi-armv8.raucb
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

Todo: see [Backlog](#backlog) above.

