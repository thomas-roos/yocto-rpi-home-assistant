SUMMARY = "Library for communicating with Luxtronik heat pump controllers (Alpha Innotec, Novelan, Siemens, ...)"
HOMEPAGE = "https://github.com/bouni/python-luxtronik"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENCE.md;md5=91897ed5245fb6efcffdd0a9e21b8c4b"

inherit pypi setuptools3

PYPI_PACKAGE = "luxtronik"

SRC_URI[sha256sum] = "8ba465497ba9f57ef5dcc1ecca293d4bc9777471a7e350ee7721997dc45d9982"

BBCLASSEXTEND = "native"
