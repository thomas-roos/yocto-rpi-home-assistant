SUMMARY = "Python Rate-Limiter using Leaky-Bucket Algorithm"
HOMEPAGE = "https://github.com/vutran1710/PyrateLimiter"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=70f2270b882e7ad0d2faf8c30a4fab4f"

DEPENDS += "python3-uv-dynamic-versioning-native"

inherit pypi python_hatchling

PYPI_PACKAGE = "pyrate_limiter"

SRC_URI[sha256sum] = "7f03c6a9235b00a4ba3c7b1e8cb6b23fbacc9d40b09f326220917a12ef22cc09"

BBCLASSEXTEND = "native"
