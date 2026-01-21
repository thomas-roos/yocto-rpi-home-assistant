# Python 3.14+ compatibility - isal uses Cython which should be compatible
# Fix cross-compilation issue where x86_64 assembly is built for ARM
do_compile:prepend() {
    # Force detection of correct architecture
    export SYSTEM_IS_UNIX=1
    export SYSTEM_IS_WINDOWS=0
}
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-Fix-cross-compilation-for-aarch64.patch"

# Export target architecture for cross-compilation detection
export ISAL_TARGET_ARCH = "${TARGET_ARCH}"

