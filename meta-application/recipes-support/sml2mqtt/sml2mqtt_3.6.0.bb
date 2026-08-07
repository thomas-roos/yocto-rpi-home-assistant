SUMMARY = "SML (Smart Message Language) energy meter to MQTT bridge"
HOMEPAGE = "https://github.com/spacemanspiff2007/sml2mqtt"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI[sha256sum] = "dcf3781b1bdf0dc81529ca124d449ff98960dccddab203651094b6c23f8ef955"
SRC_URI += "\
    file://config.yml \
    file://sml2mqtt.service \
"

inherit pypi setuptools3 systemd useradd

PYPI_PACKAGE = "sml2mqtt"

RDEPENDS:${PN} += "\
    python3-aiomqtt \
    python3-pyserial-asyncio \
    python3-easyconfig \
    python3-pydantic \
    python3-smllib \
    python3-aiohttp \
"

do_install:append() {
    install -d ${D}${sysconfdir}/sml2mqtt
    install -m 0644 ${UNPACKDIR}/config.yml ${D}${sysconfdir}/sml2mqtt/config.yml

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/sml2mqtt.service ${D}${systemd_system_unitdir}/sml2mqtt.service
}

FILES:${PN} += "${systemd_system_unitdir}/sml2mqtt.service"
CONFFILES:${PN} += "${sysconfdir}/sml2mqtt/config.yml"

SYSTEMD_SERVICE:${PN} = "sml2mqtt.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --no-create-home --shell /bin/false -N -g sml2mqtt -G dialout sml2mqtt"
GROUPADD_PARAM:${PN} = "--system sml2mqtt"
