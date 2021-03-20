# kvm 

## 安装
```
sudo apt install qemu qemu-kvm libvirt-bin bridge-utils virt-manager
sudo service libvirtd start
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
```
sudo vim /etc/network/interfaces

auto kvm_br
iface kvm_br inet dhcp
bridge_ports enp0s31f6
bridge_stp on
bridge_fd 0

sudo vim /etc/NetworkManager/NetworkManager.conf

managed=true

sudo /etc/init.d/networking restart

```

## 安装系统
```
virt-install --virt-type=kvm --name centos6 --ram 2048 --vcpus=2 --os-variant=centos6.0 --cdrom=/mnt/h/os/CentOS-6.3-x86_64-minimal.iso --network=bridge=kvm_br,model=virtio --graphics vnc --disk path=/home/th/vm/centos6.qcow2,format=qcow2,size=10

virt-install --virt-type=kvm --name ubu2004 --ram 2048 --vcpus=2 --os-variant=ubuntu18.04 --cdrom=/mnt/h/os/ubuntu-20.04-desktop-amd64.iso --network=bridge=kvm_br,model=virtio --graphics vnc --disk path=/home/th/vm/ubu2004.qcow2,format=qcow2,size=10

```
 

