#!/bin/bash
set -e

service ssh start
service ntp start 
service rpcbind start

if [ -n "$start" ];then
    eval $start
fi

exec $@