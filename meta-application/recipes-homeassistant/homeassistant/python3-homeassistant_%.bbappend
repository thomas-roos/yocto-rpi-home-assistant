FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://homeassistant.service"

# meta-homeassistant's own recipe already carries 0001-0004, so this one
# continues the numbering. It fixes the tibber integration reloading its
# config entry once an hour because the OAuth access token (valid 3600s) is
# captured once at setup and never refreshed before use - see the patch
# header and the "Tibber OAuth token" section of the top-level README.
SRC_URI += "file://0005-tibber-refresh-oauth-token-before-api-calls.patch"

# Make the ollama integration retry instead of failing permanently when the
# Ollama server is unreachable at HA startup. The upstream handler converts
# only (TimeoutError, httpx.ConnectError) into ConfigEntryNotReady, but the
# ollama client re-raises connection failures as the *builtin* ConnectionError,
# which is not an httpx.ConnectError - so the entry lands in "setup_error" and
# is never retried. That also takes the whole Assist pipeline down with it,
# because the pipeline names conversation.ollama_conversation as its engine and
# has no fallback. See the patch header and the "Ollama" section of the README.
# No known expiry: still unfixed on upstream dev as of 2026-05-12.
SRC_URI += "file://0006-ollama-retry-setup-when-the-server-is-unreachable.patch"

# Pin a static UID/GID for the homeassistant user instead of relying on
# dynamic system-user allocation order. Without this, a rebuild can hand
# its UID to a different user (it happened: systemd-network took UID 990
# once), which breaks ownership of the persistent /data/var/lib/homeassistant
# content and crash-loops the service until manually chown'd back.
#
# 990 (this image's previous dynamic allocation) is NOT safe to pin: it's
# inside oe-core's dynamic system-UID/GID range (101-999, see
# /etc/login.defs SYS_UID_MIN/MAX), so any newly-added system-user package
# can claim it first on a future rebuild - which is exactly what happened
# when mosquitto was added. 65 is outside that range and outside
# base-passwd's reserved low IDs (checked against a built rootfs's
# /etc/passwd and /etc/group), so it can't collide with dynamic allocation.
#
# NOTE: this changes the UID/GID from what's live on the deployed device
# (990/1000) - after installing an image built with this, chown
# /data/var/lib/homeassistant to 65:65 on the device once.
GROUPADD_PARAM:${PN} = "--gid 65 homeassistant"
USERADD_PARAM:${PN} = "\
    --system --home ${HOMEASSISTANT_CONFIG_DIR} \
    --no-create-home --shell /bin/false \
    --uid 65 \
    --groups homeassistant,dialout --gid homeassistant ${HOMEASSISTANT_USER} \
"
