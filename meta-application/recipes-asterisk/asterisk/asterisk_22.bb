FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-20:"

LIC_FILES_CHKSUM = "\
    file://COPYING;md5=3c6764ffcbe996d1d8f919b393ccdd67 \
    file://LICENSE;md5=97a6cefc13490dd086081452a01aee5c \
"

SND_BASE_URI = "http://downloads.asterisk.org/pub/telephony/sounds/releases"

SRC_URI = "\
    git://github.com/asterisk/asterisk.git;protocol=https;nobranch=1 \
    ${SND_BASE_URI}/asterisk-moh-opsound-wav-2.03.tar.gz;unpack=0;name=moh-sounds;subdir=${S}/sounds \
    ${SND_BASE_URI}/asterisk-core-sounds-en-gsm-1.6.1.tar.gz;unpack=0;name=core-sounds-en-gsm;subdir=${S}/sounds \
    file://paths.patch \
    file://asterisk.tmpfiles \
    file://asterisk.service \
    file://asterisk.socket \
"
PV = "22.8.2"
SRCREV = "5806560015553254c709ddbe4a235fa946b689c2"

EXTRA_AUTORECONF = ""

SRC_URI[moh-sounds.sha256sum] = "449fb810d16502c3052fedf02f7e77b36206ac5a145f3dacf4177843a2fcb538"
SRC_URI[core-sounds-en-gsm.sha256sum] = "d79c3d2044d41da8f363c447dfccc140be86b4fcc41b1ca5a60a80da52f24f2d"

require asterisk.inc
