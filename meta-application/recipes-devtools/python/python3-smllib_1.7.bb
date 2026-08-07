SUMMARY = "Library for the SML (Smart Message Language) protocol"
HOMEPAGE = "https://github.com/spacemanspiff2007/smllib"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI[sha256sum] = "672c1c5bc576f16f2d67e48e496722a7786fee424c37a455e12a26a7ec208153"

inherit pypi setuptools3

PYPI_PACKAGE = "smllib"

BBCLASSEXTEND = "native"
