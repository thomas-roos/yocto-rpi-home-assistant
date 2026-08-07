#!/bin/sh
# Generate a self-signed TLS certificate for Home Assistant, once, on the device.
#
# Deliberately NOT generated at image build time: that would ship the same
# private key in every image and put it in git. Generating here means the key is
# unique per device and never leaves it.
#
# The certificate lives under ${HA_DIR}/ssl, and ${HA_DIR} is bind-mounted from
# the /data partition, so it survives RAUC rootfs updates and is generated only
# on a genuinely fresh /data.
#
# Why a certificate at all: browsers only expose getUserMedia() (microphone) in a
# "secure context", so a WebRTC/SIP card in the dashboard needs https even on a
# trusted LAN. Being self-signed, each browser must accept the warning once.

set -eu

HA_DIR=/var/lib/homeassistant
SSL_DIR="${HA_DIR}/ssl"
CRT="${SSL_DIR}/fullchain.pem"
KEY="${SSL_DIR}/privkey.pem"
DAYS=3650

# Still valid for more than 30 days? Then there is nothing to do.
if [ -f "${CRT}" ] && [ -f "${KEY}" ] \
   && openssl x509 -in "${CRT}" -noout -checkend 2592000 >/dev/null 2>&1; then
    echo "ha-selfsigned-cert: existing certificate still valid, nothing to do"
    exit 0
fi

mkdir -p "${SSL_DIR}"
chmod 0750 "${SSL_DIR}"

HOSTNAME_SHORT="$(hostname)"

# Collect every name and address this box may be reached by. A self-signed cert
# is only accepted for the exact host in the URL, so missing a SAN here means a
# fresh warning (or an outright refusal) when reaching it that way.
SANS="DNS:${HOSTNAME_SHORT},DNS:${HOSTNAME_SHORT}.fritz.box,DNS:${HOSTNAME_SHORT}.local,DNS:localhost,IP:127.0.0.1"
for ip in $(ip -4 -o addr show scope global 2>/dev/null | awk '{split($4,a,"/"); print a[1]}'); do
    SANS="${SANS},IP:${ip}"
done

echo "ha-selfsigned-cert: generating certificate for ${SANS}"

openssl req -x509 -nodes -newkey rsa:2048 \
    -keyout "${KEY}" -out "${CRT}" -days "${DAYS}" -sha256 \
    -subj "/CN=${HOSTNAME_SHORT}" \
    -addext "subjectAltName=${SANS}" \
    -addext "basicConstraints=critical,CA:FALSE" \
    -addext "keyUsage=critical,digitalSignature,keyEncipherment" \
    -addext "extendedKeyUsage=serverAuth" >/dev/null 2>&1

# Home Assistant runs as this user and must be able to read the key.
chown homeassistant:homeassistant "${CRT}" "${KEY}" "${SSL_DIR}" 2>/dev/null || true
chmod 0640 "${KEY}"
chmod 0644 "${CRT}"

echo "ha-selfsigned-cert: wrote ${CRT}"
openssl x509 -in "${CRT}" -noout -subject -enddate -ext subjectAltName || true
