# hive 
schematool -initSchema -dbType mysql

```
hive
show functions;
desc function sum;
create database test;

use test;
create table t1(
    id      int
   ,name    string
   ,hobby   array<string>
   ,add     map<String,string>
) row format delimited
fields terminated by ','
collection items terminated by '-'
map keys terminated by ':';

INSERT INTO test.t1 values(2,'test_name',ARRAY("123", "456", "789"),str_to_map("NewKey:NewValue") ); 

load data local inpath '/user.txt' into table user;

```

# hive server
https://www.cnblogs.com/tibit/p/9029905.html
hive --service metastore &
hiveserver2 &
or hive --service hiveserver2

# spark 
http://localhost:58088
http://localhost:50070
http://localhost:58080/

var hc = new org.apache.spark.sql.hive.HiveContext(sc);
hc.sql("show databases").show();

# hadoop
haddop fs
haddop fs -ls /     (查询目录)
hadoop fs -mkdir /test      （在根目录下创建一个目录test）

# zeppelin
docker run -p 58051:8080  -v $PWD/logs:/logs -v $PWD/notebook:/notebook -e ZEPPELIN_LOG_DIR='/logs' -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.8.0

interpreter -> create -> hive jdbc ...
http://zeppelin.apache.org/docs/0.8.1/interpreter/hive.html




