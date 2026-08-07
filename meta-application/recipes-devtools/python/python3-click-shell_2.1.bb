SUMMARY = "Combine Click and Cmd into one CLI shell"
HOMEPAGE = "https://github.com/clarkperkins/click-shell"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1a0e5839ab6a6b7ca39eb3ef6aa84f13"

inherit pypi setuptools3

PYPI_PACKAGE = "click-shell"

SRC_URI[sha256sum] = "ce0c91faae284c41a39bec966f928791ad4a45763755445f1fe2041fd091aa37"

RDEPENDS:${PN} += "python3-click"

BBCLASSEXTEND = "native"
