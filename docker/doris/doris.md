# doris 

## 系统配置
vim /etc/security/limits.conf 
```
* soft nofile 65535
* hard nofile 65535
```

vim /etc/sysctl.conf
```
vm.max_map_count=2000000
```

```shell
systemctl enable ntpd --now
sysctl -w vm.max_map_count=2000000
ulimit -n 65535
```

## 下载
wget https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.0-bin-x64.tar.gz
tar -zxvf apache-doris-2.1.0-bin-x64.tar.gz 
ln -s apache-doris-2.1.0-bin-x64 doris
cd doris

## 配置
```shell
# 新建目录
mkdir -p /e/doris/doris-meta /e/doris/doris-data /e/doris/logs

# fe配置
cat >> fe/conf/fe.conf <<EOF
JAVA_HOME=/d/soft/java
priority_networks=10.0.0.0/8
LOG_DIR=/e/doris/logs
sys_log_dir=/e/doris/logs
meta_dir=/e/doris/doris-meta
EOF

# be配置
cat >> be/conf/be.conf <<EOF
JAVA_HOME=/d/soft/java
priority_networks=10.0.0.0/8
LOG_DIR=/e/doris/logs
sys_log_dir=/e/doris/logs
storage_root_path=/e/doris/doris-data
disable_storage_page_cache=false
mem_limit=50%
EOF

# 启动fe
fe/bin/start_fe.sh --daemon
# 检测fe是否正常
curl http://127.0.0.1:8030/api/bootstrap

# 启动be
be/bin/start_be.sh --daemon

# 查看日志
tail -f /e/doris/logs/*

# 登录fe
mysql -uroot -P9030

# 停止
fe/bin/stop_fe.sh
be/bin/stop_be.sh

# 开机启动
cat > /etc/systemd/system/doris_fe.service <<EOF
[Unit]
After=network.target
[Service]
LimitNOFILE=65535
Environment="JAVA_HOME=/d/soft/java"
ExecStart=/d/soft/fe/bin/start_fe.sh --daemon
ExecStop=/d/soft/fe/bin/stop_fe.sh
Restart=always
RestartSec=60
Type=forking
[Install]
WantedBy=multi-user.target
EOF

systemctl enable doris_fe --now

```

## 维护
```sql
-- 添加be
ALTER SYSTEM ADD BACKEND "be1:9050";
-- 查看be
SHOW BACKENDS;
-- 删除be
ALTER SYSTEM DROPP BACKEND "10067";
-- 添加fe
ALTER SYSTEM ADD OBSERVER "fe1:9010";
-- 查看fe
SHOW FRONTENDS;
-- 查看fe配置
SHOW FRONTEND CONFIG;
-- 设置查询缓存
set global enable_query_cache = true 
-- 修改fe配置，重启生效
ADMIN SET FRONTEND CONFIG ("fe_config_name" = "fe_config_value");
-- 修改be配置
-- curl -X POST http://{be_ip}:{be_http_port}/api/update_config?{key}={value}\&persist=true
-- 查看be配置
-- http://be_host:be_webserver_port/varz   http://localhost:9040/varz

```