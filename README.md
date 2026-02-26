A yocto layer setup to build a Raspberry Pi 64bit home assistant setup.
Including ASTERISK for SIP and knxd for KNX connectivity.


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
[Feb 26 13:59:06] ERROR[690]: loader.c:292 module_load_error: Error loading module 'pjsip': /usr/lib/asterisk/modules/pjsip.so: cannot open shared object file: No such file or directory

