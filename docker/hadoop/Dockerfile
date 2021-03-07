FROM openjdk:8

# ssh
RUN apt-get update && apt-get install -y openssh-server net-tools vim && \
ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

# env
ENV JAVA_HOME=/usr/local/openjdk-8/
ENV CLASSPATH=.:$JAVA_HOME/jre/lib:$JAVA_HOME/lib:$JAVA_HOME/lib/tools.jar
ENV HADOOP_HOME=/usr/local/hadoop 
ENV HADOOP_COMMON_HOME=$HADOOP_HOME
ENV HADOOP_HDFS_HOME=$HADOOP_HOME
ENV HADOOP_MAPRED_HOME=$HADOOP_HOME
ENV HADOOP_YARN_HOME=$HADOOP_HOME
ENV HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"
ENV HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin 
ENV HADOOP_LOG_DIR=/home/data/logs/hadoop
ENV USER=root
ENV HDFS_NAMENODE_USER=root
ENV HDFS_DATANODE_USER=root
ENV HDFS_SECONDARYNAMENODE_USER=root
ENV HDFS_JOURNALNODE_USER=root
ENV HDFS_ZKFC_USER=root
ENV YARN_NODEMANAGER_USER=root
ENV YARN_RESOURCEMANAGER_USER=root

# data dir
RUN mkdir -p /home/data/hdfs/namenode && \ 
    mkdir -p /home/data/hdfs/datanode && \
    mkdir -p $HADOOP_LOG_DIR 

# add file
ADD hadoop-3.2.2.tar.gz /usr/local/
RUN ln -s /usr/local/hadoop-3.2.2 $HADOOP_HOME && \
echo "JAVA_HOME=$JAVA_HOME" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh
COPY /config/*.xml $HADOOP_HOME/etc/hadoop/
COPY ssh_config  /root/.ssh/config
COPY entrypoint.sh $HADOOP_HOME/entrypoint.sh
RUN chmod a+x $HADOOP_HOME/entrypoint.sh
RUN chmod 600 ~/.ssh/config

# pre ...
WORKDIR $HADOOP_HOME

# start 
ENTRYPOINT ["/usr/local/hadoop/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

