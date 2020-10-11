FROM th9976/hadoop:1.0
MAINTAINER th9976 <th9976@gmail.com>

# env
ENV SPARK_HOME=/usr/local/spark
ENV PATH=$PATH:/usr/local/spark/bin

# add file
ADD spark-2.4.3-bin-hadoop2.7.tgz /usr/local/
RUN ln -s /usr/local/spark-2.4.3-bin-hadoop2.7 $SPARK_HOME && \
    cp $SPARK_HOME/conf/spark-env.sh.template $SPARK_HOME/conf/spark-env.sh && \
    echo "export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop" >> $SPARK_HOME/conf/spark-env.sh  && \
    echo "export SPARK_MASTER_PORT=7077" >> $SPARK_HOME/conf/spark-env.sh  && \
    echo "export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)" >> $SPARK_HOME/conf/spark-env.sh 
COPY entrypoint.sh $SPARK_HOME/entrypoint.sh
RUN chmod a+x $SPARK_HOME/entrypoint.sh

# pre ...
WORKDIR $SPARK_HOME
EXPOSE 4040
EXPOSE 8080
EXPOSE 8081
EXPOSE 7077

# start 
ENTRYPOINT ["/usr/local/spark/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

