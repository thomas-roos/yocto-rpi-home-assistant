SUMMARY = "Python library for Miele integration with Home Assistant"
HOMEPAGE = "https://github.com/nordicopen/pymiele"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d49e7bd9d54acb2c4816c204018ce609"

inherit pypi python_pep517
inherit python_setuptools_build_meta

PYPI_PACKAGE = "pymiele"

SRC_URI[sha256sum] = "dc63637d17a75f77cd1a4d4656ff7033366bef3f8e8441989af25e6d0d16b09e"

RDEPENDS:${PN} += "python3-aiohttp"

BBCLASSEXTEND = "native"
