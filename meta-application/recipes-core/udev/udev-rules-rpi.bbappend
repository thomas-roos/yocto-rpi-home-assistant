# 99-com.rules (installed by this recipe) assigns device nodes to the
# gpio/spi/i2c groups, but nothing on this distro creates those groups,
# so udev logs "Failed to resolve group ..., ignoring" for each on every
# boot. Harmless (root runs every service here), but create them anyway
# so they exist if a future service needs group-based device access.
inherit useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system gpio; --system spi; --system i2c"
