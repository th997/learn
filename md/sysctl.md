## ubuntu 优化参数
``` conf
# 最大限度使用物理内存
vm.swappiness = 0
# 表示进程（例如一个worker进程）可能同时打开的最大句柄数，直接限制最大并发连接数
fs.file-max = 10240000
fs.nr_open =  10240000
# 允许包转发
net.ipv4.ip_forward = 1
net.ipv6.conf.all.forwarding = 1
# 三次握手建立阶段SYN请求队列的最大长度，默认是1024。设置大一些可以在繁忙时将来不及处理的请求放入队列，而不至于丢失客户端的请求 
net.ipv4.tcp_max_syn_backlog = 102400
#系统所能处理不属于任何进程的TCP sockets最大数量
net.ipv4.tcp_max_orphans = 131072
#系统最大保持TIME_WAIT状态的连接数量
net.ipv4.tcp_max_tw_buckets = 5000
# 避免大量TIME_WAIT,防范少量SYN攻击
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_fin_timeout = 30
# mtu探测
net.ipv4.tcp_mtu_probing = 1
# 端口分配范围
net.ipv4.ip_local_port_range = 1024 65535
# https://juejin.cn/post/6861560765200105486
net.ipv4.tcp_keepalive_time = 1800 # 最近一次数据包发送与第一次keep alive探测消息发送的事件间隔，用于确认TCP连接是否有效。
net.ipv4.tcp_keepalive_intvl = 20 # 在未获得探测消息响应时，发送探测消息的时间间隔。
net.ipv4.tcp_keepalive_probes = 5 # 判断TCP连接失效连续发送的探测消息个数，达到之后判定连接失效。
net.ipv4.tcp_abort_on_overflow=1 #如果系统当前因后台进程无法处理的新连接而溢出，则允许系统重置新连接
# tcp缓存,支持百万连接 (1G 4G 16G:1page=4k),(4k,4k,16M)..  https://www.cnblogs.com/51core/articles/13683820.html
net.ipv4.tcp_mem = 262144 2097152 4194304
net.ipv4.tcp_rmem = 4096 4096 16777216
net.ipv4.tcp_wmem = 4096 4096 16777216
# #内核级别参数 https://www.zybuluo.com/hadix/note/98389
net.core.somaxconn = 2048 #系统中每个端口监听队列的最大长度
net.core.rmem_default = 262144 
net.core.wmem_default = 262144 #内核默认读写缓存字节数
net.core.rmem_max = 16777216 #内核最大读写缓存字节数
net.core.wmem_max = 16777216 
net.core.netdev_max_backlog = 20000 # 在每个网络接口接收数据包的速率比内核处理这些包的速率快时，允许送到队列的数据包的最大数目。
# 链接数限制
net.netfilter.nf_conntrack_max=10000000
# bbr 5.4内核+ (lsmod | grep bbr)
net.core.default_qdisc=fq
net.ipv4.tcp_congestion_control=bbr
# 最大内存映射区域
vm.max_map_count=2000000
```

## server
``` conf
vm.swappiness = 0
net.ipv4.ip_forward = 1
net.ipv4.tcp_mtu_probing = 1
net.ipv4.tcp_mem = 262144 2097152 4194304
net.ipv4.tcp_rmem = 4096 4096 16777216
net.ipv4.tcp_wmem = 4096 4096 16777216

net.core.somaxconn = 2048 
net.core.rmem_default = 262144
net.core.wmem_default = 262144 
net.core.rmem_max = 16777216 
net.core.wmem_max = 16777216
net.core.netdev_max_backlog = 20000

# bbr 5.4+
net.core.default_qdisc=fq
net.ipv4.tcp_congestion_control=bbr
```

## 常用参数 需验证后使用
``` conf 
# aliyun
kernel.sysrq = 1
net.ipv4.neigh.default.gc_stale_time = 120
# see details in https://help.aliyun.com/knowledge_detail/39428.html
net.ipv4.conf.all.rp_filter = 0
net.ipv4.conf.default.rp_filter = 0
net.ipv4.conf.default.arp_announce = 2
net.ipv4.conf.lo.arp_announce = 2
net.ipv4.conf.all.arp_announce = 2
# see details in https://help.aliyun.com/knowledge_detail/41334.html
net.ipv4.tcp_max_tw_buckets = 5000
net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_max_syn_backlog = 1024
net.ipv4.tcp_synack_retries = 2
net.ipv6.conf.lo.disable_ipv6 = 1
net.ipv6.conf.all.disable_ipv6 = 1
net.ipv6.conf.default.disable_ipv6 = 1


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


fs.file max = 999999
#表示进程（例如一个worker进程）可能同时打开的最大句柄数，直接限制最大并发连接数
 
net.ipv4.tcp_tw_reuse = 1
#1代表允许将状态为TIME-WAIT状态的socket连接重新用于新的连接。对于服务器来说有意义，因为有大量的TIME-WAIT状态的连接
 
net.ipv4.tcp_keepalive_time = 600
#当keepalive启用时，TCP发送keepalive消息的频率。默认是2个小时。将其调小一些，可以更快的清除无用的连接
 
net.ipv4.tcp_fin_timeout = 30
#当服务器主动关闭链接时，socket保持FN-WAIT-2状态的最大时间
 
net.ipv4.tcp_max_tw_buckets = 5000
#允许TIME-WAIT套接字数量的最大值。超过些数字，TIME-WAIT套接字将立刻被清除同时打印警告信息。默认是180000，过多的TIME-WAIT套接字会使webserver变慢
 
net.ipv4.ip_local_port_range = 1024　　61000
#UDP和TCP连接中本地端口（不包括连接的远端）的取值范围
 
net.ipv4.tcp_rmem = 4096　　32768　　262142
net.ipv4.tcp_wmem = 4096　　32768　　262142
#TCP接收/发送缓存的最小值、默认值、最大值
 
net.core.netdev_max_backlog = 8096
#当网卡接收的数据包的速度大于内核处理的速度时，会有一个队列保存这些数据包。这个参数就是这个队列的最大值。
 
net.core.rmem_default = 262144
net.core.wmem_default = 262144
#内核套接字接收/发送缓存区的默认值
 
net.core.rmem_max = 2097152
net.core.wmem_max = 2097152
#内核套接字接收/发送缓存区的最大值
 
net.ipv4.tcp_syncookies = 1
#解决TCP的SYN***。与性能无关
 
net.ipv4.tcp_max_syn_backlog = 1024
#三次握手建立阶段SYN请求队列的最大长度，默认是1024。设置大一些可以在繁忙时将来不及处理的请求放入队列，而不至于丢失客户端的请求
```