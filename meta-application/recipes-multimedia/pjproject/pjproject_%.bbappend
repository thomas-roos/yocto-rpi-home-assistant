# Asterisk registers more PJSIP modules than pjproject's default PJSIP_MAX_MODULE
# allows: Asterisk 22.11 aborts at startup with
#   pjsip_endpt_register_module: Assertion `!"Too many modules registered!"' failed.
# Asterisk's own bundled pjproject sets these in
# third-party/pjproject/patches/config_site.h (22.11.0: PJSIP_MAX_MODULE 38,
# PJ_MAX_SOCKOPT_PARAMS 5 for the TCP keepalive options). We build against the
# external pjproject, so set the same limits here. config_site.h is included by
# pj/config.h and installed with the headers, so Asterisk compiles against the
# same values.
do_configure:prepend() {
	cat > ${S}/pjlib/include/pj/config_site.h <<'EOH'
#define PJSIP_MAX_MODULE 38
#define PJ_MAX_SOCKOPT_PARAMS 5
EOH
}
