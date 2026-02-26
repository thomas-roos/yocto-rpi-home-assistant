SUMMARY = "This is the KNX panel for the KNX core integration in Home Assistant. It provides a user interface for interacting with the KNX integration."
HOMEPAGE = "https://github.com/XKNX/knx-frontend"
SECTION = "homeassistant/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=38f6332a1e1c2c3eca4cedff90e12291"

SRC_URI[sha256sum] = "2523cb8f415a950b755bd540a81f7bd22f1e12be9985cd80b42b8eafa7ce7c8b"

inherit pypi python_pep517
inherit python_setuptools_build_meta

PYPI_PACKAGE = "knx_frontend"

BBCLASSEXTEND = "native"


