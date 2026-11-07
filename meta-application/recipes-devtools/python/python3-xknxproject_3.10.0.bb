SUMMARY = "Extracts KNX projects and parses the underlying XML."
HOMEPAGE = "https://github.com/XKNX/xknxproject"
SECTION = "devel/python"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI[sha256sum] = "b8ae4a63538992a072e819c32a9ec75947f34ab57cf0adbb3b0e84155fabaede"

inherit pypi python_pep517
inherit python_setuptools_build_meta

RDEPENDS:${PN} += "python3-pyzipper python3-striprtf"

BBCLASSEXTEND = "native"
