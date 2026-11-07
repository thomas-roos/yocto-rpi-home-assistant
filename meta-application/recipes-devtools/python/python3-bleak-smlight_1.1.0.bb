SUMMARY = "Bleak backend for SMLIGHT SLZB Bluetooth proxies"
HOMEPAGE = "https://github.com/bluetooth-devices/bleak-smlight"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7a5647e12f0a7d494ea83803aac688ab"

SRC_URI[sha256sum] = "1ac71c49ee45050746a2436b0681fd984d8c31ed18f059d3c6e67d316ac1986a"

inherit pypi python_poetry_core cython

RDEPENDS:${PN} += " \
    python3-bluetooth-data-tools (>=1.18.0) \
    python3-habluetooth (>=6.4.0) \
    python3-pysmlight (>=0.5.0) \
"
