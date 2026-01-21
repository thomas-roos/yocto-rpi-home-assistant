# Make python3-orjson optional since it doesn't build with Python 3.14
# The pre-built package from cache will be used if available

RDEPENDS:${PN}:remove = "python3-orjson"
RRECOMMENDS:${PN}:append = " python3-orjson"
