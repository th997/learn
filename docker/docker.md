# docker

## install
sudo apt-get update

sudo apt-get install -y docker.io

// wget -qO- https://get.docker.com/ | sh

sudo service docker start

sudo docker version

sudo docker info

## add hub
https://cr.console.aliyun.com

```
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://qlxp99sl.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

## add permissions
sudo groupadd docker

sudo gpasswd -a ${USRE} docker

sudo service docker restart

## image
docker search spark

docker pull sequenceiq/spark

docker images

docker history --no-trunc  mysql:5.7

docker rmi 42118e3df429

docker rmi $(docker images -q)

docker save mynewimage > ~/mynewimage.tar

docker load < ~/mynewimage.tar


## container
docker run -itd --name ubuntu16 ubuntu:16.04

docker logs ubuntu16

### attach a container
docker attach ubuntu16

### exit terminal
ctrl+p , ctrl+q

### modify a container
docker commit -a 'th' -m 'message' ubuntu16 th/base

### show running container
docker ps

docker ps -a

docker start/stop/restart ubuntu16

docker rm 421df42339

docker rm $(docker ps -a -q)

## build image
sudo vi Dockerfile
```
FROM ubuntu:16.04
MAINTAINER th "th9976@gamil.com"
RUN echo "deb http://mirrors.163.com/ubuntu/ xenial main restricted universe multiverse" > /etc/apt/sources.list
RUN echo "deb http://mirrors.163.com/ubuntu/ xenial-security main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.163.com/ubuntu/ xenial-updates main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.163.com/ubuntu/ xenial-proposed main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.163.com/ubuntu/ xenial-backports main restricted universe multiverse" >> /etc/apt/sources.list
RUN apt-get update
RUN apt-get install openjdk-8-jdk -y
EXPOSE 80
```
docker build -t='th/java8' .

## data
docker run -it -v ~/dir1:/data/dir th/base1 /bin/bash

docker cp <containerId>:/file/path/within/container /host/path/target

docker volume ls

docker volume inspect th9976_db_data


## clean up data
docker rm `docker ps -a | grep Exited | awk '{print $1}'`  

docker rmi  `docker images | grep '<none>' | awk '{print $3}'` 

docker volume prune

## logs
docker logs -f -t --tail 100 container_name

## update 
docker update --restart=always xxx

## help
docker xxx --help

## add proxy
```
sudo mkdir -p /etc/systemd/system/docker.service.d
sudo vim /etc/systemd/system/docker.service.d/http-proxy.conf

[Service]
Environment="HTTP_PROXY=http://10.10.10.106:1080"
Environment="HTTPS_PROXY=http://10.10.10.106:1080"

sudo systemctl daemon-reload
sudo systemctl restart docker

see detail:  https://docs.docker.com/config/daemon/systemd/

```

## remote client
systemctl status docker

cp /lib/systemd/system/docker.service /lib/systemd/system/docker.service.bak

vim /lib/systemd/system/docker.service

delete "-H fd://"

vim /etc/docker/daemon.json 
```json
{
"hosts":[
    "fd://",
    "tcp://0.0.0.0:2375"
]
}
```
sudo systemctl daemon-reload

sudo systemctl restart docker

sudo systemctl status docker

in client:

export DOCKER_HOST=tcp://10.10.10.106:2375

## networking not work ...
ifconfig docker0 down

brctl delbr docker0

reboot now 

## docker 启动hung问题处理
https://blog.51cto.com/u_15162069/2873732
```
vim /etc/sysctl.conf
fs.pipe-user-pages-soft=65536
sysctl -p

ps aux|grep "runc \in"
cat /proc/进程id/fd/tab获取最大一个值
如：
cat /proc/420886/fd/8
```

## docker tools 
docker run -it --rm -v $PWD:/data infrabuilder/fio

docker run -it --rm -v $PWD:/data mysql:5.7.33 bash

docker run -it --rm -v $PWD:/data busybox

docker run -it --name python python

docker run -it --rm  mycli


