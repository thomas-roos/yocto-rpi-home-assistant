# meta-homeassistant's own python3-zha_0.0.80 pins zigpy==0.87.0 in its
# pyproject.toml, but meta-homeassistant's python3-zigpy recipe is only at
# 0.83.0 - an internal version mismatch within that layer's current master,
# not something introduced by our layer bump. zigpy 0.87.0 doesn't export
# CONF_NWK_COUNTRY_CODE from zigpy.config, which zha imports unconditionally,
# so ZHA fails to load entirely without this bump.
SUMMARY = "Library implementing a Zigbee stack"
HOMEPAGE = "https://github.com/zigpy/zigpy"
LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS += "\
    python3-setuptools-git-versioning-native \
"

SRC_URI[sha256sum] = "aca07ef085aa78302e67a03a836f40134c6ecbfb281cf243d435f50fd08f056a"

inherit pypi python_setuptools_build_meta ptest-python-pytest

PYPI_PACKAGE = "zigpy"

RDEPENDS:${PN} = "\
    python3-attrs \
    python3-aiohttp \
    python3-aiosqlite (>=0.20.0) \
    python3-crccheck \
    python3-cryptography \
    python3-voluptuous \
    python3-jsonschema \
    python3-pyserial-asyncio \
    python3-typing-extensions \
    python3-frozendict \
"
