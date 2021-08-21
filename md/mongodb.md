i.安装与启动
rsync -a bin /usr/local/mongodb	 //将bin目录复制到/usr/local/mongodb
./mongod --dbpath=/usr/local/mongodb/data/ --logpath=/usr/local/mongodb/dblogs/log --fork //后台启动mongodb
pstree -p|grep mongod //查看进程树
pkill mongod //关闭
./mongo //连接数据库
mongod 参数:
--dpath 指定数据库目录
--port 指定端口,默认27017
--bind_ip 绑定ip
--directoryperdb 为每个db创建独特子目录
--logpath 指定日子目录
--logappend 日志生成方式(追加/覆盖)
--pidfilepath 指定进程文件,如不指定将产生进程文件
--keyFile 集群模式的关键标志
--journal 启用日志
--nssize 指定.ns文件的大小,单位MB,默认16M,最大2GB
--maxConns 最大并发连接数
--notablescan 不允许进行表扫描
--noprealloc 关闭数据文件的预分配功能
--fork 以后台Daemon形式运行服务
--help 帮助

i.权限认证
./mongod --dbpath=/usr/local/mongodb/data/ --logpath=/usr/local/mongodb/dblogs/log --fork --auth
use test;
db.addUser("kjb","123456");
./mongo test -ukjb -p123456;
show tables;
show dbs;


i.数据类型：
	null
	true，false
	数字
	字符串
	符号
	ObjectId
	日期 new Date()
	正则表达式
	代码
	数组
	内嵌文档

i.语句
db //查看当前书库
show dbs
show tables
show collections
db.user.insert({"username":"test","passwd":"123456","email":"th997@qq.com"});//插入
db.user.find();//查询所有
db.user.findOne();//查询第一条
db.songs.distinct("username");//去重查询
db.user.find({"username":"test"});//等值查询
db.user.remove({})//删除；
db.user.update({"username":"test"},{$set:{"passwd":"1234"}});//更新
db.user.find().count();//查询记录数

//非等值查询
db.songs.find({"id":{$gt:1}});
db.songs.find({"id":{$lt:2}});
db.songs.find({"id":{$gte:1}});
db.songs.find({"id":{$lte:2}});
db.songs.find({"id":{$lte:4,$gte:3}})
//包含查询
db.songs.find({"name":/a/});(包含a)
db.songs.find({"name":/^a/});(以a开头)
db.songs.find({"name":{$all:1,2,3}});
db.songs.find({"name":{$exist:1}});
//查询结果指定列
db.songs.find({},{name:1}); (只显示name列)
db.songs.find({},{name:1,id:1}); (显示name和id两列)
//排序(1:asc,-1:desc)
db.songs.find().sort({id:1});
db.songs.find().sort({id:-1});
db.songs.find().sort({"id":1,"name":-1}
//分页 limit是pageSize，skip是第几页*pageSize
db.songs.find().sort({"id":-1}).limit(1);
db.songs.find().sort({"id":-1}).skip(1).limit(1);
//OR查询
db.songs.find({$or:[{"id":1},{"id":2}]})
db.songs.find({$or:[{"id":1},{"id":{$gte:3}}]})
//<>
db.songs.find({"name":{"$ne":"kebi"}});
//in
db.songs.find({"age":{$in:[12,22,33]}});
//选择特定列(0=false,!0=true)
db.songs.find({"age":{$in:[12,22,33]}},{age:0});
db.songs.find({"age":{$in:[12,22,33]}},{age:1});


创建用户
use admin
db.createUser(
  {
    user: "buru",
    pwd: "12345678",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)
db.auth("buru","12345678") #认证，返回1表示成功
show users
关闭服务
user admin
db.shutdownServer()

索引:
db.user.ensureIndex({"t4":-1})

安装启动
#mongodb.conf
port=27017
dbpath=/mnt/soft/mongodb/data/
logpath=/mnt/soft/mongodb/log/mongodb.log
journal=true
#!/bin/sh
cd ${0%/*}
(bin/mongod --config mongodb.conf &)

google mongodb：
mongodb1-arbiter-1
mongodb1-instance-1, mongodb1-instance-1-data
mongodb1-instance-2, mongodb1-instance-2-data

group 查询
https://docs.mongodb.org/manual/reference/operator/aggregation-pipeline/
db.WebAccessLog.aggregate(
   [
     { $match: {"accessTime":{$gt:'2015-12'}}},
     { $group : { _id : "$functionCode" ,count: { $sum: 1 }} }
   ]
)
// 统计24小时每个小时的API访问数
db.WebAccessLog.aggregate([{ $group: { _id: { hour: { $substr: ["$accessTime", 11, 2] } }, count: { $sum: 1 } } }]);

// 统计每个functionCode的访问次数
db.WebAccessLog.aggregate([{$group: {_id: "$functionCode", accessCount: {$sum: 1}}}]);

db.getCollection('WebAccessLog').find({'functionCode':920,'memberId':1086219,'accessTime':{"$gt":'2016-06-25'}})

db.getCollection('WebAccessLog').find({'functionCode':920,'memberId':1086219,'accessTime':{"$gt":'2016-06-25'}})


## docker 简易安装
docker pull mongo:latest
docker run -itd --name mongo -p 27017:27017 mongo --auth
docker exec -it mongo mongo admin
db.createUser({user:"root",pwd:"123456",roles:["root"] })
db.auth('root', '123456')

## client 
https://nosqlbooster.com/downloads