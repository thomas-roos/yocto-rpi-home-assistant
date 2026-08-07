SUMMARY = "SIP Client custom Home Assistant integration (registers to a SIP server/PBX)"
DESCRIPTION = "Native SIP endpoint for Home Assistant. Registers to a SIP server \
(here: the Asterisk running on this same device) and exposes the line as a \
media_player entity, with services for dial/answer/hangup/send_dtmf, an IVR \
engine, and a bridge into Home Assistant's Voice Assist."
HOMEPAGE = "https://github.com/eigger/hass-sip"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=abcd90f13d7cc0f435275a80e0ddfcb8"

# Release 1.5.0 (2026-08-05). Pure-Python SIP stack - manifest.json declares no
# pip requirements at all, so there is nothing to add to RDEPENDS for the SIP
# side itself.
SRC_URI = "git://github.com/eigger/hass-sip.git;protocol=https;branch=main"
SRCREV = "47a960755b7315df1da2f6baf55581fb59bb2535"

inherit allarch

do_install() {
    install -d ${D}${localstatedir}/lib/homeassistant/custom_components/sip
    cp -r ${S}/custom_components/sip/. ${D}${localstatedir}/lib/homeassistant/custom_components/sip/
}

FILES:${PN} += "${localstatedir}/lib/homeassistant/custom_components/sip"

# manifest.json has "dependencies": ["ffmpeg", "tts"] and
# "after_dependencies": ["assist_pipeline", "stt"]. Those are Home Assistant
# components rather than pip packages, but the ffmpeg component needs the actual
# ffmpeg binary at runtime, so pull it in explicitly instead of relying on it
# arriving transitively via some other recipe.
RDEPENDS:${PN} += "ffmpeg"

# Configured against the local Asterisk as endpoint 1104 (see
# meta-application/recipes-asterisk/asterisk/files/pjsip.conf and the [Features]
# context in extensions.conf, where 1104 was added to the doorphone group call).
# The config entry itself is runtime state in .storage/core.config_entries and is
# therefore not packaged - see the /data caveat in the top-level README.
#
# NOTE: audio only. This integration negotiates PCMU/PCMA/G.722 and contains no
# video support whatsoever, so it cannot replace a video-capable softphone for a
# doorphone camera stream.
