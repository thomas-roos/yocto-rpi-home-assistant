SUMMARY = "Helios EasyControls (KWL ventilation) custom Home Assistant integration (Modbus TCP)"
HOMEPAGE = "https://github.com/laszlojakab/homeassistant-easycontrols"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bcada5a697372d445b41446e10978700"

SRC_URI = "git://github.com/laszlojakab/homeassistant-easycontrols.git;protocol=https;branch=develop"
SRCREV = "f9c1015734bd8f412c165babd6c33668cd0b856a"

inherit allarch

do_install() {
    install -d ${D}${localstatedir}/lib/homeassistant/custom_components/easycontrols
    cp -r ${S}/custom_components/easycontrols/. ${D}${localstatedir}/lib/homeassistant/custom_components/easycontrols/
}

FILES:${PN} += "${localstatedir}/lib/homeassistant/custom_components/easycontrols"

# manifest.json requirement
RDEPENDS:${PN} += "python3-eazyctrl"
