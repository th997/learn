i.安装与启动
tar -zxvf redis-2.6.14.tar.gz
cd redis-2.6.14
make
mv ./redis.conf /usr/local/redis/etc/
cd src
mv redis-server redis-benchmark redis-check-aof redis-check-dump redis-cli mkreleasehdr.sh  redis-sentinel /usr/local/redis/bin/
cd /usr/local/redis/bin/
vim /usr/local/redis/etc/redis.conf --daemonize yes --启动时后台启动
./redis-server --启动
./redis-server /usr/local/redis/etc/redis.conf --指定配置文件启动
./redis-cli --客户端连接
auth 123456 --以123456密码登录
./redis-cli -a 123456 --以123456密码登录
quit --退出客户端
pkill redis-server --关闭
./redis-cli shutdown --关闭

i.配置文件redis.conf
daemonize no --启动方式
pidfile /var/run/redis.pid --pid地址
port 6379 --端口
timeout 0 --客户端连接超时时间(s),0为不限制
loglevel notice --日志级别,debug,verbose,notic,warning
logfile stdout --日志地址
databases 16 --数据库个数
save 900 1 --设置数据库镜像频率
save 300 10
save 60 10000
rdbcompression yes --景象备份时是否压缩
dbfilename dump.rdb --镜像文件
dri ./ --镜像文件路径
# slaveof <masterip> <masterport> --设置数据库为其它数据库的从数据库
# masterauth <master-password> --主数据库连接时需要的密码验证
# requirepass foobared --登录密码
# maxclients 10000 --客户端数量
# maxmemory <bytes> --使用的最大内存
appendfsync everysec --appendonly.aof文件同步的频率 always,everysec,no

i.数据类型及操作
string --二进制安全,可包含任何数据,比如图片,序列化对象
--set get setnx setex setrange mset msetnx getset getrange mget incr incrby decr decrby append strlen
hash
--hset hget hsetnx hmset hmget hincrby hexists hlen hdel hkeys hvals hgetall
list --双向链表结构,同时既可以作为栈又可作为队列
--lpush lpop lrange linsert lset lrem ltrim rpoplpush lindex
set
--sadd smembers srem spop sdiff sdiffstore sinter sinterstore sunion sunionstore smove scard sismember srandmember
zset --有序集合
--zadd zrange zrem zincrby zrand zverrange zrangebysocre zrangebyrank

i.基本操作
keys * --查询所有键
keys my* --查询以my开头的所有键
exists name --查看是否存在name键
del  name --删除name键
expire   name 10 --设置name键过期时间为10s
ttl name --查看键的剩余时间
persist naem --取消过期
select 0 --选择0数据库
move name 1 --将name移动到1数据库
randomkey --随机返回一个键
rename name name1 --将name重命名为name1
type name --查看name的类型
ping --查看连接是否正常
echo abc --输出"abc"
dbsize --当前数据库键数目
info --获取数据库信息
config get * --获取配置信息
config get port --获取配置中端口信息
flushdb --清空当前数据库
flushall --清空所有数据库中所有键
multi --开始事务
...
exec  --执行事务
multi --开始事务
...
discard --取消事务
watch name --监控name的值,当执行multi时,name被其它用户改变,则multi执行会失败(乐观锁)

i.持久化操作
1 snapshotting(快照)
save 300 10 --如果300秒内有10个key被修改则保存快照
2 append-only file(aof)
appendonly yes --启用aof
appendsync always --每个write都会写到文件中,always,everysec,no

i.消息发布与订阅
subscribe tv1 --订阅tv1
publish tv1 hello --向tv1发布消息"hello"
pubsub channels --打印所有频道
pubsub numsub [pattern] 频道订阅者数量
pubsub numpat

flushdb:删除这个db下的。
flushall:删除所有

spring redis cluster
http://blog.csdn.net/zhu_tianwei/article/details/49691501


wget http://download.redis.io/releases/redis-3.0.7.tar.gz
cd redis-3.0.7
make
make install
redis-server &
## 压测
src/redis-benchmark
redis-benchmark -h
redis-benchmark -h 172.17.0.3 -t get,set -n 100000 -d 1024
redis-benchmark -h 172.17.0.3 -t get,set -n 1000000 -d 1024 -P 16
redis-benchmark -h 192.168.8.164 -t get,set -n 100000 -d 1024
redis-benchmark -t get,set -n 100000 -d 1024 -c 3000

## redis 集群
http://www.redis.cn/topics/cluster-tutorial.html

--- 配置文件

vim redis.conf
```
port 6379
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
```
-- 分发 创建3台redis机器并启动

redis-server redis.conf &

-- 创建集群
```
src/redis-trib.rb create  172.17.0.3:6379 172.17.0.4:6379 172.17.0.5:6379
可能需要安装依赖
apt-get install ruby
gem install redis
```
