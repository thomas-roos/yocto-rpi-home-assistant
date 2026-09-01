FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://no-magicdns.conf"

# See the comment in no-magicdns.conf: MagicDNS breaks all name resolution on this
# device, so tailscaled is told not to touch /etc/resolv.conf.
do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}/tailscaled.service.d
        install -m 0644 ${UNPACKDIR}/no-magicdns.conf \
            ${D}${systemd_system_unitdir}/tailscaled.service.d/no-magicdns.conf
    fi
}
