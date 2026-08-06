SUMMARY = "SAX battery custom Home Assistant integration (Modbus TCP)"
HOMEPAGE = "https://github.com/matfroh/sax_battery_ha"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "git://github.com/matfroh/sax_battery_ha.git;protocol=https;branch=main"
SRCREV = "84cc467768b6f0d1d08091b1536994520dcc7da2"

inherit allarch

do_install() {
    install -d ${D}${localstatedir}/lib/homeassistant/custom_components/sax_battery
    cp -r ${S}/custom_components/sax_battery/. ${D}${localstatedir}/lib/homeassistant/custom_components/sax_battery/
}

FILES:${PN} += "${localstatedir}/lib/homeassistant/custom_components/sax_battery"

# manifest.json requirements: pymodbus>=3.11.1,<4.0.0, voluptuous (voluptuous
# is already a core homeassistant dependency)
RDEPENDS:${PN} += "python3-pymodbus"

# Not an integration entry (config_entries) - no config UI step run here.
# Add the "SAX battery" integration via the HA UI once you have a local,
# real-time smartmeter power sensor for pilot-from-HA mode (see project
# session notes: portal-only utility data is too delayed for that feature).
