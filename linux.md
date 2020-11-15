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

rsync --progress -ar ~/Downloads /Volumes/f/download

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
* soft nofile 365535
* hard nofile 365535
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
time dd if=/dev/zero bs=1024 count=1000000 of=1Gb.file
time dd of=1Gb.file if=/dev/zero bs=1024 count=1000000
```

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
docker run -it --rm --name certbot \
            -v "/etc/letsencrypt:/etc/letsencrypt" \
            certbot/certbot certonly \
            --manual --agree-tos --manual-public-ip-logging-ok --preferred-challenges dns-01 \
            --server https://acme-v02.api.letsencrypt.org/directory \
            -d tdhere.com \
            -d "*.tdhere.com"
add dns txt ... apt-get -y install dnsutils ... nslookup -q=txt  _acme-challenge.tdhere.com
...
certbot-auto renew
ls /etc/letsencrypt/live/tdhere.com/fullchain.pem
ls /etc/letsencrypt/live/tdhere.com/privkey.pem
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