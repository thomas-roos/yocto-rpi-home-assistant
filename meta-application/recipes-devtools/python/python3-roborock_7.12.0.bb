SUMMARY = "Python library for Roborock vacuums"
HOMEPAGE = "https://pypi.org/project/python-roborock/"
# Relicensed from GPL-3.0 to Apache-2.0 upstream.
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi python_pep517
inherit python_hatchling


PYPI_PACKAGE = "python_roborock"

SRC_URI[sha256sum] = "26f441f9e15c46b458e18b51cd274f29ca843048dddf33351c1d692f113f346b"

RDEPENDS:${PN} += "\
    python3-aiohttp (>=3.8.2) \
    python3-aiomqtt (>=2.5.0) \
    python3-construct (>=2.10.57) \
    python3-paho-mqtt (>=1.6.1) \
    python3-protobuf (>=6.31.1) \
    python3-pycryptodome (>=3.18) \
    python3-pyrate-limiter (>=4.0.0) \
    python3-vacuum-map-parser-roborock \
"

