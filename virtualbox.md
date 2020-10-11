
## VirtualBox 虚拟机系统后台启动
```
 #"centos"为虚拟机名字
 VBoxManage startvm centos -type vrdp  #后台启动
 VBoxManage controlvm centos savestate #休眠
 VBoxManage controlvm centos poweroff  #关掉电源
 VBoxManage #帮助
```

## 更改UUID
```
VBoxManage internalcommands sethduuid "path of vdi or vmdx"
```

## U盘启动
```
sudo fdisk -l #查看设备信息，找到usb设备对应的设备文件，常见的可能是/dev/sdb，像/dev/sdb1这种是指向对应设备分区的文件。
ls -l /dev/sdb #查看设备文件的属性，主要是看权限方面的，一般是brw-rw---- root
sudo chmod o+rw /dev/sdc1 #增加others在该设备文件的读写权限，如果不这样设置，那么后面运行相应的命令都得使用sudo（以root身份执行）才有权限操作该设备文件
vboxmanage internalcommands createrawvmdk -filename ~/VMs/usb.vmdk -rawdisk /dev/sdc1 #将指定设备文件模拟成指定文件名的虚拟硬盘
```