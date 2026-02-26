SUMMARY = "XKNX - An asynchronous KNX library written in Python"
HOMEPAGE = "https://github.com/XKNX/xknx"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=838c366f69b72c5df05c96dff79b35f2"

SRC_URI[sha256sum] = "ff38727a2524a9409420198b79a21fae5a7aa4af2f73e8318548d2a75069de05"

inherit pypi python_pep517
inherit python_setuptools_build_meta

BBCLASSEXTEND = "native"