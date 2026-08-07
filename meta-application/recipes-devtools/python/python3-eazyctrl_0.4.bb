SUMMARY = "Library for monitoring and controlling Helios EasyControls KWL devices via Modbus/TCP"
HOMEPAGE = "https://github.com/baradi09/eazyctrl"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=c11103cbfde2d928b4ba47256284645c"

inherit pypi setuptools3

PYPI_PACKAGE = "eazyctrl"

SRC_URI[sha256sum] = "3d900919e29bcc2698bdbefa04876572819c25d5068de14c6f5f9a4d08355454"

BBCLASSEXTEND = "native"
