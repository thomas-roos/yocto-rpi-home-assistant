SUMMARY = "Generate a self-signed TLS certificate for Home Assistant on first boot"
DESCRIPTION = "Home Assistant serves plain HTTP by default. Browsers only grant \
getUserMedia() (microphone) access in a secure context, so a WebRTC/SIP card in \
the dashboard needs https even on a trusted LAN. This generates a self-signed \
certificate on the device at first boot - not at image build time, which would \
put one shared private key in every image and in git."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://ha-gen-selfsigned-cert.sh \
    file://ha-selfsigned-cert.service \
"

inherit allarch

# Deliberately NOT using inherit systemd / SYSTEMD_SERVICE. That enables a unit by
# symlinking it into ${sysconfdir}/systemd/system/multi-user.target.wants/ - and on
# this image /etc/systemd/system is bind-mounted from the /data partition, so on an
# already-provisioned device the image's symlink is shadowed by the existing /data
# content and the unit silently never runs. (Exactly how tailscaled ended up
# "is-enabled: disabled" despite shipping an enable preset.)
#
# Installing the wants-symlink under ${systemd_system_unitdir} instead puts it on the
# rootfs, which /data does not shadow, so the service is enabled on fresh flashes AND
# on updated devices alike. systemd honours .wants directories under /usr/lib exactly
# like those under /etc.

# openssl for the certificate itself, iproute2 for enumerating the addresses
# that go into subjectAltName (busybox ip is not guaranteed to support -o).
RDEPENDS:${PN} += "openssl-bin iproute2"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/ha-gen-selfsigned-cert.sh ${D}${bindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/ha-selfsigned-cert.service ${D}${systemd_system_unitdir}/
    sed -i -e 's,@BINDIR@,${bindir},g' \
        ${D}${systemd_system_unitdir}/ha-selfsigned-cert.service

    # enable it on the rootfs, not in /etc - see the note above
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants
    ln -sf ../ha-selfsigned-cert.service \
        ${D}${systemd_system_unitdir}/multi-user.target.wants/ha-selfsigned-cert.service
}

FILES:${PN} += "${systemd_system_unitdir}"

# NOTE: generating the certificate is only half of it - Home Assistant also needs
#   http:
#     ssl_certificate: /var/lib/homeassistant/ssl/fullchain.pem
#     ssl_key: /var/lib/homeassistant/ssl/privkey.pem
# in configuration.yaml. That file is live device state on /data (like
# automations.yaml) and is deliberately not packaged - see the /data caveat in
# the top-level README.
