SUMMARY = "Python Rate-Limiter using Leaky-Bucket Algorithm"
HOMEPAGE = "https://github.com/vutran1710/PyrateLimiter"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=70f2270b882e7ad0d2faf8c30a4fab4f"

inherit pypi python_poetry_core

PYPI_PACKAGE = "pyrate_limiter"

SRC_URI[sha256sum] = "6b882e2c77cda07a241d3730975daea4258344b39c878f1dd8849df73f70b0ce"

BBCLASSEXTEND = "native"
