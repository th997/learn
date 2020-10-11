
FROM ubuntu:16.04

MAINTAINER th <th9976@gmail.com>

RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" > /etc/apt/sources.list && \
	echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse" >> /etc/apt/sources.list && \
	echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list && \
	echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list && \
	echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list

RUN apt-get update
RUN apt-get install golang -y
RUN apt-get install openjdk-8-jdk -y 
RUN apt-get install git -y
RUN apt-get install vim -y
RUN apt-get install openssh-server -y
RUN apt-get install inetutils-ping -y
RUN apt-get install net-tools -y
RUN apt-get install maven -y
RUN apt-get install psmisc -y
RUN apt-get install python2.7 python-pip -y
RUN apt-get install supervisor -y
RUN apt-get install portmap -y
RUN apt-get install nfs-kernel-server nfs-common -y

# timezone
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get install tzdata -y 
RUN ln -fs /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN dpkg-reconfigure -f noninteractive tzdata
RUN apt-get install ntp -y
