# setup.py's setup_requires triggers setuptools' legacy fetch_build_eggs,
# which shells out to pip to satisfy its own numpy build-time version pin
# (numpy<2.5) - but our numpy-native is 2.5.1 (outside that range) and
# do_compile has no network access anyway. The real numpy dependency is
# already correctly provided via DEPENDS, so just drop this redundant
# legacy check instead of trying to make the network fetch work.
do_compile:prepend() {
    sed -i "s/setup_requires=build_requires,/setup_requires=[],/" ${S}/setup.py
}
