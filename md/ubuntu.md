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

Acquire::https::proxy "http://127.0.0.1:1080/";

Acquire::http::proxy "http://127.0.0.1:1080/";

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

## 优化TCP
```
#禁用包过滤功能
net.ipv4.ip_forward = 0
#启用源路由核查功能
net.ipv4.conf.default.rp_filter = 1
#禁用所有IP源路由
net.ipv4.conf.default.accept_source_route = 0
#使用sysrq组合键是了解系统目前运行情况，为安全起见设为0关闭
kernel.sysrq = 0
#控制core文件的文件名是否添加pid作为扩展
kernel.core_uses_pid = 1
#开启SYN Cookies，当出现SYN等待队列溢出时，启用cookies来处理
net.ipv4.tcp_syncookies = 1
#每个消息队列的大小（单位：字节）限制
kernel.msgmnb = 65536
#整个系统最大消息队列数量限制
kernel.msgmax = 65536
#单个共享内存段的大小（单位：字节）限制，计算公式64G*1024*1024*1024(字节)
kernel.shmmax = 68719476736
#所有内存大小（单位：页，1页 = 4Kb），计算公式16G*1024*1024*1024/4KB(页)
kernel.shmall = 4294967296
#timewait的数量，默认是180000
net.ipv4.tcp_max_tw_buckets = 6000
#开启有选择的应答
net.ipv4.tcp_sack = 1
#支持更大的TCP窗口. 如果TCP窗口最大超过65535(64K), 必须设置该数值为1
net.ipv4.tcp_window_scaling = 1
#TCP读buffer
net.ipv4.tcp_rmem = 4096 131072 1048576
#TCP写buffer
net.ipv4.tcp_wmem = 4096 131072 1048576
#为TCP socket预留用于发送缓冲的内存默认值（单位：字节）
net.core.wmem_default = 8388608
#为TCP socket预留用于发送缓冲的内存最大值（单位：字节）
net.core.wmem_max = 16777216
#为TCP socket预留用于接收缓冲的内存默认值（单位：字节）
net.core.rmem_default = 8388608
#为TCP socket预留用于接收缓冲的内存最大值（单位：字节）
net.core.rmem_max = 16777216
#每个网络接口接收数据包的速率比内核处理这些包的速率快时，允许送到队列的数据包的最大数目
net.core.netdev_max_backlog = 262144
#web应用中listen函数的backlog默认会给我们内核参数的net.core.somaxconn限制到128，而nginx定义的NGX_LISTEN_BACKLOG默认为511，所以有必要调整这个值
net.core.somaxconn = 262144
#系统中最多有多少个TCP套接字不被关联到任何一个用户文件句柄上。这个限制仅仅是为了防止简单的DoS攻击，不能过分依靠它或者人为地减小这个值，更应该增加这个值(如果增加了内存之后)
net.ipv4.tcp_max_orphans = 3276800
#记录的那些尚未收到客户端确认信息的连接请求的最大值。对于有128M内存的系统而言，缺省值是1024，小内存的系统则是128
net.ipv4.tcp_max_syn_backlog = 262144
#时间戳可以避免序列号的卷绕。一个1Gbps的链路肯定会遇到以前用过的序列号。时间戳能够让内核接受这种“异常”的数据包。这里需要将其关掉
net.ipv4.tcp_timestamps = 0
#为了打开对端的连接，内核需要发送一个SYN并附带一个回应前面一个SYN的ACK。也就是所谓三次握手中的第二次握手。这个设置决定了内核放弃连接之前发送SYN+ACK包的数量
net.ipv4.tcp_synack_retries = 1
#在内核放弃建立连接之前发送SYN包的数量
net.ipv4.tcp_syn_retries = 1
#开启TCP连接中time_wait sockets的快速回收
net.ipv4.tcp_tw_recycle = 1
#开启TCP连接复用功能，允许将time_wait sockets重新用于新的TCP连接（主要针对time_wait连接）
net.ipv4.tcp_tw_reuse = 1
#1st低于此值,TCP没有内存压力,2nd进入内存压力阶段,3rdTCP拒绝分配socket(单位：内存页)
net.ipv4.tcp_mem = 94500000 915000000 927000000
#如果套接字由本端要求关闭，这个参数决定了它保持在FIN-WAIT-2状态的时间。对端可以出错并永远不关闭连接，甚至意外当机。缺省值是60 秒。2.2 内核的通常值是180秒，你可以按这个设置，但要记住的是，即使你的机器是一个轻载的WEB服务器，也有因为大量的死套接字而内存溢出的风险，FIN- WAIT-2的危险性比FIN-WAIT-1要小，因为它最多只能吃掉1.5K内存，但是它们的生存期长些。
net.ipv4.tcp_fin_timeout = 15
#表示当keepalive起用的时候，TCP发送keepalive消息的频度（单位：秒）
net.ipv4.tcp_keepalive_time = 30
#对外连接端口范围
net.ipv4.ip_local_port_range = 2048 65000
#表示文件句柄的最大数量
fs.file-max = 102400
# 压力测试用
net.ipv4.tcp_syncookies = 0
net.ipv4.tcp_max_syn_backlog=819200
net.ipv4.tcp_tw_recycle=1
net.ipv4.tcp_tw_reuse=1
net.ipv4.tcp_max_tw_buckets=65535

# 禁用ipv6
net.ipv6.conf.all.disable_ipv6=1
net.ipv6.conf.default.disable_ipv6=1
net.ipv6.conf.lo.disable_ipv6=1

# aliyun
vm.swappiness = 0
net.ipv4.neigh.default.gc_stale_time=120
# see details in https://help.aliyun.com/knowledge_detail/39428.html
net.ipv4.conf.all.rp_filter=0
net.ipv4.conf.default.rp_filter=0
net.ipv4.conf.default.arp_announce = 2
net.ipv4.conf.lo.arp_announce=2
net.ipv4.conf.all.arp_announce=2
# see details in https://help.aliyun.com/knowledge_detail/41334.html
net.ipv4.tcp_max_tw_buckets = 5000
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_max_syn_backlog = 1024
net.ipv4.tcp_synack_retries = 2
```
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

