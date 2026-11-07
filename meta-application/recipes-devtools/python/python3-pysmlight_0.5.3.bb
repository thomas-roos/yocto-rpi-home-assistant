SUMMARY = "Python library for SMLight devices"
HOMEPAGE = "https://pypi.org/project/pysmlight/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

inherit pypi
inherit python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PYPI_PACKAGE = "pysmlight"

SRC_URI[sha256sum] = "390dbc8b7566513cbaed602808ba9a2bed04582c3ce0d86dd6ba7c88e2b25364"

RDEPENDS:${PN} += "\
    python3-aiohttp (>=3.9.3) \
    python3-aiohttp-sse-client2 (>=0.3.0) \
    python3-awesomeversion (>=24.6.0) \
    python3-mashumaro (>=3.10) \
"