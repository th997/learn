#!/bin/bash
set -e

# hadoop 2
if [ -n "$slaves" ];then
    IFS=,
    ary=($slaves)
    echo "" > $HADOOP_HOME/etc/hadoop/slaves
    echo "" > $SPARK_HOME/conf/slaves
    for key in "${!ary[@]}"; 
        do echo "${ary[$key]}" >> $HADOOP_HOME/etc/hadoop/slaves; 
           echo "${ary[$key]}" >> $SPARK_HOME/conf/slaves; 
    done
fi

if [ -n "$masters" ];then
    echo "export SPARK_MASTER_IP=$masters" >> $SPARK_HOME/conf/spark-env.sh
fi

service ssh start

if [ -n "$start" ];then
    eval $start
fi

exec $@