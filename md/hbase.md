##下载
http://mirrors.hust.edu.cn/apache/hbase/stable/

## 配置
https://hbase.apache.org/book.html#config.files

vi conf/hbase-site.xml
```
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///mnt/data/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/mnt/data/zookeeper</value>
  </property>
</configuration>
```
## 启动
bin/start-hbase.sh

## 连接
bin/hbase shell

## 查看命名空间
list_namespace

## 查看命名空间下表
list_namespace_tables 'hbase'

## 帮助
help 'list'

## 查看服务器状态
status

## 查看版本
version

## 建表
create 'test', 'cf'

## 查看所有表
list

## 查看表
list 'test'
describe 'test'

## 插入
```
put 'test', 'row1', 'cf:a', 'value1'
put 'test', 'row2', 'cf:name', '小明'
put 'test', 'row2', 'cf:age', 13
```
## 查询
```
get 'test','row1'
get 'test','row2'
get 'test','row2' 'cf'
```
## 浏览
scan 'test'

## 工具查看
tools


## Hbase程序设计
```
1.创建一个Configuration对象
  包含各种配置信息
2.创建一个HTable句柄
  提供Configuration对象
  提供待访问的Table的名称
3.执行相应的操作
  执行put,get,delete,scan等操作
4.关闭HTable句柄
  将内存数据刷新到新磁盘上
  释放各种资源
```

## 数据结构
Table -> Column Family -> Column
Cell: rowkey + CF + rowName + rowValue + timestamp
Hbase -> Region server -> Hregion+Hlog -> HStore -> MemStore+StoreFile

## 数据查找
rowkey, rowkey range,timerange,bloom filter,cache,filter
https://segmentfault.com/a/1190000023423629
https://bbs.huaweicloud.com/forum/thread-69747-1-1.html
https://blog.csdn.net/x541211190/article/details/108434475

## count 
count 'user',INTERVAL => 200000,CACHE => 20000
hbase org.apache.hadoop.hbase.mapreduce.RowCounter 'user'

## 快照备份
snapshot 'default:user', 'userSnapshot'
list_snapshots

## 删除快照
delete_table_snapshots "user"

## 导出快照
hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dmapreduce.map.memory.mb=4096 -Dmapreduce.map.java.opts=-Xmx4096m -snapshot userSnapshot  -copy-to hdfs://mycluster:8020/hbase/snapshot/userSnapshot

or

hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dmapreduce.map.memory.mb=4096 -Dmapreduce.map.java.opts=-Xmx4096m -snapshot userSnapshot -copy-to /tmp/userSnapshot

## 还原快照
hbase org.apache.hadoop.hbase.snapshot.ExportSnapshot -Dmapreduce.map.memory.mb=4096 -Dmapreduce.map.java.opts=-Xmx4096m -snapshot userSnapshot -copy-from /hbase/snapshot/userSnapshot -copy-to /hbase/

##　还原表
disable 'user'
restore_snapshot 'userSnapshot'
enable 'user'

https://cloud.tencent.com/developer/article/1539797


