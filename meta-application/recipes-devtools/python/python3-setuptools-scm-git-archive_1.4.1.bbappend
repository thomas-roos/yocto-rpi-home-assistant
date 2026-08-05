# Newer setuptools no longer ships pkg_resources, which this package's
# ancient setup.py imports unconditionally for a legacy bootstrap trick.
# See the patch for details.
FILESEXTRAPATHS:prepend := "${THISDIR}/python3-setuptools-scm-git-archive:"
SRC_URI:append = " file://0002-support-setuptools-without-pkg_resources.patch"
