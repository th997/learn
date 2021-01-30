# cdh

wget https://archive.cloudera.com/cm6/6.3.1/ubuntu1804/apt/archive.key
apt-key add archive.key

wget https://archive.cloudera.com/cm6/6.3.1/ubuntu1804/apt/cloudera-manager.list
mv cloudera-manager.list /etc/apt/sources.list.d/

apt update
apt install cloudera-manager-daemons cloudera-manager-agent cloudera-manager-server


## docker 
docker pull bluedata/cdh632multi:1.12

docker run -it --ip 172.20.100.110 --net test01 -h="cdh632" --name cdh632 -d --privileged=true  -p 28001:22 -p 28002:7180 -p 28080:8080 -p 28070:7070 -p 28081:8081 -p 28040:4040 -p 28057:50070 -p 28088:8088 -p 28010:16010 -p 28006:60010 -p 28100:10000 -p 28888:8888 1nj0zren.mirror.aliyuncs.com/bluedata/cdh632multi:1.0


docker run -it  -h="cdh632" --name cdh632 -d --privileged=true  -p 28001:22 -p 28002:7180 -p 28080:8080 -p 28070:7070 -p 28081:8081 -p 28040:4040 -p 28057:50070 -p 28088:8088 -p 28010:16010 -p 28006:60010 -p 28100:10000 -p 28888:8888 bluedata/cdh632multi:1.12


https://github.com/emirkorkmaz/cloudera-quickstart-docker-compose