SUMMARY = "Python library for Roborock vacuums"
HOMEPAGE = "https://pypi.org/project/python-roborock/"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit pypi python_pep517
inherit python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PYPI_PACKAGE = "python_roborock"

SRC_URI[sha256sum] = "173d87fb1261edcfddef03f14313130c5c31f4de1b8340b2d45872ea9b915d06"

RDEPENDS:${PN} += "\
    python3-aiohttp \
    python3-aiomqtt \
    python3-click \
    python3-click-shell \
    python3-construct \
    python3-paho-mqtt \
    python3-pycryptodome \
    python3-pyrate-limiter \
    python3-vacuum-map-parser-roborock \
"

