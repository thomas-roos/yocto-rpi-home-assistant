SUMMARY = "Fast, simple packet creation / parsing, with definitions for basic TCP/IP protocols"
HOMEPAGE = "https://github.com/kbandla/dpkt"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=331b9d636187a21a45111966949ad115"

inherit pypi setuptools3

PYPI_PACKAGE = "dpkt"

SRC_URI[sha256sum] = "43f8686e455da5052835fd1eda2689d51de3670aac9799b1b00cfd203927ee45"

BBCLASSEXTEND = "native"
