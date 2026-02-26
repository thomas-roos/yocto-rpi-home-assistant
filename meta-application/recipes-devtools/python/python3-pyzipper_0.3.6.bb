SUMMARY = "A replacement for Python’s zipfile that can read and write AES encrypted zip files."
HOMEPAGE = "https://github.com/danifus/pyzipper"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4a6ce89f8836606b2fa79b7d8e898868"

inherit pypi python_pep517
inherit python_setuptools_build_meta

PYPI_PACKAGE = "pyzipper"

SRC_URI[sha256sum] = "0adca90a00c36a93fbe49bfa8c5add452bfe4ef85a1b8e3638739dd1c7b26bfc"

