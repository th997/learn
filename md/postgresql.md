# postgresql

## quick start http://www.ruanyifeng.com/blog/2013/12/getting_started_with_postgresql.html

## 安装
sudo apt-get install postgresql

## 登录, postgres 为默认用户
sudo su - postgres
psql

## 创建密码
\password postgres

## 修改密码
alter user postgres with password 'postgres';

## 远程访问
```
vi pg_hba.conf
host  all    all     0.0.0.0/0     md5
vi postgresql.conf
listen_addresses='*'
/etc/init.d/postgresql restart
```

## 创建用户,授权,登录
```
sudo adduser dbuser
CREATE USER dbuser WITH PASSWORD '123456';
CREATE DATABASE mydb OWNER dbuser;
GRANT ALL PRIVILEGES ON DATABASE mydb to dbuser;
psql -U dbuser -d  mydb -h 127.0.0.1 -p 5432
```

## 控制台命令
```
\h：查看SQL命令的解释，比如\h select。
\?：查看psql命令列表。
\l：列出所有数据库。
\c [database_name]：连接其他数据库。
\d：列出当前数据库的所有表格。
\d [table_name]：列出某一张表格的结构。
\du：列出所有用户。
\e：打开文本编辑器。
\conninfo：列出当前数据库和连接的信息。
```

## 建表
```
CREATE TABLE test (
  id serial NOT NULL,
  c1 varchar(255) DEFAULT NULL,
  PRIMARY KEY (id) 
)
``` 

## 查看/修改设置
select * from pg_catalog.pg_settings ps where "source" !='default'

update pg_settings set setting ='xx' where name='xx'

## 查看扩展
select * from pg_catalog.pg_available_extensions

## 查看连接
select * from pg_stat_activity

## 索引
https://zhuanlan.zhihu.com/p/457860359

https://developer.aliyun.com/article/111793#slide-11

https://www.postgresql.org/docs/current/indexes-types.html

```sql
drop INDEX w2000_c5_idx
CREATE INDEX concurrently w2000_c5_idx ON public.w2000 (c5);-- concurrently避免锁表 支持 <   <=   =   >=   >
CREATE INDEX w2000_c5_idx ON public.w2000 (c5 collate "C"); -- 支持 like 'xxx%'
CREATE EXTENSION pg_trgm;
CREATE INDEX w2000_c8_idx ON public.w2000 USING gin (c1,c8 gin_trgm_ops); -- 支持全文搜索?
```

## 清除delete数据占用的空间
vacuum verbose analyze [full] [verbose] [table] [column]

http://www.postgres.cn/docs/9.4/sql-vacuum.html


## 常用参数配置
```conf
# https://github.com/digoal/blog/blob/master/201812/20181203_01.md
# max_connections 公式：物理内存(GB)*1000*(1/4)/5
max_connections = 800
# IF use hugepage: 主机内存*(1/4) ELSE: min(32GB, 主机内存*(1/4))
shared_buffers = 4096MB
# 如果磁盘性能很差，并且是OLTP业务。可以考虑设置为off降低COMMIT的RT，提高吞吐(设置为OFF时，可能丢失部分XLOG RECORD)   
# 可以在代码在代码中单个事物中设置： set local synchronous_commit = off  https://github.com/digoal/blog/blob/master/201712/20171207_01.md
# 各参数危险程度 https://www.modb.pro/db/94082
synchronous_commit = off
# 无危险，分组提交，可能会导致一定的延迟，并发请求下可以提高总tps (微秒)
commit_delay = 5000
commit_siblings = 5
```

## 压力测试
```shell
# sysbench
docker run -it --rm  severalnines/sysbench bash
# prepare/run/cleanup
sysbench --db-driver=pgsql --pgsql-host=10.10.10.106 --pgsql-port=55432 --pgsql-user=postgres --pgsql-password=postgres_666888 --pgsql-db=postgres --oltp-tables-count=1 --oltp-table-size=1000000 --rand-init=on --threads=1 --time=60 --events=0 --report-interval=10 --percentile=99 /usr/share/sysbench/tests/include/oltp_legacy/update_index.lua run

# pgbench
docker run -it --rm postgres bash 
export PGHOST=10.10.10.106
export PGPORT=55432
export PGDATABASE=postgres
export PGUSER=postgres
export PGPASSWORD=postgres_666888
# 初始化 没有日志的表，100×10w数据
pgbench -i --unlogged-tables -s 100 -U postgres -p 55432 -d postgres -h 10.0.8.2 
# 一个client，一个job，10秒压测
pgbench -M prepared -r -c 1 -j 1 -T 10 -U postgres -p 55432 -d postgres -h 10.0.8.2 
```

## 从库搭建
```sql
-- 主库上添加复制帐号
alter user postgres with replication;
or
create user replicator replication login encrypted password 'password';

-- 主库上修改配置文件 pg_hba.conf
host    replication     postgres        10.1.1.1/8          md5

-- 重启主库

-- 登陆备库机器，停止pg进程，清空原data目录，从主库备份数据
-- -Fp表示以plain格式数据，-Xs表示以stream方式包含所需的WAL文件，-v日志，-P表示显示进度，-R表示为replication写配置信息。
docker run -it --rm -v $PWD/data:/var/lib/postgresql/data postgres:13 \
pg_basebackup -F p  -X s -v -R -P -h 10.10.10.106 -p 55432 -D /var/lib/postgresql/data -U postgres 

-- 启动从库

-- 查看从库状态
select * from pg_stat_replication;

-- 验证数据

-- 从库切换为主库
pg_ctl promote

```

## 编译安装
https://www.postgresql.org/docs/current/install-procedure.html

https://www.postgresql.org/docs/current/server-start.html


```sh
git clone --depth=1 -b REL_13_9 https://github.com/postgres/postgres

apt install libreadline-dev build-essential zlib1g-dev libsystemd-dev -y

cd postgres
./configure --prefix=/d/pgsql --with-systemd

make && make install
export PATH=$PATH:/d/pgsql/bin

/d/pgsql/bin/initdb -D /d/pgsql/data

/d/pgsql/bin/pg_ctl -D /d/pgsql/data -l /d/pgsql/logfile start
/d/pgsql/bin/pg_ctl -D /d/pgsql/data stop

```

## 分区表
``` sql
CREATE TABLE my_hash_partitioned_table ( 
    id SERIAL PRIMARY KEY, 
    name VARCHAR(50), 
    created_at TIMESTAMP 
) 
PARTITION BY HASH (id); 
 
DO $$ 
DECLARE 
    i INTEGER; 
    partition_table_name TEXT; 
BEGIN 
    FOR i IN 0..99 LOOP 
        partition_table_name := 'my_hash_partitioned_table_partition_' || i; 
        EXECUTE format('CREATE TABLE %I PARTITION OF my_hash_partitioned_table FOR VALUES WITH (MODULUS 100, REMAINDER %s)', partition_table_name, i); 
    END LOOP; 
END $$; 
 
INSERT INTO my_hash_partitioned_table (name, created_at) VALUES ('John', now()); 
INSERT INTO my_hash_partitioned_table (name, created_at) VALUES ('Jane', now()); 
SELECT * FROM my_hash_partitioned_table;

```





