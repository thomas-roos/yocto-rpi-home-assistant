SUMMARY = "Extracts KNX projects and parses the underlying XML."
HOMEPAGE = "https://github.com/XKNX/xknxproject"
SECTION = "devel/python"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI[sha256sum] = "2a4150b6ddb1c6f5ab961b2220ad48f8a416f0b5fbe654e03b1e6cdc7c6bd17a"

inherit pypi python_pep517
inherit python_setuptools_build_meta

BBCLASSEXTEND = "native"
