SUMMARY = "The official Python client for Ollama"
HOMEPAGE = "https://github.com/ollama/ollama-python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a8abe7311c869aba169d640cf367a4af"

inherit pypi python_pep517
inherit python_hatchling

DEPENDS += "python3-hatch-vcs-native"

PYPI_PACKAGE = "ollama"

SRC_URI[sha256sum] = "936d55daa684f474364c098611c933626f8d6c7d67065c5b7ae0c477b508b07f"

RDEPENDS:${PN} += "python3-httpx (>=0.27) python3-pydantic (>=2.9)"

BBCLASSEXTEND = "native"
