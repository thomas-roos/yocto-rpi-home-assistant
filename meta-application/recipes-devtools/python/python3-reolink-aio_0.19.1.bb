SUMMARY = "Reolink NVR/cameras API package"
HOMEPAGE = "https://github.com/starkillerOG/reolink_aio"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b87996612e5f88515ada80c5c7a1aecd"

inherit pypi setuptools3

PYPI_PACKAGE = "reolink_aio"

SRC_URI[sha256sum] = "d7337dc9218efd0913321d8b621482e763e029e250c95db71252761f0a2dc363"

RDEPENDS:${PN} += "\
    python3-aiohttp \
    python3-aiortsp \
    python3-orjson \
    python3-pycryptodomex \
    python3-typing-extensions \
"

BBCLASSEXTEND = "native"
