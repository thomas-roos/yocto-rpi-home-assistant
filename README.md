A yocto layer setup to build a Rasperry Pi 64 home assistant setup.


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
```bash
cd bitbake/bin/ && \
./bitbake-setup --setting default top-dir-prefix $PWD/../../ init \
  $PWD/../../bitbake-setup.conf.json \
  homeassistant distro/poky-altcfg application/ha core/yocto/sstate-mirror-cdn --non-interactive && \
  cd -
```

3. Source the build environment:
```bash
. ./bitbake-builds/bitbake-setup-doorphone-distro_poky-altcfg/build/init-build-env
```

4. Build the image:
```bash
bitbake doorphone-image
```

Or rauch bundle:

```bash
bitbake doorphone-bundle
```


5. Resulting image:

```bash
./bitbake-builds/bitbake-setup-doorphone-distro_poky-altcfg/build/tmp/deploy/images/raspberrypi-armv8/doorphone-image-raspberrypi-armv8.rootfs.wic.bz2
```

Or rauc bundle:

```bash
./bitbake-builds/bitbake-setup-doorphone-distro_poky-altcfg/build/tmp/deploy/images/raspberrypi-armv8/doorphone-bundle-raspberrypi-armv8.raucb
```
