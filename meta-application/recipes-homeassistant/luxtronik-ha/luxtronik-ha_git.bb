SUMMARY = "Luxtronik heat pump custom Home Assistant integration (Alpha Innotec, Novelan, Siemens, ...)"
HOMEPAGE = "https://github.com/BenPru/luxtronik"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=981dccb80ca2fa60d06c6b6ccc8ef8c3"

SRC_URI = "git://github.com/BenPru/luxtronik.git;protocol=https;branch=main"
SRCREV = "79a2364a95f00d0cd23d7509c197f7079db3ebcb"

inherit allarch

do_install() {
    install -d ${D}${localstatedir}/lib/homeassistant/custom_components/luxtronik2
    cp -r ${S}/custom_components/luxtronik2/. ${D}${localstatedir}/lib/homeassistant/custom_components/luxtronik2/
}

FILES:${PN} += "${localstatedir}/lib/homeassistant/custom_components/luxtronik2"

# manifest.json requirements: luxtronik==0.3.14, getmac~=0.9.5, packaging>=26.2
# (getmac and packaging are already provided by meta-homeassistant / oe-core)
RDEPENDS:${PN} += "python3-luxtronik python3-getmac python3-packaging"

# config_flow integration - no YAML wiring needed here. Add via HA UI:
# Settings > Devices & Services > Add Integration > Luxtronik, host 192.168.0.55.
