# pve

# 设置源
deb https://mirrors.ustc.edu.cn/debian/ bookworm main contrib non-free non-free-firmware
deb https://mirrors.ustc.edu.cn/debian/ bookworm-updates main contrib non-free non-free-firmware
deb https://mirrors.ustc.edu.cn/debian/ bookworm-backports main contrib non-free non-free-firmware
deb https://mirrors.ustc.edu.cn/debian-security/ bookworm-security main contrib non-free non-free-firmware

#  导入
qm disk import 101 win11.qcow2 local-lvm --format=qcow2

qm disk import 105 ubuntu-23.04-server-cloudimg-amd64.img local-lvm

# 虚拟机磁盘扩容
web 扩容
pvs
vgs
lvs
pvresize -v /dev/sda3
lvextend -l +100%FREE /dev/mapper/ubuntu--vg-ubuntu--lv
resize2fs /dev/mapper/ubuntu--vg-ubuntu--lv


# 直通
vim /etc/default/grub
GRUB_CMDLINE_LINUX_DEFAULT="quiet intel_iommu=on"
update-grub
reboot