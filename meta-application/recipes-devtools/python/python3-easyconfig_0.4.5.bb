SUMMARY = "Pydantic based configuration objects with yaml load/dump support"
HOMEPAGE = "https://github.com/spacemanspiff2007/EasyConfig"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI[sha256sum] = "0f1445ae86fde64a4775938ff620a0f05a9e3c3ecf090bbb1b7b33112067032e"

inherit pypi setuptools3

PYPI_PACKAGE = "easyconfig"

RDEPENDS:${PN} += "\
    python3-pydantic \
    python3-pydantic-settings \
    python3-ruamel-yaml \
    python3-typing-extensions \
"

BBCLASSEXTEND = "native"
