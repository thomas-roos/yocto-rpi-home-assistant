SUMMARY = "KNXD extends the IP-reach of the KNX bus and supports multiple concurrent clients"
SECTION = "base"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=8264535c0c4e9c6c335635c4026a8022"

SRC_URI = "git://github.com/knxd/knxd.git;protocol=https;branch=main \
           file://knxd.service \
           file://knxd.socket \
           file://knxd.conf \
           "

SRCREV = "5b5514c32cc6cb3a436beb1a52885044b4a96fcf"

inherit autotools-brokensep gettext pkgconfig systemd

EXTRA_OECONF = "--enable-eibnetip --enable-eibnetiptunnel --enable-usb --enable-eibnetipserver --enable-systemd \
                --enable-ft12 --enable-dummy --enable-groupcache --enable-tpuart \
               "

DEPENDS += "libev systemd libusb fmt"
RDEPENDS:${PN} = "libev"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/knxd.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/knxd.socket ${D}${systemd_unitdir}/system
    install -d ${D}${sysconfdir}/knxd
    install -m 0644 ${UNPACKDIR}/knxd.conf ${D}${sysconfdir}/knxd/

    rm -r ${D}${libdir}/sysusers.d
}

PACKAGES =+ " ${PN}-examples-dbg  ${PN}-examples"

FILES:${PN}-examples += "${datadir}/knxd/examples \
                         ${datadir}/knxd/eibclient.php \
                         ${datadir}/knxd/EIB* \
                        "

SYSTEMD_SERVICE:${PN} = "knxd.service knxd.socket knxd-net.socket"

