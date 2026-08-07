SUMMARY = "Asyncio-based RTSP library, useful to receive video streams"
HOMEPAGE = "https://github.com/marss/aiortsp"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3000208d539ec061b899bce1d9ce9404"

inherit pypi setuptools3

PYPI_PACKAGE = "aiortsp"

SRC_URI[sha256sum] = "6c2ae08ba78fd9b939a281365fdc323896ae5453c3e2c3c1c3dd43efb120928e"

RDEPENDS:${PN} += "python3-dpkt"

BBCLASSEXTEND = "native"
