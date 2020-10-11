#!/bin/bash
set -e


if [ -n "$slaves" ];then
    IFS=,
    ary=($slaves)
    echo "" > $HADOOP_HOME/etc/hadoop/slaves
    for key in "${!ary[@]}"; do echo "${ary[$key]}" >> $HADOOP_HOME/etc/hadoop/slaves; done
fi


if [ -n "$regionservers" ];then
    IFS=,
    ary=($regionservers)
    echo "" > $HBASE_HOME/conf/regionservers
    for key in "${!ary[@]}"; 
        do echo "${ary[$key]}" >> $HBASE_HOME/conf/regionservers; 
    done
fi

service ssh start

if [ -n "$start" ];then
    eval $start
fi

exec $@
