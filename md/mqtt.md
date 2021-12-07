# mqtt

## 协议
### 固定报头
1字节(8位), 7-4表示包类型(一共15种,0保留), 3-0位表示标志位
```
    CONNECT(1),
    CONNACK(2),
    PUBLISH(3),
    PUBACK(4),
    PUBREC(5),
    PUBREL(6),
    PUBCOMP(7),
    SUBSCRIBE(8),
    SUBACK(9),
    UNSUBSCRIBE(10),
    UNSUBACK(11),
    PINGREQ(12),
    PINGRESP(13),
    DISCONNECT(14),
    AUTH(15);
```
### 长度
最多4字节,每字节的最高位表示是否继续,所有报文最大长度为 2^(32-4)-1=268435455=256M
### 报文主体
