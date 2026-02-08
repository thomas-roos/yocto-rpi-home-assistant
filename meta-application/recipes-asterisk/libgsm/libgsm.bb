DESCRIPTION = "GSM Audio Library"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=fc1372895b173aaf543a122db37e04f5"

PV = "1.0.22"

SRC_URI = "\
    http://www.quut.com/gsm/gsm-${PV}.tar.gz \
    file://dyn-lib.patch \
    file://fix-signal-handler.patch \
"
SRC_URI[sha256sum] = "f0072e91f6bb85a878b2f6dbf4a0b7c850c4deb8049d554c65340b3bf69df0ac"
S = "${UNPACKDIR}/gsm-1.0-pl22"

EXTRA_OEMAKE = "\
    CC='${CC}' \
    LD='${CC}' \
    LN='ln -s' \
    RANLB='${RANLIB}' \
    CCFLAGS='${CFLAGS} -fPIC -DNeedFunctionPrototypes=1 -c' \
    GSM_INSTALL_LIB='$(DESTDIR)${libdir}' \
    GSM_INSTALL_INC='$(DESTDIR)${includedir}/gsm' \
    GSM_INSTALL_MAN='$(DESTDIR)${mandir}' \
    TOAST_INSTALL_BIN='$(DESTDIR)${bindir}' \
    TOAST_INSTALL_MAN='$(DESTDIR)${mandir}' \
    GSM_INSTALL_ROOT=x \
    TOAST_INSTALL_ROOT=x \
"

PARALLEL_MAKE = ""

do_install() {
	install -d -m 0755 ${D}${libdir} ${D}${bindir} ${D}${mandir} ${D}${includedir}/gsm
	oe_runmake install DESTDIR='${D}'

	## replace absolute links
	for p in tcat untoast; do
	    p=${D}${bindir}/$p
	    test -L "$p"
	    ln -sfr "$(readlink $p)" "$p"
	done
}

PACKAGES =+ "${PN}-bin"
FILES:${PN}-bin = "${bindir}/*"

BBCLASSEXTEND = "native nativesdk"
