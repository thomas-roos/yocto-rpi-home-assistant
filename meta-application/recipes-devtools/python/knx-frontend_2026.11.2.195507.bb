SUMMARY = "This is the KNX panel for the KNX core integration in Home Assistant. It provides a user interface for interacting with the KNX integration."
HOMEPAGE = "https://github.com/XKNX/knx-frontend"
SECTION = "homeassistant/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=38f6332a1e1c2c3eca4cedff90e12291"

SRC_URI[sha256sum] = "2eec58ddad8175bfddb9ee52b3b33d3e5df5e1c0cb25f37951a210b141d31623"

inherit pypi python_pep517
inherit python_setuptools_build_meta

PYPI_PACKAGE = "knx_frontend"

BBCLASSEXTEND = "native"


