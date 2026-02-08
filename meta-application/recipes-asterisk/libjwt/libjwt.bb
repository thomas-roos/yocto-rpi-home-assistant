LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f75d2927d3c1ed2414ef72048f5ad640"

SRC_URI = "\
    git://github.com/benmcollins/libjwt.git;protocol=https;branch=master \
    file://so-name.patch \
"
SRCREV = "43b640c5076e2166287710fa5c3d7054f0228f97"
SRC_URI[sha256sum] = "3b1c18214c1ef87ad06caabe47d7c35ac70900cc60e176461624efa95b2ecda1"

PV = "1.17.0"

inherit pkgconfig

DEPENDS += "jansson"
PACKAGECONFIG ??= "openssl"

inherit autotools

PACKAGECONFIG[openssl] = "--with-openssl,,openssl"
PACKAGECONFIG[gnutls]  = "--without-openssl,,gnutls"
PACKAGECONFIG[examples] = "--with-examples,--without-examples"

# inherit cmake
#
# EXTRA_OECMAKE += "\
#     -DBUILD_SHARED_LIBS=yes \
# "
#
#
#
# PACKAGECONFIG[openssl] = "-DWITHOUT_OPENSSL=OFF,,openssl"
# PACKAGECONFIG[gnutls]  = "-DWITHOUT_OPENSSL=ON,,openssl"
