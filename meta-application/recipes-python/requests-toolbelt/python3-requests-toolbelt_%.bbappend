# meta-python-ai's copy of this recipe (which shadows meta-python's) lacks the
# sdist name override: PyPI publishes requests-toolbelt-1.0.0.tar.gz with a
# hyphen, and pypi.bbclass now defaults to the underscore form, so do_fetch 404s.
PYPI_PACKAGE_SDIST = "requests-toolbelt"
