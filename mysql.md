
## Mysql常用命令
```
mysql -u username -p  // 登录
set password=password('123456'); //设置密码为123456
flush privileges ; //刷新修改
status; //查看数据库信息

grant all privileges on discuzdb.* to test@'127.0.0.1' identified by 'password'; //创建用户并授权
grant insert,delete,update,select,create,drop on *.* to test@"%" identified by "password"; //创建远程用户
grant select on mysql.* to reader@"%" identified by "password"; //创建远程用户


grant all privileges on seata.storage_tbl to test1@'%' identified by '123456';
grant all privileges on seata.order_tbl to test2@'%' identified by '123456';
grant all privileges on seata.account_tbl to test3@'%' identified by '123456';

8.0
CREATE USER user@'%' IDENTIFIED WITH mysql_native_password BY 'userm123.';  
GRANT ALL ON *.* TO user@'%' WITH GRANT OPTION; 

//修改默认编码(配置文件)
default-character-set=utf8  //在[mysqld]下面
default-character-set=utf8  //在[client]下面
default-storage-engine = MyISAM //设置默认引擎,在[mysqld]下面
set storage_engine=InnoDB; //设置当前会话的默认引擎

//show 命令集
show character set; //查看所支持的字符集
show columns from table_name from db_name; //查看列
show create database test; //查看数据库test建库语句
show create table test; //查看表test建表语句
show databases; //显示所有数据库
show engines; // 显示安装以后可用的存储引擎和默认引擎
show grants for username; //查看用户权限
show index from talbe_name from db_name; //查看索引
show innodb status; // 显示innoDB
show privileges; // 显示服务器所支持的不同权限
show processlist; // 显示系统中正在运行的所有进程
show open tables ; //查看打开的表
show status ;  //查看当前状态
show tables from db_name; //查看某个数据库中的表
show table status from db_name; //查看某个表状态
show variables; //  查看配置参数
show variables like 'character%'; //查看当前编码坏境
```

## 修改密码
```
1. 停止mysqld；
/etc/init.d/mysql stop
(您可能有其它的方法,总之停止mysqld的运行就可以了)
2. 用以下命令启动MySQL，以不检查权限的方式启动；
mysqld --skip-grant-tables &
3. 然后用空密码方式使用root用户登录 MySQL；
mysql -u root
4. 修改root用户的密码；
mysql> update mysql.user set password=PASSWORD('newpassword') where User='root';
mysql> flush privileges;
mysql> quit
重新启动MySQL
/etc/init.d/mysql restart
就可以使用新密码 newpassword 登录了。

mysql 5.6的初始密码生成在/root目录下
mysql --help
```

## 引擎
InnoDB 支持事务,具高可靠性,高性能和搞扩展性. 提供缓冲,二次写,自适应哈希索引,预读.采用聚集方式,每张表的存储是按主键的顺序存放,如果没有主键会自动生成6个字节的ROWID并以此作为主键.

MyISAM 不支持事务和表锁,支持全文索引.

NDB 集群存储引擎,数据全存放于内存中(5.1开始可将非索引数据放磁盘中),

Memory 数据放于内存中,

Archive  只支持insert和select

Federated

Maria 可看作MyISAM后续版本

Merge

CSV

Sphinx

Infobright

支持全文索引:MyISAM,InnoDB,Sphinx

show engines\G; //查看引擎

充分利用缓冲池:内存>=数据+索引

## 备份:
按方法:热备,冷备,温备
按文件:逻辑备份,裸文件备份
按内容:完全备份,增量备份,日志备份

## 简单定时备份(少量数据)
```
# mysql备份脚本,mysqldump记得加上全路径,否则文件可能为空
vi /mnt/bak/data/mysqlbakup.sh
#!/bin/sh

curdir=${0%/*}
curdate=`date +%y%m%d_%H%M%S`
user='tes'
pwd='12341'
host=1.1.1.1
port=3306
basic_name=/mnt/bak/data/baohe_basic_$curdate.sql
rt_name=/mnt/bak/data/baohe_rt_$curdate.sql

cd $curdir
mysqldump --opt --add-drop-table=false -u$user -p$pwd -h$host -P$port baohe_basic > $basic_name
mysqldump --opt --add-drop-table=false -u$user -p$pwd -h$host -P$port baohe_rt > $rt_name
tar cfz $curdate.tar.gz *.sql
rm -rf *.sql

./mv.py
exit

#定时任务
crontab  -e
0 12 * * * /mnt/bak/data/mysqlbakup.sh

#定时任务自启动
vi /etc/rc.d/rc.local
/sbin/service crond start

``` 

## 简单备份与恢复(少量数据)
```
1.备份
mysqldump -uroot -p123456 test -l -F >'/tmp/test.sql'

mysqldump -h10.171.232.8 -P3306 -ureader -p test_db trading_charge --lock-tables=false --where=" occurringTime > '2018-03' " > /mnt/temp/ttt.sql

2.恢复
mysql -uroot -p123456 test < '/tmp/test.sql'

导出到文件
select substring(email,1,length(email)-13) as email from ewomail.i_users 
into outfile '/temp.csv';

``` 

## 错误锁定限制
set global max_connect_errors = 1000;

## 存储过程
```
DELIMITER $$
USE `test_db`$$
DROP PROCEDURE IF EXISTS `pro_name`$$

CREATE PROCEDURE `pro_name`(IN beginId BIGINT, IN n INT)
BEGIN
	DECLARE i INT;
	DECLARE id BIGINT;
	SET i = 0;
	WHILE i < n DO
		INSERT INTO test_db.test(id, passwd, role, state) VALUES (i+beginId, 'password',, 1, 3);
		SET i = i + 1;
	END WHILE;
END$$
DELIMITER ;

CALL pro_name(9999999, 2);

```

## 慢查询记录
```
slow_query_log = 1
slow_query_log_file = slow.log
long_query_time = 1

或者
set global log_output='Table';
set global slow_query_log='ON';
set global slow_launch_time=2;
set global long_query_time=3;
```


## 全文索引 模糊查询
    explain select * from baohe_rt.`trading` t  where match(t.`trackingNo`)against('1632655850')

## 查询结果建表
create table select ...

## 分区表
```
create table `sms_info_new` partition by range(UNIX_TIMESTAMP(sendTime))(
partition sms_info_2015 values less than(UNIX_TIMESTAMP('2016-01-01 00:00:00')),
partition sms_info_2016 values less than(UNIX_TIMESTAMP('2017-01-01 00:00:00'))
)
as select * from  sms_info;
drop table  sms_info;
rename table sms_info_new to sms_info;
// 需要重建索引
// see http://pandarabbit.blog.163.com/blog/static/2092841442012617450920/
// see http://xuebinbin212.blog.163.com/blog/static/112167376201111294041677/
// 添加分区
alter table sms_info add partition (partition sms_info_2017_1 values less than (UNIX_TIMESTAMP('2017-04-01 00:00:00')));
// 扩展分区
alter table sms_info
reorganize partition sms_info_2016 into
(
partition sms_info_2016_1 values less than(UNIX_TIMESTAMP('2016-07-01 00:00:00')),
partition sms_info_2016_2 values less than(UNIX_TIMESTAMP('2017-01-01 00:00:00'))
);
// 直接修改为分区表
alter table trading_charge partition by range(feeId)(
partition trading_charge_max values less than(9999999999)
);
//扩展
alter table trading_charge
reorganize partition trading_charge_max into
(
partition trading_charge_201605 values less than(11022349),
partition trading_charge_max values less than(9999999999)
);
# 删除分区
alter table sms_info coalesce partition sms_info_2015_1;

```

## window 下安装
mysqld install mysql57

mysqld --initialize-insecure --user=mysql

## mysql 基准测试
--- see https://github.com/akopytov/sysbench

---  生成测试数据
```
sysbench \
--mysql-table-engine=innodb \
--mysql-host=localhost \
--mysql-port=3306 \
--mysql-db=test \
--mysql-user=root \
--mysql-password=123456 \
--oltp-table-size=100000 \
--oltp-table-name=t_test \
--db-driver=mysql \
--threads=10 \
--events=10000 \
oltp_read_only \
run
```
```
sysbench --test=oltp --oltp-table-size=5000000 --oltp-table-name=t_test \
--mysql-table-engine=innodb \
--mysql-host=10.168.177.75 \
--mysql-port=3308 \
--mysql-db=test \
--mysql-user=jimbo \
--mysql-password=jimbo123456 \
--db-driver=mysql \
--num-threads=100 \
run
```
--- 帮助命令
sysbench --test=oltp help

-- 新
http://linuxperformance.top/index.php/archives/83/

sysbench oltp_insert \
--db-driver=mysql \
--mysql-user=root \
--mysql-password=123456 \
--mysql-port=21306 \
--mysql-host=localhost \
--threads=20 \
--time=15 \
prepare


##  文件测试
```
sysbench --test=fileio --file-num=16 --file-total-size=2G prepare
sysbench --test=fileio --file-total-size=2G --file-test-mode=rndrd --max-time=180 --max-requests=100000000 --num-threads=16 --init-rng=on --file-num=16 --file-extra-flags=direct --file-fsync-freq=0 --file-block-size=16384 run
sysbench --test=fileio --file-num=16 --file-total-size=2G cleanup
```

## 表信息查询语句
```
select * from information_schema.`COLUMNS` t where  t.`EXTRA`='on update CURRENT_TIMESTAMP';
select * from information_schema.`TABLES` t order by t.`TABLE_ROWS` desc;
select t.table_schema,t.table_name from test.`TABLES` t where t.table_schema!='test_db' and t.table_rows>1000000 order by t.`TABLE_ROWS` desc;
```

## frm 表结构恢复
```
curl -s http://get.dbsake.net > dbsake 
chmod u+x dbsake 
./dbsake --version 
./dbsake frmdump /var/lib/mysql/sakila/staff.frm
```

## 查看瞬时的连接情况，以及这些连接当前正在处理的语句
show processlist;

## 查看最大连接数
show global status like 'Max_used_connections';

## 查找用了全表扫描的语句
select * from sys.`statements_with_full_table_scans`;

## 日志订阅
docker run -it --rm zendesk/maxwell bin/maxwell --user='root' --password='xxx' --host='10.10.10.106' --port='53306' --producer=stdout