## setting.xml 全局环境配置文件
```
localRepository：表示本地库的保存位置，也就是maven主要的jar保存位置，默认在${user.dir}/.m2/repository，如果需要另外设置，就换成其他的路径。
interactiveMode：如果Maven需要和用户交互以获得输入，则设置成true，反之则应为false。默认为true。
offline：如果不想每次编译，都去查找远程中心库，那就设置为true。当然前提是你已经下载了必须的依赖包。
usePluginRegistry：如果需要让Maven使用文件plugin-registry.xml来管理插件版本，则设为true。默认为false。
proxies: 多个proxy profile配置。当你在其它平台工作时，能方面切换。
servers: 一些需要验证的服务器配置，每个服务器都可以有不同配置。
mirros: 仓库的下载镜像。
profiles: 项目构建的配置信息，这里会有单独说明。
activeProfiles：激活的profile列表，按顺序生效。
pluginGroups: 如果插件groupId未指明，按该列表下的id去查找。
```
示例
``` xml
<localRepository>D:/Program/apache-maven-3.1.0/repository</localRepository>
<offline>true</offline>
<mirrors>
  <mirror>
    <name>aliyun-nexus</name>
    <mirrorOf>central</mirrorOf>
    <id>aliyun-nexus</id>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
 </mirror>
</mirrors>
<profile>
        <id>jdk-1.6</id>
        <activation>
            <activeByDefault>true</activeByDefault>
            <jdk>1.6</jdk>
        </activation>
        <properties>
            <maven.compiler.source>1.6</maven.compiler.source>
            <maven.compiler.target>1.6</maven.compiler.target>
            <maven.compiler.compilerVersion>1.6</maven.compiler.compilerVersion>
        </properties>
</profile>
```
## pom.xml 工程配置文件
```
modelversion :pom.xml使用的对象模型版本.
groupId :创建项目的组织或团体的唯一Id.
artifactId :项目的唯一Id,可视为项目名.
packaging :打包物的扩展名，一般有JAR,WAR,EAR等
version :产品的版本号.
name :项目的显示名，常用于Maven生成的文档。
url :组织的站点，常用于Maven生成的文档。
description :项目的描述，常用于Maven生成的文档。
properties :自定义属性
dependencies :依赖
```

## 命令
```
mvn -v 查看版本
mvn clean 清理
mvn validate : 验证项目是否正确以及相关信息是否可用。
mvn compile : 编译。
mvn test : 通过junit进行单元测试。
mvn package : 根据事先指定的格式（比如jar），进行打包。
mvn integration-test : 部署到运行环境中，准备进行集成测试。
mvn verify : 对包进行有效性性和质量检查。
mvn install : 安装到本地代码库。
mvn deploy : 在集成或发布环境，将包发布到远程代码库。
mvn archetype:generate  创建mvn项目
mvn jetty:run   运行项目于jetty上,
mvn site     生成项目相关信息的网站
mvn -Dwtpversion=1.0 eclipse:eclipse  生成Wtp插件的Web项目
mvn -Dwtpversion=1.0 eclipse:clean  清除Eclipse项目的配置信息(Web项目)
mvn eclipse:eclipse    将项目转化为Eclipse项目
mvn -e   显示详细错误 信息.
mvn archetype:create -DgroupId=th90 -DartifactId=th90.blog 创建普通项目
mvn archetype:create -DgroupId=th90 -DartifactId=th90.blog -DarchetypeArtifactId=maven-archetype-webapp 创建WEB项目
```

## 路径说明
```
${basedir} 存放 pom.xml和所有的子目录
${basedir}/src/main/java 项目的 java源代码
${basedir}/src/main/resources 项目的资源，比如说 property文件
${basedir}/src/main/webapp 网站文件
${basedir}/src/test/java 项目的测试类，比如说 JUnit代码
${basedir}/src/test/resources 测试使用的资源
```