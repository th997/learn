## Centos搭建Svn服务器
环境:Centos6.3 x64 安装svn版本1.6

## 1.安装
yum install subversion
svnversion --version

## 2.建库
mkdir /usr/mydata/svn
svnadmin create /usr/mydata/svn

## 3.配置
```
vi /usr/mydata/svn/conf/passwd
末尾添加:
test=123123
vi /usr/mydata/svn/conf/authz
末尾添加:
test=rw
vi /usr/mydata/svn/conf/svnserve.conf
末尾添加:
anon-access = none
auth-access = write
password-db = password
authz-db = authz
realm = /usr/mydata/svn
```
## 4.启动
svnserve --help
svnserve -d -r /usr/mydata/svn/

## 5.连接地址
svn://localhost
