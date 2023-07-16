#!/bin/bash
set -e

sudo service ssh start

. /usr/local/greenplum-db/greenplum_path.sh

if [ -n "$start" ];then
    eval $start
fi

exec $@