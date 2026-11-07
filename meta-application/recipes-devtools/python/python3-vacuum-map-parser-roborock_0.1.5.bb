SUMMARY = "Vacuum map parser for Roborock devices"
HOMEPAGE = "https://github.com/PiotrMachowski/Python-package-vacuum-map-parser-roborock"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=103aa1e1748c15e5e6595aa349e4959f"

inherit pypi python_poetry_core

PYPI_PACKAGE = "vacuum_map_parser_roborock"

SRC_URI[sha256sum] = "906109447d350f493ba8b59496c1d3075f4527173d7b02842d63a897da11ea1e"

RDEPENDS:${PN} += "python3-pillow python3-vacuum-map-parser-base (>=0.1.5)"

BBCLASSEXTEND = "native"
