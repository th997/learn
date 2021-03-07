FROM th9976/hadoop

# env
ENV FLINK_HOME=/usr/local/flink
ENV PATH=$PATH:/usr/local/flink/bin
ENV HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
ENV HADOOP_CLASSPATH=/usr/local/hadoop/share/hadoop/hdfs/lib

# add file
ADD flink-1.12.1-bin-scala_2.11.tgz  /usr/local/
RUN ln -s /usr/local/flink-1.12.1 $FLINK_HOME && \
    echo "hadoop1:8081" > $FLINK_HOME/conf/masters && \
    echo "hadoop2:8081" >> $FLINK_HOME/conf/masters && \
    echo "hadoop3:8081" >> $FLINK_HOME/conf/masters && \
    echo "hadoop1" > $FLINK_HOME/conf/workers && \
    echo "hadoop2" >> $FLINK_HOME/conf/workers && \
    echo "hadoop3" >> $FLINK_HOME/conf/workers && \
    echo ""
# https://repo.maven.apache.org/maven2/org/apache/flink/flink-shaded-hadoop-2-uber/2.7.5-7.0/flink-shaded-hadoop-2-uber-2.7.5-7.0.jar    
COPY flink-shaded-hadoop-2-uber-2.7.5-7.0.jar $FLINK_HOME/lib/
COPY conf/* $FLINK_HOME/conf/
COPY entrypoint.sh $FLINK_HOME/entrypoint.sh
RUN chmod a+x $FLINK_HOME/entrypoint.sh

# pre ...
WORKDIR $FLINK_HOME

# start 
ENTRYPOINT ["/usr/local/flink/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

