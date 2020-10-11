# RabbitMQ

## rabbitmq 简易安装. 源码安装方式自行google.
wget http://mirrors.ustc.edu.cn/fedora/epel/epel-release-latest-6.noarch.rpm

rpm -ivh epel-release-latest-6.noarch.rpm

yum install rabbitmq-server -y

## 服务启动
service rabbitmq-server restart

## 查看激活管理插件,15672端口http访问需要防火墙放行
/usr/lib/rabbitmq/bin/rabbitmq-plugins list

/usr/lib/rabbitmq/bin/rabbitmq-plugins enable rabbitmq_management

## 创建管理员用户，负责整个MQ的运维，例如：
rabbitmqctl add_user  user_admin  passwd_admin

rabbitmqctl set_user_tags user_admin administrator

## 创建RabbitMQ监控用户，负责整个MQ的监控，例如：
rabbitmqctl add_user  user_monitoring  passwd_monitor

rabbitmqctl set_user_tags user_monitoring monitoring

## 创建某个项目的专用用户，只能访问项目自己的virtual hosts
rabbitmqctl  add_user  user_proj  passwd_proj

rabbitmqctl set_user_tags user_proj management

rabbitmqctl set_permissions -p /  ecc '.*' '.*' '.*'

## 删除用户
rabbitmqctl delete_user username

## 修改密码
rabbitmqctl change_password username newpassword

## 列出所有用户
rabbitmqctl list_users

## 访问管理页面
http://localhost:15672/


## 管理权限
1. 创建虚拟主机
rabbitmqctl add_vhost vhostpath
2. 删除虚拟主机
rabbitmqctl delete_vhost vhostpath
3. 列出所有虚拟主机
rabbitmqctl list_vhosts
4. 设置用户权限
rabbitmqctl set_permissions [-p vhostpath] username regexp regexp regexp
5. 清除用户权限
rabbitmqctl clear_permissions [-p vhostpath] username
6. 列出虚拟主机上的所有权限
rabbitmqctl list_permissions [-p vhostpath]
7. 列出用户权限
rabbitmqctl list_user_permissions username
