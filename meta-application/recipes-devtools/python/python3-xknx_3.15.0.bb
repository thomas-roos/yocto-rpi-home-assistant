SUMMARY = "XKNX - An asynchronous KNX library written in Python"
HOMEPAGE = "https://github.com/XKNX/xknx"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI[sha256sum] = "98cbe4bc86aed092a90b11fa9eecd78e6c375523e60311a6819172990f63c475"

inherit pypi python_pep517
inherit python_setuptools_build_meta

RDEPENDS:${PN} += "python3-cryptography python3-ifaddr"

BBCLASSEXTEND = "native"