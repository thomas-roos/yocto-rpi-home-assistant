# Same zstd-sys/pkg-config issue as python3-deebot-client: see that bbappend
# for details.
inherit pkgconfig
DEPENDS += "pkgconfig-native zstd"
