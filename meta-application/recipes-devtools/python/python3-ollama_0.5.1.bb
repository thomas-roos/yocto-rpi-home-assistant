SUMMARY = "The official Python client for Ollama"
HOMEPAGE = "https://github.com/ollama/ollama-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a8abe7311c869aba169d640cf367a4af"

inherit pypi python_pep517
inherit python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PYPI_PACKAGE = "ollama"

SRC_URI[sha256sum] = "5a799e4dc4e7af638b11e3ae588ab17623ee019e496caaf4323efbaa8feeff93"

RDEPENDS:${PN} += "python3-httpx python3-pydantic"

BBCLASSEXTEND = "native"
