SUMMARY = "SAX battery custom Home Assistant integration (Modbus TCP)"
HOMEPAGE = "https://github.com/matfroh/sax_battery_ha"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

# Upstream main. Our former fork branch (thomas-roos/sax_battery_ha
# sunspec-support: skip dead Modbus unit ids, SunSpec slave 100 sensors incl.
# the full Model 203 ADW200 grid meter block, scale-factor bounds check) was
# merged upstream as #130. Main also carries two fixes the fork lacks:
#  - #143: pymodbus >= 3.12 reports "Request cancelled outside library."; the
#    old check only matched "... outside pymodbus", so every successful SAX
#    write was logged and handled as a failure (we run pymodbus 3.14)
#  - #136: a missing power-factor sensor or unknown SoC no longer stops pilot
#    steering
SRC_URI = "git://github.com/matfroh/sax_battery_ha.git;protocol=https;branch=main"
SRCREV = "943281fad91a6743f89c78a21bb3c04bbfc078a7"

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
