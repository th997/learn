4层模型，从上往下：
应用层  http/ftp/...
传输层  tcp/udp
IP层
网络接口层


一个包67个字节=14+53
ip  53=20+33
tcp 33=32+1


SYN表示建立连接，
FIN表示关闭连接，
ACK表示响应，
PSH表示有 DATA数据传输，
RST表示连接重置。

大流量高并发:
net.ipv4.tcp_mtu_probing = 1