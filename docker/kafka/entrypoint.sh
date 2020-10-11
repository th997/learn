#!/bin/bash
set -e

if [ -n "$broker_id" ];then
    echo "broker.id=$broker_id" >> $KAFKA_HOME/config/server.properties
fi

if [ -n "$zookeeper_connect" ];then
    echo "zookeeper.connect=$zookeeper_connect" >> $KAFKA_HOME/config/server.properties
fi

if [ -n "$host" ];then
    echo "advertised.listeners=PLAINTEXT://$host:9092" >> $KAFKA_HOME/config/server.properties
fi


if [ -n "$start" ];then
    eval $start
fi

exec $@