LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "\
    git://github.com/pjsip/pjproject.git;protocol=https;nobranch=1 \
    file://config_site.h;subdir=${S}/pjlib/include/pj \
    file://arm-arch.patch \
    file://install-perms.patch \
    file://0010-Avoid_deadlock_between_transport_and_transaction.patch \
"

SRCREV = "2f4bc29b2fa65cc29e50ba03f0b8b6de820eaf6b"

inherit autotools-brokensep

PACKAGECONFIG ??= "epoll ssl"

PACKAGECONFIG[speex-codec] = "--enable-speex-codec,--disable-speex-codec,speex-codec"
PACKAGECONFIG[speex-aec] = "--enable-speex-aec,--disable-speex-aec,speex-aec"
PACKAGECONFIG[bcg729] = "--enable-bcg729,--disable-bcg729,bcg729"
PACKAGECONFIG[gsm-codec] = "--enable-gsm-codec,--disable-gsm-codec,gsm-codec"
PACKAGECONFIG[ilbc-codec] = "--enable-ilbc-codec,--disable-ilbc-codec,ilbc-codec"
PACKAGECONFIG[l16-codec] = "--enable-l16-codec,--disable-l16-codec,l16-codec"
PACKAGECONFIG[g722-codec] = "--enable-g722-codec,--disable-g722-codec,g722-codec"
PACKAGECONFIG[g7221-codec] = "--enable-g7221-codec,--disable-g7221-codec,g7221-codec"
PACKAGECONFIG[opencore-amr] = "--enable-opencore-amr,--disable-opencore-amr,opencore-amr"
PACKAGECONFIG[silk] = "--enable-silk,--disable-silk,silk"
PACKAGECONFIG[opus] = "--enable-opus,--disable-opus,opus"
PACKAGECONFIG[video] = "--enable-video,--disable-video,video"
PACKAGECONFIG[v4l2] = "--enable-v4l2,--disable-v4l2,v4l2"
PACKAGECONFIG[sound] = "--enable-sound,--disable-sound,sound"
PACKAGECONFIG[ext-sound] = "--enable-ext-sound,--disable-ext-sound,ext-sound"
PACKAGECONFIG[sdl] = "--enable-sdl,--disable-sdl,sdl"
PACKAGECONFIG[libyuv] = "--enable-libyuv,--disable-libyuv,libyuv"
PACKAGECONFIG[ffmpeg] = "--enable-ffmpeg,--disable-ffmpeg,ffmpeg"
PACKAGECONFIG[openh264] = "--enable-openh264,--disable-openh264,openh264"
PACKAGECONFIG[ipp] = "--enable-ipp,--disable-ipp,ipp"
PACKAGECONFIG[libwebrtc] = "--enable-libwebrtc,--disable-libwebrtc,libwebrtc"
PACKAGECONFIG[epoll] = "--enable-epoll,--disable-epoll"
PACKAGECONFIG[resample] = "--enable-resample,--disable-resample"
PACKAGECONFIG[g711-codec] = "--enable-g711-codec,--disable-g711-codec"
PACKAGECONFIG[upnp] = "--enable-upnp,--disable-upnp"

## do *NOT* use '--enable-ssl'; this disables every relevant action in
## configure.ac which checks for OpenSSL
PACKAGECONFIG[ssl] = "--with-ssl,--without-ssl --disable-ssl,openssl"

do_unpack:append() {
}

rename_acconfigure() {
	mv ${S}/aconfigure.ac ${S}/configure.ac
}
do_unpack[postfuncs] += "rename_acconfigure"

EXTRA_AUTORECONF += "\
    -I build \
    --exclude=autoheader \
"

EXTRA_OECONF += "\
    --disable-shared \
"

## Not SMP safe
PARALLEL_MAKE = ""

do_compile() {
    oe_runmake lib
}

LD = "${CXX} ${LDFLAGS}"
CXXFLAGS += "-Wno-deprecated"

CLEANBROKEN = "1"

python populate_packages:prepend () {
    libdir = d.expand("${libdir}")
    pnbase = d.expand("${PN}-lib%s")
    do_split_packages(d, libdir, '^lib(.*)\.so\..*', pnbase, '${BPN} %s library',
                      prepend=True, extra_depends = '', allow_links=True)
}
