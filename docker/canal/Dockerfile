FROM openjdk:8
MAINTAINER th9976 <th9976@gmail.com>

# ssh
RUN apt-get update && apt-get install -y openssh-server && \
ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

# env
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV CANAL_HOME=/usr/local/canal

# data dir
RUN mkdir -p /usr/local/canal 

# add file
ADD canal.deployer-1.1.2.tar /usr/local/canal 
RUN ln -s /usr/local/hadoop-2.9.2 $HADOOP_HOME && \
echo "JAVA_HOME=$JAVA_HOME" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh
COPY /config/*.xml $HADOOP_HOME/etc/hadoop/
COPY ssh_config  /root/.ssh/config
COPY entrypoint.sh $HADOOP_HOME/entrypoint.sh
RUN chmod a+x $HADOOP_HOME/entrypoint.sh
RUN chmod 600 ~/.ssh/config

# pre ...
WORKDIR $HADOOP_HOME
RUN /usr/local/hadoop/bin/hdfs namenode -format
# namenode
EXPOSE 50070
# datanode
EXPOSE 50075
# nodemanager
EXPOSE 8042
# resourcemanager
EXPOSE 8088
# hdfs
EXPOSE 9000

# start 
ENTRYPOINT ["/usr/local/hadoop/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

