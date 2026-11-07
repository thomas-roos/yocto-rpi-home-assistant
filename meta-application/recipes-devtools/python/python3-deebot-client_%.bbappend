# The zstd-sys crate's build.rs probes for a system libzstd via pkg-config,
# but this maturin/cargo recipe never pulls in pkg-config-native or zstd, so
# the probe fails outright instead of finding a usable library. oe-core's
# zstd recipe happens to be exactly 1.5.7, the version zstd-sys 2.0.16 bundles
# (see its "+zstd.1.5.7" version suffix), so linking against it is safe.
# inherit pkgconfig is required too: DEPENDS alone stages libzstd.pc into the
# sysroot but doesn't export PKG_CONFIG_LIBDIR/PKG_CONFIG_SYSROOT_DIR, so
# pkg-config still can't find it without the class.
inherit pkgconfig
DEPENDS += "pkgconfig-native zstd"
