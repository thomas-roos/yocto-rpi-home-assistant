SUMMARY = "orjson is a fast, correct JSON library for Python"
HOMEPAGE = "https://pypi.org/project/orjson/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

SRC_URI[sha256sum] = "82393ab47b4fe44ffd0a7659fa9cfaacc717eb617c93cde83795f14af5c2e9d5"

SRC_URI += "file://001_Gate-avx512-under-x86_64-target.patch"

CVE_PRODUCT = "orjson"

require ${BPN}-crates.inc

inherit pypi python_maturin cargo-update-recipe-crates

DEPENDS = "python3-maturin-native"

RDEPENDS:${PN} += "python3-maturin python3-mypy"

do_compile:prepend() {
    sed -i "/panic = \"abort\"/d" ${S}/Cargo.toml
}

BBCLASSEXTEND = "native nativesdk"
