SUMMARY = "Vacuum map parser for Roborock devices"
HOMEPAGE = "https://github.com/PiotrMachowski/Python-package-vacuum-map-parser-roborock"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=103aa1e1748c15e5e6595aa349e4959f"

inherit pypi python_poetry_core

PYPI_PACKAGE = "vacuum_map_parser_roborock"

SRC_URI[sha256sum] = "07ab7cd8aaf0e94da62d2a228013b2f6b8acb0e6d2215b697b6441ffdfd70e89"

RDEPENDS:${PN} += "python3-pillow python3-vacuum-map-parser-base"

BBCLASSEXTEND = "native"
