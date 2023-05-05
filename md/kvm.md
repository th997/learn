# kvm

## 安装

```
sudo apt install qemu qemu-kvm libvirt-daemon-system libvirt-clients bridge-utils virt-manager virtinst
sudo systemctl enable libvirtd 
sudo systemctl start libvirtd 
sudo virt-manager
```

## kvm 格式转化

```
qemu-img convert -f raw -O qcow2 /images/centos6.img /images/centos6.qcow2
qemu-img info centos6.qcow2
```

## 压缩

qemu-img convert -c -O qcow2 test1.qcow2 test2.qcow2

## 修改大小

qemu-img resize t.qcow2 +1G

## kvm 桥接网络
netplan (sudo apt install netplan.io)

```
cd /etc/netplan/
cp 01-network-manager-all.yaml 01-network-manager-all.yaml.bak

network:
  version: 2
  renderer: networkd
  ethernets:
    enp0s31f6:
      dhcp4: false
      dhcp6: false
  bridges:
    kvm_br:
      interfaces: [enp0s31f6]
      dhcp4: false
      addresses: [10.10.10.106/24]
      #gateway4: 10.10.10.1
      routes:
        - to: 0.0.0.0/0
          via: 10.10.10.1
      nameservers:
        addresses: [127.0.0.1]
      dhcp6: true

sudo netplan apply   

systemctl disable NetworkManager
systemctl restart systemd-networkd

https://devtutorial.io/how-to-setup-bridged-networking-for-kvm-in-ubuntu-20-04.html
```

## 安装系统

```
virt-install --virt-type=kvm --name centos6 --ram 2048 --vcpus=2 --os-variant=centos6.0 --cdrom=/mnt/h/os/CentOS-6.3-x86_64-minimal.iso --network=bridge=kvm_br,model=virtio --graphics vnc --disk path=/home/th/vm/centos6.qcow2,format=qcow2,size=10

virt-install --virt-type=kvm --name ubu2004 --ram 2048 --vcpus=2 --os-variant=ubuntu18.04 --cdrom=/mnt/h/os/ubuntu-20.04-desktop-amd64.iso --network=bridge=kvm_br,model=virtio --graphics vnc --disk path=/home/th/vm/ubu2004.qcow2,format=qcow2,size=10

```

## windows共享剪贴板 
https://dausruddin.com/how-to-enable-clipboard-and-folder-sharing-in-qemu-kvm-on-windows-guest/

https://www.spice-space.org/download/windows/spice-guest-tools/spice-guest-tools-latest.exe 

Or easier, shortened URL: https://cutt.ly/SROqBqZ

## windows降低cpu使用
virsh edit vm-name
```xml
  <clock offset="utc">
    <timer name="hpet" present="yes"/>
    <timer name="hypervclock" present="yes"/>
  </clock>
```
## windows 激活
```
卸载密钥
slmgr /upk
安装密钥
slmgr /ipk W269N-WFGWX-YVC9B-4J6C9-T83GX
kms激活,kms服务器(openwrt支持)
slmgr /skms 10.10.10.235
slmgr /ato
```

## cloud image

vim init.yml

```yml
#cloud-config
# https://cloudinit.readthedocs.io/en/latest/topics/examples.html
hostname: ubuntu
timezone: UTC
ssh_pwauth: true
disable_root: true
chpasswd: { expire: false }
#password: ubuntu
#package_upgrade: true
#package_reboot_if_required: true
#packages:
#  - apt-transport-https
#  - ca-certificates
#  - curl
#  - gnupg-agent
#  - software-properties-common
users:
  - name: th
    lock-passwd: false
    shell: /bin/bash
    sudo:
      - ALL=(ALL) ALL
    # mkpasswd --method=SHA-512 --rounds=4096
    # 123456
    passwd: $6$rounds=4096$q/aa9wUuu3NnhUmN$o9nzBT5lTZG3Jn6NnG.qzX262PM6RY01N8uXNOadzNhn5f.F7TmyQiO.YYL/pQby6TUuG/WJSs6YSbbzEVnSg.
apt:
  primary:
    - arches: [ default ]
      uri: "http://mirrors.aliyun.com/ubuntu/"
      search:
        - "http://mirrors.aliyun.com/ubuntu/"
#runcmd:
#  - apt remove cloud-init -y
```

```shell
# wget https://cloud-images.ubuntu.com/releases/22.04/release/ubuntu-22.04-server-cloudimg-amd64.img -O ubu2204.img.bak
# wget https://cloud-images.ubuntu.com/releases/20.04/release/ubuntu-20.04-server-cloudimg-amd64.img -O ubu2004.img.bak
export NAME=ubu2204a
export IP=10.10.10.201
export GATEWAY=10.10.10.1
cat > network.yml <<EOF
version: 2
ethernets:
  enp1s0:
    dhcp4: false
    addresses: [ $IP/24 ]
    gateway4: $GATEWAY
    nameservers:
      addresses: [ $GATEWAY ]
EOF
sed -i 's/\$IP/$IP/' network.yml
sed -i 's/\$GATEWAY/$GATEWAY/' network.yml
sed -i "s/hostname:.*/hostname: $NAME/" init.yml
cloud-localds -v --network-config=network.yml  $NAME-init.img init.yml
cp ubu2204.img.bak $NAME.img
qemu-img resize $NAME.img +100G
sudo virt-install --name $NAME --virt-type kvm --os-variant ubuntu22.04 --memory 16000 --vcpus 2 --network bridge=kvm_br,model=virtio --disk $NAME.img,device=disk,bus=virtio --disk $NAME-init.img,device=cdrom --graphics none --import
```