# tls

## tls 1.2 4次握手
* Client Hello: 发送支持的加密套件+随机数C
* Server Hello: 返回CA证书+加密套件+随机数S
* Client Check: 验证CA证书+发送密钥C(经过公钥加密),利用密钥C+随机数C+随机数S生成对称密钥,发送finished信号就绪
* Server Finish: 私钥解密密钥C,相同算法生成对称密钥,发送finished信号,可以开始通信

## tls 1.3 3次握手
* Client: 发送支持的加密套件+随机数C
* Server: 返回CA证书+加密套件+随机数S,finished
* Client: 验证CA证书+发送密钥C(经过公钥加密),利用密钥C+随机数C+随机数S生成对称密钥,发送finished信号就绪

https://blog.csdn.net/fred_lzy/article/details/106275423