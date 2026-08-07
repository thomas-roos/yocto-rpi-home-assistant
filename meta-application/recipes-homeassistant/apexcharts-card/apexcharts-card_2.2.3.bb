SUMMARY = "apexcharts-card - graph card for the Home Assistant Lovelace UI"
DESCRIPTION = "A Lovelace custom card built on the ApexCharts library, giving \
Home Assistant dashboards graphs that the built-in history-graph and \
statistics-graph cards cannot draw (mixed y-axes, per-series aggregation, \
now-vs-past comparisons, bar/area/scatter mixing)."
HOMEPAGE = "https://github.com/RomRider/apexcharts-card"
LICENSE = "MIT"

# Upstream ships only a prebuilt, minified bundle as a GitHub release asset -
# building it from source would need a full node/yarn toolchain in the build,
# so the release artifact is fetched directly and pinned by sha256. The LICENSE
# is fetched separately because the minified bundle carries no license header.
LIC_FILES_CHKSUM = "file://apexcharts-card-${PV}.LICENSE;md5=4707aced58ea92ca74af27634ad16bbe"

SRC_URI = "\
    https://github.com/RomRider/apexcharts-card/releases/download/v${PV}/apexcharts-card.js;name=card;downloadfilename=apexcharts-card-${PV}.js \
    https://raw.githubusercontent.com/RomRider/apexcharts-card/v${PV}/LICENSE;name=license;downloadfilename=apexcharts-card-${PV}.LICENSE \
"
SRC_URI[card.sha256sum] = "346780707773fb90c45e9eeaf8acb010d16a8089e86563a242cc4e1d8995f718"
SRC_URI[license.sha256sum] = "2ac0a76d0c13719cbc5204717d71ff604fc6f56847009ae83a80bdecf216af8e"

# both SRC_URI entries are loose files, so they land straight in UNPACKDIR
S = "${UNPACKDIR}"

inherit allarch

# Home Assistant serves <config>/www/ as /local/, so this ends up at
# http://<host>:8123/local/apexcharts-card.js
do_install() {
    install -d ${D}${localstatedir}/lib/homeassistant/www
    install -m 0644 ${S}/apexcharts-card-${PV}.js \
        ${D}${localstatedir}/lib/homeassistant/www/apexcharts-card.js
}

FILES:${PN} += "${localstatedir}/lib/homeassistant/www"

# A Lovelace resource must also be registered before the card is usable, in
# .storage/lovelace_resources (Settings > Dashboards > Resources in the UI):
#   {"type": "module", "url": "/local/apexcharts-card.js?v=${PV}"}
# That file is runtime state on the persistent /data partition, so it is not
# packaged here - see the /data caveat in the top-level README. Bump the ?v=
# query string along with PV so browsers do not serve a stale cached bundle.
