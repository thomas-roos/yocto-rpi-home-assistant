# Same zstd-sys/pkg-config issue as python3-deebot-client: see that bbappend
# for details. uv bundles zstd-sys 2.0.15+zstd.1.5.7, same zstd C version as
# oe-core's zstd recipe.
inherit pkgconfig
DEPENDS += "pkgconfig-native zstd"
