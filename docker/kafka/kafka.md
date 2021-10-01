# 测试
./kafka-topics.sh --create --topic test --replication-factor 1 --partitions 100 --zookeeper 172.16.16.21:2181,172.16.16.22:2181,172.16.16.23:2181  

./kafka-topics.sh --list -zookeeper 172.16.16.21:2181,172.16.16.22:2181,172.16.16.23:2181  

./kafka-producer-perf-test.sh --topic test --num-records 1000000 --record-size 1024  --throughput 1000000 --producer-props bootstrap.servers=172.16.16.31:9092,172.16.16.32:9092,172.16.16.33:9092

./kafka-consumer-perf-test.sh --bootstrap-server 172.16.16.31:9092,172.16.16.32:9092,172.16.16.33:9092 --topic test --fetch-size 1048576 --messages 1000000 --threads 1

## web ui kafka-manager
docker run -it --rm --name kfk-manager -p 59000:9000 --network mynetwork  \
-e ZK_HOSTS=172.16.16.21:2181,172.16.16.22:2181,172.16.16.23:2181 \
sheepkiller/kafka-manager

## web ui kafka-eagle

## 概念
* Cluster 集群
* Broker 节点，消息存储， 一个Cluster可以有多个Broker
* Topic 主题
* Producer 生产者
* Consumer 消费者
* ConsumerGroup 消费组
* Partition 分区
* Replication 副本

## 规则
* 每个topic可指定多个分区
* 每个topic可指定多个副本
* 每个消费者可同时消费多个分区
* 相同key的数据发送到相同分区

## rebalance
* consumer group与topic分区重新匹配
* coordinator:协调者,通常是分区leader所在的broker,与consumer存在心跳,超时检测,分区分配

影响
* 阻塞读写

何时产生
* 当topic分区发生变化
* 当consumer消费超时
* 当group成员个数发生变化
* 当group订阅的topic发生变化

## zookeeper
* broker 上下线
* 分区扩缩容,leader选举,副本状态

## 高性能
* 顺序读写磁盘
* 零拷贝
* 内存映射,ospage
* 批量处理

## 消息顺序
* 指定key
* 多分区
* 内存队列
* https://blog.csdn.net/qianshangding0708/article/details/103360193

## 重试机制
* 生产者: ack,多副本,重试次数
* 消费者: ack,幂等,重试队列,失败消息保存

## 可靠性
* https://blog.csdn.net/u013256816/article/details/71091774