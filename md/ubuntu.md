## root 用户
```
sudo passwd root
...
su root
...
```

## root 权限
sudo su -

## apt proxy
sudo vim /etc/apt/apt.conf

Acquire::https::proxy "http://10.10.10.106:1080/";

Acquire::http::proxy "http://10.10.10.106:1080/";

## 更新源
sudo cp /etc/apt/sources.list /etc/apt/sources.list.backup

sudo vi /etc/apt/sources.list

18.04
``` 
deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse
# deb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse
# deb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse
# deb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse
# deb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse
deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse
# deb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse

```
 

## oracle java
```
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```

## 网络设置
```
1. vim /etc/network/interfaces
#localhost
auto lo
iface lo inet loopback
#eth0
auto eth0
#iface eth0 inet dhcp
iface eth0 inet static
address 192.168.1.131
netmask 255.255.255.0
gateway 192.168.1.1
#dns
dns-nameservers 223.5.5.5 8.8.8.8
2.
sudo ifdown eth0 && ifup eth0
或者 sudo /etc/init.d/networking restart

```

## nfs server
```
apt-get update
apt-get install nfs-server
vim /etc/exports
添加： /mnt/soft *(rw,sync,no_root_squash,no_subtree_check)
#/etc/init.d/rpcbind restart
/etc/init.d/nfs-kernel-server restart
systemctl enable nfs-kernel-server.service
```
## nfs client
```
apt-get update
apt-get install nfs-common
vim /etc/fstab
h1:/root/nfs      /mnt/nfs              nfs                defaults    0    0
mount -t nfs h1:/mnt/soft /mnt/soft
```

## windows 共享
```
sudo mount -t cifs //10.1.15.25/code /home/th/e/code -o username=administrator,password=134,uid=th,gid=th
```

## ssh 远程登录
rm /etc/ssh/ssh_host_*

dpkg-reconfigure openssh-server

## root 登陆
```
vim /etc/ssh/sshd_config
PermitRootLogin yes
service ssh restart
```

### 开机启动项目查看
sudo rcconf

## 开机小键盘
```
sudo -i
su gdm -s /bin/bash
gsettings set org.gnome.settings-daemon.peripherals.keyboard numlock-state 'on'
```

## 1804 点击最小化
gsettings set org.gnome.shell.extensions.dash-to-dock click-action 'minimize'

## dock
```
gsettings  range  org.gnome.shell.extensions.dash-to-dock transparency-mode

gsettings set org.gnome.shell.extensions.dash-to-dock extend-height false
gsettings set org.gnome.shell.extensions.dash-to-dock dock-position BOTTOM
gsettings set org.gnome.shell.extensions.dash-to-dock transparency-mode FIXED
gsettings set org.gnome.shell.extensions.dash-to-dock dash-max-icon-size 28
gsettings set org.gnome.shell.extensions.dash-to-dock unity-backlit-items true
```

## 输入法
https://blog.csdn.net/lupengCSDN/article/details/80279177

# windows远程桌面
https://www.cnblogs.com/pipci/p/9952749.html
sudo apt install rdesktop
rdesktop -u th -p *** -g 1024*720 10.10.10.169 -f

## windows远程桌面
remmina

## vncserver
```
https://www.realvnc.com/download/file/vnc.files/VNC-Server-6.4.1-Linux-x64.deb
https://blog.51cto.com/dgd2010/711213
sudo vnclicense -add ANN2U-FM59S-DAGV4-4TK96-BDTKA
vncserver
```

## snap
```
sudo systemctl edit snapd.service
[Service]
Environment=http_proxy=http://localhost:1081
Environment=https_proxy=http://localhost:1081
ctrl+x
enter 
sudo systemctl daemon-reload
sudo systemctl restart snapd.service
```

#＃ tweak配置
```
sudo apt install gnome-tweak-tool 
sudo apt install gnome-shell-extension-dash-to-panel
logout 
```

## wine 
```
https://tecadmin.net/install-wine-on-ubuntu/

sudo dpkg --add-architecture i386
wget -qO - https://dl.winehq.org/wine-builds/winehq.key | sudo apt-key add -

sudo apt-add-repository 'deb https://dl.winehq.org/wine-builds/ubuntu/ bionic main'

sudo apt-get update
sudo apt-get install --install-recommends winehq-stable

sudo apt-get install aptitude
sudo aptitude install winehq-stable

wine putty.exe

```

## wine 
```
sudo apt-get remove --purge wine
rm -r ~/.wine
sudo apt-get autoremove
rm -r ~/.local/share/applications
rm -r ~/.config/menus/applications-merged/wine*
```

## 截屏
```
gnome-screenshot -h
截屏选区到剪贴板， 可定义快捷键 
gnome-screenshot -ac 
```

## 日志清理
journalctl --vacuum-time=2d

journalctl --vacuum-size=500M

## 删除孤立的包
deborphan | xargs sudo apt-get purge -y

## apt 指定代理
sudo apt -o Acquire::http::proxy="http://10.10.10.106:1080" upgrade

## swap优化
vim /etc/sysctl.conf

vm.swappiness = 10

## U盘格式化
fdisk -l
mkfs.exfat -n udisk /dev/sdd1  

## 分区
```
fdisk /dev/sdc
帮助　ｍ
显示分区表 p
创建gpt分区表 g
新增分区 n
写入数据 w

gdisk 类似
```

## syslinux
efibootmgr -c -d /dev/sdc -p 1 -l /EFI/syslinux/syslinux.efi -L "syslinux"

https://lockless.github.io/2018/05/13/UEFI-tool/ 

https://www.pendrivelinux.com/multiboot-create-a-multiboot-usb-from-linux/


## ufw 
```
sudo ufw enable/reload
sudo ufw default allow/deny
sudo ufw allow 22/tcp
sudo ufw limit 22/tcp 
sudo ufw allow log 22/tcp
sudo ufw status verbose
sudo ufw deny from x.x.x.x/24
sudo ufw deny smtp 
sudo ufw delete allow smtp
sudo ufw allow proto udp 192.168.0.1 port 53 to 192.168.0.2 port 53

iptables -S
sudo vim /etc/ufw/user.rules

```
## iptables
```
iptables -I INPUT -s 141.98.10.137/16 -j DROP
iptables -I INPUT -s 185.36.81.23/16 -j DROP
iptables -I INPUT -s 45.125.65.35/16 -j DROP
iptables -I INPUT -s 46.38.145.164/16 -j DROP
service iptables save
```

## systemctl 
systemctl enable httpd.service
systemctl disable httpd.service
systemctl start httpd.service
systemctl stop httpd.service
systemctl status httpd.service
systemctl list-units --type=service

## ubutun 18.04 添加开机启动
```
sudo vim /lib/systemd/system/rc.local.service
添加
[Install]
WantedBy=multi-user.target
Alias=rc-local.service

sudo vim /etc/rc.local
添加
#!/bin/bash
xxx.sh

chmod +x /etc/rc.local
chmod +x xxx.sh
ln -s /lib/systemd/system/rc.local.service /etc/systemd/system/
systemctl enable rc-local

systemctl start rc-local.service
systemctl status rc-local.service
reboot 
```

## 无人值守更新(18.04)
sudo apt update

sudo apt install unattended-upgrades

## 显卡设置
nvidia-smi
nvidia-settings

## 网络配置
ip a

sudo vim /etc/netplan/50-cloud-init.yaml
```
network:
 version: 2
 renderer: networkd
 ethernets:
   enp0s5:
     dhcp4: yes
     dhcp6: yes
```
sudo netplan apply

https://www.hi-linux.com/posts/49513.html

## snap 包
snap list
snap remove xx

## apt 
```
apt-get --purge remove packagename卸载一个已安装的软件包（删除配置文件）
dpkg --force-all --purge packagename 有些软件很难卸载，而且还阻止了别的软件的应用，就可以用这个，不过有点冒险。
apt-get autoclean apt会把已装或已卸的软件都备份在硬盘上，所以如果需要空间的话，可以让这个命令来删除你已经删掉的软件
apt-get clean这个命令会把安装的软件的备份也删除，不过这样不会影响软件的使用的。
apt-get dist-upgrade将系统升级到新版本
apt-cache search string在软件包列表中搜索字符串
dpkg -l package-name-pattern列出所有与模式相匹配的软件包。如果您不知道软件包的全名，您可以使用“package-name-pattern”。
aptitude 详细查看已安装或可用的软件包。与apt-get类似，aptitude可以通过命令行方式调用，但仅限于某些命令——最常见的有安装和卸载命令。由于aptitude比apt-get了解更多信息，可以说它更适合用来进行安装和卸载。
apt-cache showpkg pkgs显示软件包信息。
apt-cache dumpavail打印可用软件包列表。
apt-cache show pkgs显示软件包记录，类似于dpkg –print-avail。
apt-cache pkgnames打印软件包列表中所有软件包的名称。
dpkg -S file这个文件属于哪个已安装软件包。
dpkg -L package列出软件包中的所有文件。
```

## dpkg
dpkg -l  列表
dpkg -r xxxx  删除
dpkg -P xxxx  删除包括配置文件
dpkg -i xxxx  安装

## snap 
snap list

## dns
vim /etc/systemd/resolved.conf 
systemctl restart systemd-resolved.service
systemd-resolve --status

# 关闭swap
关闭
sudo swapoff -a

保存 注释 swap行
sudo vim /etc/fstab

重启后 删除 /swap.img


# 防止暴力破解
sudo apt install fail2ban
vim /etc/fail2ban/jail.local
```
[DEFAULT]
#ignoreip = 192.168.1.2
bantime = 1800
maxretry = 5
findtime = 600
backend = polling
[ssh-iptables]
enabled = true
filter = sshd
action = iptables[name=SSH, port=ssh, protocol=tcp]
sendmail-whois[name=SSH, dest=th9976@gmail.com, sender=fail2ban@email.com]
```
sudo systemctl enable fail2ban
sudo systemctl restart fail2ban

# 包卸载
apt list | grep installed | grep -v "python\|node\|lib\|ubuntu\|gnome\|fonts\|linux-headers\|cuda"
apt remove  xxx
apt autoremove

# 查看启动时间
systemd-analyze blame
sudo systemd-analyze plot > boot.svg

# 网络查询
networkctl list
brctl show

# samba
chmod -R o+r+w /data/samba
chmod o+x+w /data/samba
docker run -itd --name samba --restart always -p 139:139 -p 445:445 -p 137-138:137-138/udp -v /data/samba:/mount dperson/samba -w "WORKGROUP" -s "share;/mount/;yes;no;no;all;none" -u "test;123456" -S

# samba
apt install samba
vim /etc/samba/smb.conf
``` conf
[share]
    path = /data/samba/
    public = yes
    browseable = yes
    public = yes
    read only = no
    valid users = test
    force user = nobody
    force group = nogroup
    available = yes
```
smbpasswd -a test
systemctl restart nmbd