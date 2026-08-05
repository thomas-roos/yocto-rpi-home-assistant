# Newer setuptools no longer ships pkg_resources, which this package's
# setup.py used only to parse requirements.txt. See the patch for details.
FILESEXTRAPATHS:prepend := "${THISDIR}/python3-adax-local:"
SRC_URI:append = " file://0001-support-setuptools-without-pkg_resources.patch"
