##下载
http://mirrors.hust.edu.cn/apache/hbase/stable/

## 配置
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
## 建表

create 'test', 'cf'

##查看表
list 'test'

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