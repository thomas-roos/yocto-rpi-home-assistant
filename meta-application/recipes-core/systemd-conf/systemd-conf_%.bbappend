FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://override.conf"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}/systemd-timesyncd.service.d/
    install -m 0644 ${UNPACKDIR}/override.conf ${D}${systemd_system_unitdir}/systemd-timesyncd.service.d/

    # enable watchdog on systemd configuration
    if ${@bb.utils.contains('MACHINE_FEATURES','watchdog','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system.conf.d/
        echo "[Manager]" > ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
        echo "RuntimeWatchdogSec=60" >> ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
        echo "ShutdownWatchdogSec=60" >> ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
        echo "RebootWatchdogSec=60" >> ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
    fi
}

FILES:${PN} += "/usr/lib/systemd/system/systemd-timesyncd.service.d/override.conf"
