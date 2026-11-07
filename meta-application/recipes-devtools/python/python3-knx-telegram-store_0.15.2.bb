SUMMARY = "Host-agnostic Python library for KNX telegram persistence"
HOMEPAGE = "https://github.com/XKNX/knx-telegram-store"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=768804e722cc42fd18012f62916254f7"

SRC_URI[sha256sum] = "0144df0891e673599ce151b7d01378eadba79e9f39dd5f420bde72f43ff293b5"

inherit pypi python_setuptools_build_meta

# Home Assistant installs the "sqlite" and "postgres" extras. Only the
# SQLite backend, which the KNX integration uses by default, is provided;
# asyncpg for the PostgreSQL backend is not packaged.
RDEPENDS:${PN} += " \
    python3-aiosqlite (>=0.20) \
    python3-defusedxml (>=0.7.1) \
    python3-greenlet \
    python3-sqlalchemy (>=2.0) \
"
