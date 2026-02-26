SUMMARY = "Python library for SMLight devices"
HOMEPAGE = "https://pypi.org/project/pysmlight/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

inherit pypi
inherit python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PYPI_PACKAGE = "pysmlight"

SRC_URI[sha256sum] = "66fa45306e40796620a174651725b34fc39b9b7554739c59af533906b8f9a2fd"

RDEPENDS:${PN} += "\
    python3-aiohttp \
    python3-async-timeout \
    python3-attrs \
"