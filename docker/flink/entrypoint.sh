#!/bin/bash
set -e

# hadoop 2
if [ -n "$slaves" ];then
    IFS=,
    ary=($slaves)
    echo "" > $HADOOP_HOME/etc/hadoop/slaves
    for key in "${!ary[@]}"; do echo "${ary[$key]}" >> $HADOOP_HOME/etc/hadoop/slaves; done
fi

# hadoop 3
if [ -n "$workers" ];then
    IFS=,
    ary=($workers)
    echo "" > $HADOOP_HOME/etc/hadoop/workers
    for key in "${!ary[@]}"; do echo "${ary[$key]}" >> $HADOOP_HOME/etc/hadoop/workers; done
fi

service ssh start

if [ -n "$start" ];then
    eval $start
fi

exec $@
