# docker 
some exercises

# build 
```
// --no-cache  --build-arg http_proxy=http://10.10.10.106:1080 --build-arg https_proxy=http://10.10.10.106:1080
docker build -t='th9976/ubu1804:1.0' -f ubu1804/Dockerfile ./ubu1804 
docker build -t='th9976/ubu1604:1.0' -f ubu1604/Dockerfile ./ubu1604
docker build -t='th9976/docker:1.0' -f docker/Dockerfile ./docker

docker build -t='th9976/hadoop' -f hadoop/Dockerfile ./hadoop 
docker build -t='th9976/hbase' -f hbase/Dockerfile ./hbase
docker build -t='th9976/spark:1.0' -f spark/Dockerfile ./spark
docker build -t='th9976/kafka:1.0' -f kafka/Dockerfile ./kafka
docker build -t='th9976/hive:1.0' -f hive/Dockerfile ./hive
docker build -t='th9976/cdh:1.0' -f cdh/Dockerfile ./cdh

...
```

# run
```
docker run -it --rm --name test --hostname h1 --privileged=true th9976/ubu1804:1.0 /sbin/init
docker run -it --rm --name temp --hostname h1 -p7180:7180 th9976/cdh:1.0
docker run -it --rm --name temp --hostname h1 centos:6

docker run -it --rm --name temp --net=host redis
docker exec -it temp /bin/bash

docker run -it --name ubu -p25:25 ubuntu:18.04 

docker-compose -f mysql/mysql-compose.yml up
docker-compose -f redis/redis-compose.yml up
docker-compose -f redis/redis-cluster-compose.yml up
docker-compose -f zookeeper/zookeeper-compose.yml up
# docker run --name zookeeper -p 2181:2181 --restart always -d zookeeper
docker-compose -f wordpress/wordpress-compose.yml up

docker-compose -f hadoop/hadoop-compose.yml up
docker-compose -f spark/spark-compose.yml up
docker-compose -f hbase/hbase-compose.yml up
docker-compose -f kafka/kafka-compose.yml up
docker-compose -f hive/hive-compose.yml up

```

