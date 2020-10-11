#FROM th9976/hadoop:1.0
FROM th9976/spark:1.0
MAINTAINER th9976 <th9976@gmail.com>

# env
ENV HIVE_HOME=/usr/local/hive
ENV ZEPPELIN_HOME=/usr/local/zeppelin
ENV PATH=$PATH:/usr/local/hive/bin
ENV PATH=$PATH:/usr/local/zeppelin/bin

# add file
ADD apache-hive-3.1.1-bin.tar.gz /usr/local/
ADD zeppelin-0.8.1-bin-all.tgz /usr/local/
RUN ln -s /usr/local/apache-hive-3.1.1-bin $HIVE_HOME && \
    ln -s /usr/local/zeppelin-0.8.1-bin-all $ZEPPELIN_HOME && \
    cp $HIVE_HOME/conf/hive-env.sh.template $HIVE_HOME/conf/hive-env.sh && \
    echo "export HADOOP_HOME=/usr/local/hadoop" >> $HIVE_HOME/conf/hive-env.sh  && \
    echo "export HIVE_CONF_DIR=/usr/local/hive/conf" >> $HIVE_HOME/conf/hive-env.sh  && \
    echo "export HIVE_AUX_JARS_PATH=/usr/local/hive/lib" >> $HIVE_HOME/conf/hive-env.sh && \
    cp -rf $SPARK_HOME/jars/scala-library*.jar  $HIVE_HOME/lib/ && \
    cp -rf $SPARK_HOME/jars/spark-core*.jar  $HIVE_HOME/lib/ && \
    cp -rf $SPARK_HOME/jars/spark-network-common*.jar  $HIVE_HOME/lib/ 

COPY mysql-connector-java*.jar /usr/local/hive/lib/
COPY zeppelin-env.sh /usr/local/zeppelin/conf
COPY zeppelin-site.xml /usr/local/zeppelin/conf
COPY hive-site.xml /usr/local/hive/conf/
RUN ln -s /usr/local/hive/conf/hive-site.xml /usr/local/spark/conf/hive-site.xml
COPY entrypoint.sh $HIVE_HOME/entrypoint.sh
COPY start-hive.sh $HIVE_HOME/start-hive.sh
RUN chmod a+x $HIVE_HOME/*.sh

# pre ...
WORKDIR $HIVE_HOME
EXPOSE 4040
EXPOSE 8080
EXPOSE 8081
EXPOSE 7077

# start 
ENTRYPOINT ["/usr/local/hive/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

