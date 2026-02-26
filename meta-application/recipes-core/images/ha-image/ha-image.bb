SUMMARY = "My homeassistant image with rauc A/B updates, asterisk, knxd"
HOMEPAGE = "https://github.com/aws4embeddedlinux/meta-aws-demos"

LICENSE = "MIT"

# this needs to be done before installing the (dynamic) packagegroups
inherit core-image

IMAGE_INSTALL += "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    packagegroup-base \
    packagegroup-core-boot \
    "

### rauc ###
CORE_IMAGE_EXTRA_INSTALL:append:rpi = " rauc-grow-data-part"

# only adding if device is rpi, as others might have a different partition layout
IMAGE_INSTALL:append:rpi = " config-init"

# this will allow kernel updates with rauc
IMAGE_INSTALL:append = " kernel-image kernel-modules"

### tmux ###
IMAGE_INSTALL:append = " tmux"
GLIBC_GENERATE_LOCALES = "en_US.UTF-8 UTF-8"
IMAGE_INSTALL:append = " glibc-utils localedef "
IMAGE_INSTALL:append = " ssh openssh-sshd openssh-sftp openssh-scp"

### misc ###
IMAGE_INSTALL:append = " sudo"

# this will disable root password - be warned!
EXTRA_IMAGE_FEATURES ?= "allow-empty-password allow-root-login empty-root-password"

EXTRA_IMAGE_FEATURES += "ssh-server-openssh"

### license compliance ###
COPY_LIC_MANIFEST = "1"

COPY_LIC_DIRS = "1"

# IMAGE_FEATURES += "read-only-rootfs"

# fstab should be equal to sdimage-ha_partition.wks.in file,
# for rauc bundle generation wic file is not used!
ROOTFS_POSTPROCESS_COMMAND = "rootfs_user_fstab_common"
ROOTFS_POSTPROCESS_COMMAND:append:rpi = " ; rootfs_user_fstab_rpi"
ROOTFS_POSTPROCESS_COMMAND:append:qemux86-64 = " ; rootfs_user_fstab_qemu"

rootfs_user_fstab_common () {

# Set hostname
echo "ha" > ${IMAGE_ROOTFS}/${sysconfdir}/hostname

# overwrite the default fstab, adding customization for this image
cat << EOF > ${IMAGE_ROOTFS}/${sysconfdir}/fstab
/dev/root            /                    auto       defaults              1  1
proc                 /proc                proc       defaults              0  0
devpts               /dev/pts             devpts     mode=0620,ptmxmode=0666,gid=5      0  0
tmpfs                /run                 tmpfs      mode=0755,nodev,nosuid,strictatime 0  0
tmpfs                /var/volatile        tmpfs      defaults              0  0
LABEL=data     /data     ext4    x-systemd.growfs        0       0
/data/etc/systemd/network            /etc/systemd/network            none    bind            0       0
/data/etc/systemd/system            /etc/systemd/system            none    bind            0       0
/data/etc/asterisk            /etc/asterisk            none    bind            0       0
/data/etc/knxd            /etc/knxd            none    bind            0       0
/data/var/lib/homeassistant            /var/lib/homeassistant            none    bind            0       0
EOF

install -d -m 0755 ${IMAGE_ROOTFS}/data

# copy those directories that should be present at the data partition to /data and just
# leave them empty as a mount point for the bind mount

install -d ${IMAGE_ROOTFS}/data/etc/wpa_supplicant

install -d ${IMAGE_ROOTFS}/data/etc/systemd/network
if [ -n "$(ls -A ${IMAGE_ROOTFS}/etc/systemd/network 2>/dev/null)" ]; then
    mv -f ${IMAGE_ROOTFS}/etc/systemd/network/* ${IMAGE_ROOTFS}/data/etc/systemd/network
fi

install -d ${IMAGE_ROOTFS}/data/etc/systemd/system
if [ -n "$(ls -A ${IMAGE_ROOTFS}/etc/systemd/system 2>/dev/null)" ]; then
    mv -f ${IMAGE_ROOTFS}/etc/systemd/system/* ${IMAGE_ROOTFS}/data/etc/systemd/system
fi

install -d ${IMAGE_ROOTFS}/data/etc/asterisk
if [ -n "$(ls -A ${IMAGE_ROOTFS}/etc/asterisk 2>/dev/null)" ]; then
    mv -f ${IMAGE_ROOTFS}/etc/asterisk/* ${IMAGE_ROOTFS}/data/etc/asterisk
fi

install -d ${IMAGE_ROOTFS}/data/etc/knxd
if [ -n "$(ls -A ${IMAGE_ROOTFS}/etc/knxd 2>/dev/null)" ]; then
    mv -f ${IMAGE_ROOTFS}/etc/knxd/* ${IMAGE_ROOTFS}/data/etc/knxd
fi

install -d ${IMAGE_ROOTFS}/data/var/lib/homeassistant
chown homeassistant:homeassistant ${IMAGE_ROOTFS}/data/var/lib/homeassistant
if [ -n "$(ls -A ${IMAGE_ROOTFS}/var/lib/homeassistant 2>/dev/null)" ]; then
    mv -f ${IMAGE_ROOTFS}/var/lib/homeassistant/* ${IMAGE_ROOTFS}/data/var/lib/homeassistant
fi

# decided to do here instead of a bbappend of wpa:supplicant
# install -d ${IMAGE_ROOTFS}/${sysconfdir}/systemd/system/multi-user.target.wants/
# ln -sf /${libdir}/systemd/system/wpa_supplicant@.service ${IMAGE_ROOTFS}/${sysconfdir}/systemd/system/multi-user.target.wants/wpa_supplicant@wlan0.service

# end rootfs_user_fstab_common
}

rootfs_user_fstab_rpi () {
cat << EOF >> ${IMAGE_ROOTFS}/${sysconfdir}/fstab
LABEL=boot    /boot   vfat    defaults         0       0
# /data/etc/wpa_supplicant             /etc/wpa_supplicant             none    bind            0       0
/data/var/lib/alsa      /var/lib/alsa      none    bind            0       0
EOF
# end rootfs_user_fstab_rpi
}

rootfs_user_fstab_qemu () {
cat << EOF >> ${IMAGE_ROOTFS}/${sysconfdir}/fstab
/dev/sda2    /grubenv             auto       defaults,sync  0  0
EOF
# end rootfs_user_fstab_qemu
}

# Optimizations for RAUC adaptive method 'block-hash-index'
# rootfs image size must to be 4K-aligned
IMAGE_ROOTFS_ALIGNMENT = "4"

# ext4 block size should be set to 4K and use a fixed directory hash seed to
# reduce the image delta size (keep oe-core's 4K bytes-per-inode)
EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096 -E hash_seed=86ca73ff-7379-40bd-a098-fcb03a6e719d"

# taken from meta-virtualizion
# Use local.conf to specify additional systemd services to disable. To overwrite
# the default list use SERVICES_TO_DISABLE:pn-systemd-container in local.conf
# TODO: enable asterisk.service knxd.service knxd.socket knxd-net.socket
SERVICES_TO_DISABLE:rpi = "systemd-userdbd.service systemd-userdbd.socket systemd-networkd-persistent-storage.service knxd.service knxd.socket knxd-net.socket"
SERVICES_TO_DISABLE:qemux86-64 = "systemd-userdbd.service systemd-userdbd.socket systemd-networkd-persistent-storage.service asterisk.service knxd.service knxd.socket knxd-net.socket"

disable_systemd_services () {
	SERVICES_TO_DISABLE="${SERVICES_TO_DISABLE}"
	if [ -n "$SERVICES_TO_DISABLE" ]; then
		echo "Disabling systemd services:"
		for service in $SERVICES_TO_DISABLE; do
			echo "    $service"
			systemctl --root="${IMAGE_ROOTFS}" mask $service > /dev/null >1
		done
	fi
}

enable_systemd_services () {
	SERVICES_TO_ENABLE="${SERVICES_TO_ENABLE}"
	if [ -n "$SERVICES_TO_ENABLE" ]; then
		echo "Enabling additional systemd services:"
		for service in $SERVICES_TO_ENABLE; do
			echo "    $service"
			systemctl --root="${IMAGE_ROOTFS}" enable $service > /dev/null >1
		done
	fi
}

ROOTFS_POSTPROCESS_COMMAND += "disable_systemd_services; enable_systemd_services;"

### homeassistant ###
IMAGE_INSTALL:append = " python3-homeassistant python3-homeassistant-frontend"
IMAGE_INSTALL:append = " python3-xknx python3-xknxproject python3-pyzipper knx-frontend python3-striprtf python3-pycryptodomex"

IMAGE_INSTALL:append = " python3-pysmlight"

IMAGE_INSTALL:append = " \
    python3-zigpy \
    python3-zigpy-deconz \
    python3-zigpy-xbee \
    python3-zigpy-zigate \
    python3-zigpy-znp \
"		

IMAGE_INSTALL:append = " python3-roborock"

### additional ###
IMAGE_INSTALL:append = " asterisk"

IMAGE_INSTALL:append = " knxd"

# debug tools
IMAGE_INSTALL:append = " lsof ldd"

# misc
IMAGE_INSTALL:append = " python3-misc python3-venv python3-tomllib python3-ensurepip libcgroup python3-pip"
