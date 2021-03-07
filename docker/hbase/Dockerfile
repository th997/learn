FROM th9976/hadoop

# env
ENV HBASE_HOME=/usr/local/hbase
ENV HBASE_CONF_DIR=/usr/local/hbase/conf
ENV PATH=$PATH:/usr/local/hbase/bin

# add file
ADD hbase-2.4.1-bin.tar.gz  /usr/local/
RUN ln -s /usr/local/hbase-2.4.1 $HBASE_HOME && \
    echo "export JAVA_HOME=$JAVA_HOME" >> $HBASE_HOME/conf/hbase-env.sh && \
    echo "export HADOOP_HOME=$HADOOP_HOME" >> $HBASE_HOME/conf/hbase-env.sh && \
    echo "export HBASE_MANAGES_ZK=false" >> $HBASE_HOME/conf/hbase-env.sh && \
    echo "export PATH=$PATH" >> $HBASE_HOME/conf/hbase-env.sh && \
    # cp 	$HBASE_HOME/lib/client-facing-thirdparty/htrace* $HBASE_HOME/lib/ \
    echo ""
COPY conf/* $HBASE_HOME/conf/
COPY entrypoint.sh $HBASE_HOME/entrypoint.sh
RUN chmod a+x $HBASE_HOME/entrypoint.sh

# pre ...
WORKDIR $HBASE_HOME
EXPOSE 16010

# start 
ENTRYPOINT ["/usr/local/hbase/entrypoint.sh"]
CMD [ "sh", "-c", "bash"]

