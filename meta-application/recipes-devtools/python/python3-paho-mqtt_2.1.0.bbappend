# OpenSSL 4 builds of CPython drop ssl.PROTOCOL_TLSv1_2; without this
# tls_set() raises AttributeError (breaks the Roborock cloud MQTT session).
FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"
SRC_URI += "file://0001-Fix-tls_set-default-when-ssl.PROTOCOL_TLSv1_2-is-missing.patch"
