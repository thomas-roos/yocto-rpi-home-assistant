# Avoid file conflicts with tests directory
do_install:append() {
    rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/tests
}
