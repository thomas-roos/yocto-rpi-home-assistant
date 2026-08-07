SUMMARY = "Python library for Miele integration with Home Assistant"
HOMEPAGE = "https://github.com/nordicopen/pymiele"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d49e7bd9d54acb2c4816c204018ce609"

inherit pypi python_pep517
inherit python_setuptools_build_meta

PYPI_PACKAGE = "pymiele"

SRC_URI[sha256sum] = "22a28986e013f14612b6acbed8d43dbb87b31c13ee9babcd9cde36fa1abb9197"

RDEPENDS:${PN} += "python3-aiohttp"

BBCLASSEXTEND = "native"
