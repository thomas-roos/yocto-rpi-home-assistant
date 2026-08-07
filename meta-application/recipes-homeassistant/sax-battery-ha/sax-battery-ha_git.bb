SUMMARY = "SAX battery custom Home Assistant integration (Modbus TCP)"
HOMEPAGE = "https://github.com/thomas-roos/sax_battery_ha"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

# Fork of matfroh/sax_battery_ha (upstream: https://github.com/matfroh/sax_battery_ha).
# branch sunspec-support adds on top of upstream main@84cc467:
#  - skip Modbus unit ids that never respond (e.g. missing/disabled smartmeter
#    accessory) instead of retrying every register every poll forever
#  - read-only sensors for the SunSpec (slave 100) interface available on
#    firmware Master V61 / Gateway V54+: PV power, grid power/frequency,
#    battery capacity, available charge/discharge power, and full Model 203
#    grid smartmeter (ADW200) coverage: per-phase/sum current, voltage
#    (L-N and L-L), apparent power, reactive power, power factor
#  - defensive bounds-check so bad device data can never crash the whole
#    coordinator update (see commit history for the off-by-one bug this
#    guards against, now also fixed at the root)
SRC_URI = "git://github.com/thomas-roos/sax_battery_ha.git;protocol=https;branch=sunspec-support"
SRCREV = "af416b898ae140a0d68d294c768c554001151634"

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
