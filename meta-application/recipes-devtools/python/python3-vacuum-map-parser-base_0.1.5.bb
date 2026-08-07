SUMMARY = "Vacuum map parser - base package"
HOMEPAGE = "https://github.com/PiotrMachowski/Python-package-vacuum-map-parser-base"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=103aa1e1748c15e5e6595aa349e4959f"

inherit pypi python_poetry_core

PYPI_PACKAGE = "vacuum_map_parser_base"

SRC_URI[sha256sum] = "efbf889ae7a7a8fe6478354a1711e857ee781c2d7f3a09e5b30e714b60036c4a"

RDEPENDS:${PN} += "python3-pillow"

BBCLASSEXTEND = "native"
