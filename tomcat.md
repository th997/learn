## Tomcat设置环境变量
```
Tomcat默认情况下会用系统的环境变量中找到JAVA_HOME和JRE_HOME。但是有的时候我们需要不同版本的JDK共存。
可以在${TOMCAT_HOME}/bin/setclasspath.bat最前面设置JAVA_HOME和JRE_HOME。
例如：
rem set user jdk and jre home
set JAVA_HOME=E:\javadev\jdk5
set JRE_HOME=E:\javadev\jdk5\jre
另外，有时需要设置Tomcat的最大内存，方法如下：
在catalina.bat的echo Using CATALINA_BASE:   %CATALINA_BASE%上一行中加入
rem set max meroy
set JAVA_OPTS=-Xms800m -Xmx1000m
```

## tomcat多个域名和虚拟目录
```
/etc/hosts
127.0.0.1 localhost
127.0.0.1 localhost1
server.xml:
    <Host name="localhost"  appBase="webapps"
          unpackWARs="true" autoDeploy="true"
          xmlValidation="false" xmlNamespaceAware="false">
<Context path="" docBase="/usr/local/bin/apache-tomcat-6.0.36/webapps/test1" reloadable="true" crossContext="true"/>

    </Host>
    <Host name="localhost1"  appBase="webapps"
          unpackWARs="true" autoDeploy="true"
          xmlValidation="false" xmlNamespaceAware="false">
<Context path="" docBase="/usr/local/bin/apache-tomcat-6.0.36/webapps/test2" reloadable="true" crossContext="true"/>
```

## 允许所有IP访问80端口
iptables -A INPUT -p tcp -s 0/0 –dport 80 -j ACCEPT


## 远程管理
```
<role rolename="manager-gui"/>
<user username="ecc" password="ecc_test" roles="manager-gui"/>
```

## 优化
```
	<Executor name="tomcatThreadPool" namePrefix="catalina-exec-"
        maxThreads="2000" minSpareThreads="30"/>

    <Connector executor="tomcatThreadPool"  port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" URIEncoding="UTF-8" acceptCount="10000" />

set JAVA_OPTS= -Xmx2048M -Xms512M -XX:MaxPermSize=256m
linux
catalina.sh 最前
JAVA_OPTS="-Xmx1024M -Xms512M"

调优参数
http://tomcat.apache.org/tomcat-8.0-doc/config/http.html
Executor
maxThreads="3000" minSpareThreads="100" maxIdleTime="600000"
Connector
URIEncoding="UTF-8" acceptorThreadCount="2" acceptCount="15000"

压测
mkdir -m 644 -p /usr/local/man/man1
wget http://blog.s135.com/soft/linux/webbench/webbench-1.5.tar.gz
tar zxvf webbench-1.5.tar.gz
cd webbench-1.5
make && make install
//yum install httpd-tools
webbench -c 3000 -t 20 http://10.252.112.179:8000/examples/servlets/servlet/HelloWorldExample
webbench -c 3000 -t 20 http://10.252.112.179:8000/examples/jsp/jsp2/el/basic-arithmetic.jsp


```

## 权限
```
 <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="::1|0:0:0:0:0:0:0:1|127\.0\.0\.1|192\.168\.\d+\.\d+|10\.\d+\.\d+\.\d+"/>
 <Valve className="org.apache.catalina.valves.RemoteAddrValve" allow="::1|0:0:0:0:0:0:0:1|127\.0\.0\.1|192\.168\.1\.\d+|10\.\d+\.\d+\.\d+"/>
```

