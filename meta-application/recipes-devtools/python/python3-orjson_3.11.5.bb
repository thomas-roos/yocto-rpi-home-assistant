SUMMARY = "Fast, correct Python JSON library"
LICENSE = "Apache-2.0 | MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE-APACHE;md5=1836efb2eb779966696f473ee8540542 \
                    file://${S}/LICENSE-MIT;md5=b377b220f43d747efdec40d69fcaa69d"

SRC_URI = "https://files.pythonhosted.org/packages/source/o/orjson/orjson-${PV}.tar.gz"

SRC_URI[sha256sum] = "82393ab47b4fe44ffd0a7659fa9cfaacc717eb617c93cde83795f14af5c2e9d5"

S = "${UNPACKDIR}/orjson-${PV}"

inherit python_setuptools_build_meta

DEPENDS += "python3-pip-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/python3-orjson/files:"
SRC_URI:append = " file://orjson-3.11.5-cp314-cp314-manylinux_2_17_aarch64.manylinux2014_aarch64.whl"

do_compile() {
    :
}

do_install() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    ${STAGING_BINDIR_NATIVE}/pip3 install --no-deps --target=${D}${PYTHON_SITEPACKAGES_DIR} ${UNPACKDIR}/orjson-3.11.5-cp314-cp314-manylinux_2_17_aarch64.manylinux2014_aarch64.whl
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}/*"

INSANE_SKIP:${PN} += "already-stripped"

BBCLASSEXTEND = "native nativesdk"
