## 命令帮助
man xxx

## 用户和群组管理
groupadd mysql

useradd -g mysql mysql

passwd mysql

## 分区表控制 -l 显示当前磁盘的情况 -lu 显示SCSI硬盘的分区情况...
fdisk  	

## 语言环境修改, 删除所有内容,显示英文
vi /etc/sysconfig/i18n

## 查看某个进程的进程树
pstree -p|grep mongod 

## 同步数据
sync

## 备份
rsync -a bin /usr/local/mongodb	 

rsync -aP ~/Downloads /Volumes/f/download

rsync -avzuP 增量同步 合并目录

## 查看当前文件夹下个文件大小等信息
ls -hl

## 显示CPU情况
sar 1 1 |grep -E "idle|Average" 

## 显示硬盘信息
df -m 

## 显示内存信息
free -m 

cat /proc/meminfo 

## 显示磁盘IO信息
iostat -d -k 1 5 

## 逻辑CPU个数
cat /proc/cpuinfo | grep "processor" | wc -l 

## 物理CPU个数
cat /proc/cpuinfo | grep "physical id" | sort | uniq | wc -l 

## 每个物理CPU中Core的个数
cat /proc/cpuinfo | grep "cpu cores" | wc -l

## 查看CPU信息（型号）
cat /proc/cpuinfo | grep name | cut -f2 -d: | uniq -c 

## tail查看文本文件末尾
tail -f catalina.out -n 200

tailf catalina.out -n 200

## 软链接
ln -s  a b

## 抓包命令
tcpdump -s 0 -w a.cap udp port 1352

## 抓包打印
tcpdump -X host 47.244.231.126 and port 22

https://www.jianshu.com/p/a62ed1bb5b20

## iptables 
```
iptables -L  #查看已有规则
iptables -F  #清除预设表filter中的所有规则链的规则,相当于没有开防火墙了
iptables -X  #清除预设表filter中使用者自定链中的规则
iptables -A INPUT -p tcp --dport 22 -j ACCEPT #允许22端口 dport 目标端口
iptables -A OUTPUT -p tcp --sport 22 -j ACCEPT #允许22端口 sport 源端口
iptables -P INPUT DROP  #只允许规定的包通过
iptables -P OUTPUT DROP  #
iptables -P FORWARD DROP  #
iptables -A INPUT -p tcp --dport 80 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 80 -j ACCEPT
iptables -A OUTPUT -p icmp -j ACCEPT  #允许ping
iptables -A INPUT -p icmp -j ACCEPT  #
iptables -A FORWARD -f -m limit --limit 100/s --limit-burst 100 -j ACCEPT

#处理IP碎片数量,防止攻击,允许每秒100个
/etc/rc.d/init.d/iptables save  #保存设置
iptables -F
service iptables restart  #重新启动
service iptables save

#开放特定端口
iptables -A INPUT -p tcp --dport 27017 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 27017 -j ACCEPT
/etc/rc.d/init.d/iptables save
service iptables restart

#只允许特定ip访问某端口
iptables -I INPUT -p TCP --dport 5672 -j DROP
iptables -I INPUT -s 192.168.8.4 -p TCP --dport 5672 -j ACCEPT
iptables -I INPUT -s 192.168.8.164 -p TCP --dport 5672 -j ACCEPT
iptables -I INPUT -s 192.168.8.56 -p TCP --dport 5672 -j ACCEPT
iptables -I INPUT -s 127.0.0.1 -p TCP --dport 5672 -j ACCEPT
service iptables save
service iptables restart

```

## 配置IP
```
vi /etc/sysconfig/network-scripts/ifcfg-eth0
DEVICE=eth0 //指出设备名称
ONBOOT=yes//是否启动应用
BOOTPROTO=static //启动类型 静态 （默认dhcp）
IPADDR=192.168.1.115 //IP地址
NETMASK=255.255.255.0//子网掩码
GATEWAY=192.168.1.1 //网关
service network restart
```
 
## 配置DNS
```
vi /etc/resolv.conf
nameserver 8.8.8.8
nameserver 8.8.4.4
```

## 统计端口连接数数
netstat -nat | grep -i "8000" | wc -l

## 查看占用8000端口的进程
lsof -i:8000

## 文件类型:
```
- 普通文件
d 目录
l 软连接
b 块特殊文件,一般指块设备,如磁盘
c 字特殊文件,一般指字符设备,如键盘
p 命名的管道文件,一般用于进程之间传递数据
s socket,套接字
```

## 查看开机启动项
chkconfig --list

## 压缩
tar cfz dir.tar.gz dirname

## 查看可打开文件数
ulimit -n 

## 重启
reboot now

## 环境变量
```
vi /etc/profile

JAVA_HOME=/usr/java/jdk1.7.0_75
PATH=$JAVA_HOME/bin:$PATH
export PATH JAVA_HOME

source /etc/profile
```

## java 监控命令
```
jstat -gcutil 28418
jstack -l 5780
jmap -heap 28418
jmap -histo:live 28418
jmap -dump:format=b,file=/temp/jmap.bin 28418
jhat /temp/jmap.bin
http://120.26.65.150:7000/
jmap -dump:format=b,file=heap.bin 912

ps -p 1426 -o vsz,rss
pmap -x 6577
```

## 批量压缩
find . -name "*.txt.*" -type f -exec tar -zcvf {}.tar.gz {} \;

## 批量压缩并且删除源文件
find . -name "*.txt.*" -type f -exec tar -zcvf {}.tar.gz {} --remove-files \;

## find 查找 执行命令
find ./ -name "*.url" |xargs -t -i rm -f {}

find ./ -name "*.rar" |xargs -t -i sh -c 'unrar x "{}" "$(dirname {})"'

## 查找指定文件,指定内容,打印文件及所在行号
find /mnt -name appConfig*  -exec  grep -n  "url_dao" ./ {} \;

##   open files 查看
lsof -n|awk '{print $2}'|sort|uniq -c |sort -nr|more

ps -ef | grep pid

lsof -n | grep 27650 | wc -l

## open files 修改
```
vi /etc/security/limits.conf

# End of file
* soft nofile 1000000
* hard nofile 1000000
```

## ping 127.0.0.1 不通
ifconfig lo 127.0.0.1 up

## 同步时间
ntpdate time.nist.gov

ntpdate cn.pool.ntp.org

## 网卡查看
ethtool eth1

#查看文件占用
fuser ab.txt

## fio磁盘测试
```
wget http://brick.kernel.dk/snaps/fio-2.0.14.tar.gz
./configure
make
make install
yum install libaio-devel
fio -direct=1  -iodepth=64  -rw=randwrite  -ioengine=libaio  -bs=16k  -size=1G  -numjobs=1  -runtime=1000  -group_reporting  -name=/mnt/testfile
```

## time 磁盘测试
```
time dd if=/dev/zero bs=1M count=1000 of=1Gb.file
time dd if=1Gb.file bs=1M count=1000 of=/dev/zero
```

## 磁盘io测试
docker run -it --rm -v $PWD:/data infrabuilder/fio

or wget https://raw.githubusercontent.com/InfraBuilder/docker-fio/main/benchmark.sh

## 文件监控
```
yum install inotify-tools
(inotifywait -mr --timefmt %Y%m%d%H%M%S --format %T%w%f%e -e create,delete,modify -o /mnt/inotify.txt /mnt/apache-tomcat-8.0.23/webapps/manager &)
```

## 查看文件夹大小
du -h --max-depth=1

## TCP mtu
sysctl -w net.ipv4.tcp_mtu_probing=1

## 网速
nload
iptraf

## 查看文件编码
enca -L zh_CN filename
enca -L zh_CN -x UTF-8 filename

## scp 比如要把当前一个文件copy到远程另外一台主机上，可以如下命令。
scp /home/daisy/full.tar.gz root@172.19.2.75:/home/root

## 时间同步
```
/usr/sbin/ntpdate us.pool.ntp.org >> /var/log/crontab.log 2>&1
crontab  -e
*/30 * * * * /usr/sbin/ntpdate us.pool.ntp.org >> /var/log/crontab.log 2>&1
```

## 复制覆盖
\cp -rf

## 域名ssl
```
docker run -it --rm  --name certbot \
            -v "/etc/letsencrypt:/etc/letsencrypt" \
            certbot/certbot certonly \
            --manual --agree-tos --manual-public-ip-logging-ok --preferred-challenges dns-01 \
            --server https://acme-v02.api.letsencrypt.org/directory \
            -d tdhere.com \
            -d "*.tdhere.com"
add dns txt ... apt-get -y install dnsutils ... nslookup -q=txt  _acme-challenge.tdhere.com
...
certbot-auto renew
ls /etc/letsencrypt/live/test.com/fullchain.pem
ls /etc/letsencrypt/live/test.com/privkey.pem
```

## 目录授权给th
sudo chown th /tmp

sudo chown th:groupname /tmp

## 设置默认网关 (网卡wlan0,网关192.168.43.1)
sudo route add default dev wlan0 metric 0  gw 192.168.43.1

## ssh 免密登陆
ssh-keygen
ssh-copy-id -i .ssh/id_rsa.pub user@xxx
// sudo chmod 755 /home/xx

## 监听端口
nc -l 3307 < response.hcy

## 分析ｃｐｕ占用
top
top -H -p 16759 (进程id)
strace -cp 16790 (线程id)

## 磁盘io分析
watch -n 1 -d "iostat -dkx | sort -r -k 14"
pidstat -d 1

## 查看进程上下文切换次数
vmstat 2 5
pidstat -wt -p 1222839 2 2

## 删除重复文件
rdfind -deleteduplicates true -ignoreempty false ~/Downloads

## 分区扩展
fdisk /dev/xxx # 调整分区参数 
e2fsck -f /dev/xxxn #检查分区信息
resize2fs /dev/xxxn #调整分区大小

## 模拟tcp监听
while true; do nc -l -p 8100 -q 1 < test.http; done

## 磁盘分区备份
partclone.ntfs -c -z 1024000000 -s /dev/xxx1 -x lz4 -o diskxxx.img.lz4 

## 磁盘分区还原
lz4 diskxxx.img.lz4 | partclone.ntfs -r -z 1024000000 -o /dev/xxx1

## 压缩加密 
7z a  -p -mhe=on xxx.7z xxx

## tar lz4
tar -I lz4 -cvf xxx.tar.lz4 xxx

## 查看磁盘信息
```
smartctl -a /dev/sda
hdparm -i /dev/sda
# 查看健康状态
smartctl -H /dev/sda
# 快速检测
smartctl -l selftest /dev/sda
# 查看错误信息
smartctl -l error /dev/sda
```

## cpu test
sysbench cpu run

## ssh proxy
vim ~/.ssh/config
```
Host *
    ProxyCommand nc -X 5 -x 127.0.0.1:1080 %h %p
```

## xrdp
```
sudo apt-get install xrdp
sudo adduser xrdp ssl-cert
sudo systemctl enable xrdp

如果黑屏，开头添加
sudo vim /etc/xrdp/startwm.sh
unset DBUS_SESSION_BUS_ADDRESS
unset XDG_RUNTIME_DIR

优化配置 
sudo vim /etc/xrdp/xrdp.ini
crypt_level=low

sudo vim /etc/xrdp/sesman.ini
MaxLoginRetry=6
```

## sshfs
```
sshfs -o allow_other,reconnect,follow_symlinks user@host:/ /mnt/tu
vim /etc/fstab
user@host:/ /mnt/tu fuse.sshfs _netdev,allow_other 0 0
```

## wifi
获取wifi列表
nmcli device wifi list
连接wifi
sudo nmcli device wifi connect xxx password "12345678"
共享热点(band a为指5G,channel 44为指定信道)
sudo nmcli device wifi hotspot con-name Hotspot ifname wlp0s20f3 band a channel 44 ssid tu password "12345678" 
显示连接
sudo nmcli connection show
删除热点
sudo nmcli connection delete Hotspot
查看当前信道
iwlist wlp0s20f3 channel
重启
sudo nmcli con down Hotspot
sudo nmcli con up Hotspot

## 私钥生成公钥
ssh-keygen -y -f id_rsa

## 防火墙关闭
sudo iptables -F
sudo iptables -X
sudo netfilter-persistent save