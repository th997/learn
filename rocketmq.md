## rocketmq
http://blog.csdn.net/zhu_tianwei/article/details/40949523

## 安装jdk

## 单机
```
export NAMESRV_ADDR="10.168.153.26:9876"
sh ./bin/mqnamesrv  &
sh ./bin/mqbroker -n "10.168.153.26:9876" &
```
## 双主
```
h1
export NAMESRV_ADDR="10.10.10.201:9876;10.10.10.202:9876"
sh ./bin/mqnamesrv  &
sh ./bin/mqbroker -c ./conf/2m-noslave/broker-a.properties &
h2
export NAMESRV_ADDR="10.10.10.201:9876;10.10.10.202:9876"
sh ./bin/mqnamesrv  &
sh ./bin/mqbroker -n "10.10.10.201:9876;10.10.10.202:9876" -c ./conf/2m-noslave/broker-b.properties &
```
