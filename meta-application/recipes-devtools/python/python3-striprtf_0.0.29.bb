SUMMARY = "A simple library to convert rtf to text."
HOMEPAGE = "https://github.com/danifus/pyzipper"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7f34550ca55dff99b9d50c5b28f4c266"

inherit pypi python_pep517
inherit python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PYPI_PACKAGE = "striprtf"

SRC_URI[sha256sum] = "5a822d075e17417934ed3add6fc79b5fc8fb544fe4370b2f894cdd28f0ddd78e"
