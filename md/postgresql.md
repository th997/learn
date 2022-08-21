# postgresql

## quick start http://www.ruanyifeng.com/blog/2013/12/getting_started_with_postgresql.html

## 安装
sudo apt-get install postgresql

## 登录, postgres 为默认用户
sudo su - postgres
psql

## 创建密码
\password postgres

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
select * from pg_settings

update pg_settings set setting ='xx' where name='xx'

## 查看连接
select * from pg_stat_activity

## 索引
https://zhuanlan.zhihu.com/p/457860359

https://developer.aliyun.com/article/111793#slide-11

```sql
drop INDEX w2000_c5_idx
CREATE INDEX w2000_c5_idx ON public.w2000 (c5); // 支持 = > <
CREATE INDEX w2000_c5_idx ON public.w2000 (c5 collate "C"); -- 支持 like 'xxx%'
CREATE EXTENSION pg_trgm;
CREATE INDEX w2000_c5_idx ON public.w2000 USING gin (c5 gin_trgm_ops); -- 支持全文搜索
```