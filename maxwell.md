# maxwell

## mysql config
CREATE database maxwell;
CREATE USER 'maxwell'@'%' IDENTIFIED BY '123456';
GRANT ALL ON maxwell.* TO 'maxwell'@'%';
GRANT SELECT, REPLICATION CLIENT, REPLICATION SLAVE ON *.* TO 'maxwell'@'%';
flush privileges;

## docker test
docker run -it --rm zendesk/maxwell bin/maxwell --user=maxwell \
    --password=123456 --host=10.10.10.106 --port=53306 --producer=stdout

ctrl+c cancel ...

## start
docker run -it --rm --network mynetwork  zendesk/maxwell bin/maxwell \
    --user=maxwell --password=123456 --host=10.10.10.106 --port=53306 --producer=kafka  \
    --kafka.bootstrap.servers=172.16.16.31:9092,172.16.16.32:9092,172.16.16.33:9092 --kafka_topic=test

## bootstrapping
INSERT INTO maxwell.bootstrap (database_name, table_name) VALUES ('test', 'company');
INSERT INTO maxwell.bootstrap (database_name, table_name) VALUES ('test', 'user');

## doc
http://maxwells-daemon.io/quickstart/

https://support.huaweicloud.com/usermanual-mrs/mrs_01_0441.html    

