#!/bin/bash
nohup schematool -initSchema -dbType mysql &
sleep 3
nohup hive --service metastore  &
sleep 3
nohup hive --service hiveserver2 &
sleep 5
nohup zeppelin-daemon.sh start
echo Hive has started !
