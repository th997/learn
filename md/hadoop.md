 Hadoop
## Hadoop 介绍
1. hadoop-${version}.tar.gz 目录结构
```
	bin: 基本的管理脚本和使用脚本所在目录
	etc: 配置文件目录
	include: 对外提供的编程库文件
	lib: hadoop对外提供的动态库
	libexec: 各个服务对应的
	sbin: hadoop管理脚本所在目录,主要包含HDFS和YARN中各类服务的启动和关闭脚本
	share: 各个模块编译后的jar所在目录
```
2. hadoop-${version}-src.tar.gz
```
	hadoop-common-project: hadoop基础库所在目录,该目录包含了其它所在模块可能会用到的基础库,包括RPC,Metrics,Counter等.
	hadoop-mapreduce-project: MapReduce框架的实现,在MRv1中,MapReduce由编程模型(map/reduce),调度系统(JobTracker和TaskTracker)和数据处理引擎(MapTask和ReduceTask)等模块组成.
	hadoop-hdfs-project: 分布式文件系统的实现.
	hadoop-yarn-project: 资源管理系统YARN实现.
```
3. Hadoop YARN
```
	hadoop-yarn-api: 给出了YARN内部涉及的4个主要RPC协议的Java声明和Protocol Buffers定义,这4个协议分别是ApplicationMasterProtocol,ContainnerManagementProtocol,ResourceManagerAdministrationProtocol
	hadoop-yarn-common: 该部分包含了YARN底层库的实现,包括事件库,服务库,状态机库,Web页面库等.
	hadoop-yarn-applications: 包含了2个Application编程实例,分别是distributedshell和Unmanaged AM.
	hadoop-yarn-client: 与YARN RPC协议交互的相关库
	hadoop-yarn-server: YARN的核心实现,包括ResourceManager,NodeManger,资源管理器等核心组件的实现.
```
4. 配置:
```
	hadoop.env.sh
	#export JAVA_HOME=${JAVA_HOME}
	export JAVA_HOME=/usr/java/jdk1.7.0_75
	4个配置文件:http://hadoop.apache.org/docs/r2.6.0/hadoop-project-dist/hadoop-common/SingleCluster.html
		core-site.xml
		hdfs-site.xml
		mapred-site.xml
		yarn-site.xml
	bin/hdfs namenode -format
	sbin/start-all.sh
	jps
	启动后5个进程 ResourceManager,NodeManager,NameNode,SecondaryNameNode,DataNode
```
5. YARN
```
	ResourceManager: (RM) Master,负责整个系统的资源管理和分配.主要由2个组件构成:调度器(Scheduler)和应用程序管理器(Application Manager,ASM).
	NodeManager: (NM) Slave,每个节点上的资源和任务管理器.定期向RM汇报本节点资源使用情况和各个Container运行状态,接收来自AM的Container启动/停止等各种请求.
	ApplicationMaster: (AM) 负责当个应用程序的管理
```
6. 云计算
```
	IaaS(Infrastructure-as-a-Service),基础设施即服务
	PaaS(Platform-as-a-Service),平台即服务,YANR可看作PAAS层
	Saas(Soft-as-a-Service),软件即服务
```
7.


## Hadoop 命令
```
#创建目录
bin/hadoop fs -mkdir /test
#上传文件
bin/hadoop fs -put /mnt/soft/jdk-7u75-linux-x64.rpm  /test
#http浏览
http://121.43.149.208:50070/explorer.html
http://121.43.149.208:8088/
```


## Hahoop 安装
### 免密码登陆
```
( from http://blog.csdn.net/square_l/article/details/11829797)
所有服务器执行
ssh-keygen -t rsa
	默认在 ~/.ssh目录生成两个文件：
    id_rsa      ：私钥
    id_rsa.pub  ：公钥
主服务器,倒入公钥到本机
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
其他服务器,依次倒入主服务器的公钥
cat ~/id_rsa.pub >> ~/.ssh/authorized_keys
至此,主服务器可以免密码登陆其他服务器
```
### hadoop用户创建
```
useradd -m hadoop -s /bin/bash
passwd hadoop

adduser hadoop sudo
```

### hadoop 集群安装
http://songlee24.github.io/2015/07/20/hadoop-hbase-zookeeper-distributed-mode/
1. 配置系统环境
```
192.168.1.131 h1
192.168.1.132 h2
192.168.1.133 h3
h1 作为主免密码可以ssh登陆到 h2 h3
安装配置jdk
创建hadoop用户
```
2. 修改hadoop配置文件
```
vim etc/hadoop/hadoop-env.sh
	配置JAVA_HOME
vim etc/hadoop/core-site.xml
	<configuration>
	<property>
		<name>fs.default.name</name>
		<value>hdfs://h1:9000</value>
	</property>
	</configuration>
vim etc/hadoop/hdfs-site.xml
	<configuration>
		<property>
			<name>dfs.name.dir</name>
			<value>/mnt/data/hadoop/name</value>
		</property>
		<property>
			<name>dfs.data.dir</name>
			<value>/mnt/data/hadoop/data</value>
		</property>
		<property>
			<name>dfs.replication</name>
			<value>3</value>
		</property>
	</configuration>
vim etc/hadoop/mapred-site.xml
	<configuration>
		<property>
			<name>mapred.job.tracker</name>
			<value>h1:9001</value>
		</property>
	</configuration>
vim etc/hadoop/masters
	h1
vim etc/hadoop/slaves
	h2
	h3
bin/hadoop namenode -format
```
3. 复制，启动集群
```
复制到h2，h3
在h1上启动
sbin/star-all.sh
```

### zookeeper 安装
```
下载 解压后 conf目录下复制
cp zoo_sample.cfg zoo.cfg
修改配置
dataDir=/mnt/data/zookeeper
dataLogDir=/mnt/data/zookeeper
server.1=h1:2888:3888
server.2=h2:2888:3888
server.3=h3:2888:3888
设置myid
echo "1" > /mnt/data/zookeeper/myid
复制到h2,h3
h2
echo "2" > /mnt/data/zookeeper/myid
h3
echo "3" > /mnt/data/zookeeper/myid
启动3个zookeeper
bin/zkServer.sh start
查看状态
bin/zkServer.sh status
```
http://blackproof.iteye.com/blog/2039040

### Hbase 安装
* vim  hbase-env.sh
```
export JAVA_HOME=/mnt/run/jdk1.8.0_65/
export HBASE_CLASSPATH=/mnt/run/hadoop-2.6.0/etc/hadoop/
export HBASE_MANAGES_ZK=false
```
* vim hbase-site.xml
```
<configuration>
	<property>
		<name>hbase.rootdir</name>
		<value>hdfs://h1:9000/hbase</value>
	</property>
	<property>
		<name>hbase.master</name>
		<value>h1</value>
	</property>
	<property>
		<name>hbase.cluster.distributed</name>
		<value>true</value>
	</property>
	<property>
		<name>hbase.zookeeper.property.clientPort</name>
		<value>2181</value>
	</property>
	<property>
		<name>hbase.zookeeper.quorum</name>
		<value>h1,h2,h3</value>
	</property>
	<property>
		<name>zookeeper.session.timeout</name>
		<value>60000000</value>
	</property>
	<property>
		<name>dfs.support.append</name>
		<value>true</value>
	</property>
</configuration>
```
* vim regionservers
	h1
	h2
* 复制到h2,h3,启动hbase
  bin/start-base.sh
* 登陆hbase
bin/hbase shell


### kafka 安装
```
vim config/server.properties
broker.id=0
zookeeper.connect=h1:2181,h2:2181,h3:2181/kafka
复制到h2 h3
h2
broker.id=1
h3
broker.id=2
创建目录
bin/zkCli.sh
create /kafka ''
启动3个kafka
bin/kafka-server-start.sh ./config/server.properties &

strom 安装类似。。。
http://shiyanjun.cn/archives/934.html
```




### Ambari 安装
```
cd /etc/yum.repos.d/
wget http://s3.amazonaws.com/dev.hortonworks.com/ambari/centos6/2.x/BUILDS/2.1.0-1409/ambaribn.repo
yum install ambari-server
ambari-server setup
ambari-server start
http://<ambari-server-host>:8080  admin admin
Ambari 重新初始化
ambari-server reset
ambari-server setup
报错: aused by: org.postgresql.util.PSQLException: FATAL: no pg_hba.conf entry for host "127.0.0.1", user "ambari", database "ambari", SSL off
	修改配置
	vi /var/lib/pgsql/data/pg_hba.conf
	末尾增加内容
	host	all	all	127.0.0.1/32	md5
	重启postgresql
	/etc/init.d/postgresql restart
```
