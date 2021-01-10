# clickhouse

## docker test
docker run -it --rm --name some-clickhouse-server --ulimit nofile=262144:262144 yandex/clickhouse-server

docker run -it --rm --link some-clickhouse-server:clickhouse-server yandex/clickhouse-client --host clickhouse-server

## file dir
/etc/clickhouse-server/config.xml
/etc/clickhouse-server/config.d/
/etc/clickhouse-server/user.xml
/etc/clickhouse-server/user.d
/var/lib/clickhouse

## install 
./docker/clickhouse/

## MergeTree
```sql
-- 在各个节点创建本地表
CREATE TABLE default.test1_local
(
    `id` Int32,
    `website` String,
    `wechat` String,
    `FlightDate` Date,
    Year UInt16
)ENGINE = MergeTree
PARTITION BY FlightDate
ORDER BY (Year, FlightDate)
SETTINGS index_granularity = 8192;

-- 在其中一个节点创建分布式表
CREATE TABLE default.test1 AS test1_local
ENGINE = Distributed(ck_cluster1, default, test1_local, rand());

-- 往分布式表插入数据
INSERT INTO default.test1 (id,website,wechat,FlightDate,Year)values(1,'https://xxxxx1.com/','xxxxx1','2020-11-28',2020);
INSERT INTO default.test1 (id,website,wechat,FlightDate,Year)values(2,'http://www.xxxxx2.cn/','xxxxx2','2020-11-27',2019);
INSERT INTO default.test1 (id,website,wechat,FlightDate,Year)values(3,'http://www.xxxxx3.cn/','xxxxx3','2020-11-26',2018);
INSERT INTO default.test1 (id,website,wechat,FlightDate,Year)values(4,'http://www.xxxxx4.cn/','xxxxx4','2020-11-25',2017);
INSERT INTO default.test1 (id,website,wechat,FlightDate,Year)values(5,'http://www.xxxxx5.cn/','xxxxx5','2020-11-24',2016);
INSERT INTO default.test1 (id,website,wechat,FlightDate,Year)values(6,'http://www.xxxxx6.cn/','xxxxx6','2020-11-23',2015);

-- 往分布式表查询数据
select * from test1;

```

## ReplacingMergeTree
```sql
-- 分布式ddl,任意一个节点执行                                                        
CREATE TABLE default.test2_local on cluster ck_cluster2
(
    `id` Int32,
    `website` String,
    `wechat` String,
    `FlightDate` Date,
    Year UInt16
)ENGINE = ReplicatedMergeTree('/clickhouse/tables/{shard}/test2','{replica}')
PARTITION BY FlightDate
ORDER BY (Year, FlightDate)
SETTINGS index_granularity = 8192;

-- 分布式表
CREATE TABLE default.test2 AS test2_local
ENGINE = Distributed(ck_cluster2, default, test2_local, rand());

-- 插入数据
INSERT INTO default.test2 (id,website,wechat,FlightDate,Year)values(1,'https://xxxxx1.com/','xxxxx1','2020-11-28',2020);
INSERT INTO default.test2 (id,website,wechat,FlightDate,Year)values(2,'http://www.xxxxx2.cn/','xxxxx2','2020-11-27',2019);
INSERT INTO default.test2 (id,website,wechat,FlightDate,Year)values(3,'http://www.xxxxx3.cn/','xxxxx3','2020-11-26',2018);
INSERT INTO default.test2 (id,website,wechat,FlightDate,Year)values(4,'http://www.xxxxx4.cn/','xxxxx4','2020-11-25',2017);
INSERT INTO default.test2 (id,website,wechat,FlightDate,Year)values(5,'http://www.xxxxx5.cn/','xxxxx5','2020-11-24',2016);
INSERT INTO default.test2 (id,website,wechat,FlightDate,Year)values(6,'http://www.xxxxx6.cn/','xxxxx6','2020-11-23',2015);

-- 查询数据
select * from default.test2 ;

```

## command
``` sql
-- 查看集群
select * from system.clusters;
-- 查看宏
select * from system.macros;
-- 查看引擎
SELECT * FROM table_engines;
-- 查看函数
SELECT * FROM functions limit 100;

```

## doc
https://segmentfault.com/a/1190000038318776
https://zhuanlan.zhihu.com/p/161242274




