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
disable_storage_page_cache=true
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
alter system add backend "be1:9050";
-- 查看be
show backends;
-- 删除be
alter system drop backend "10067";
-- 添加fe
alter system add observer "fe1:9010";
-- 添加fe
alter system add follower "fe1:9010";
-- 查看fe
show frontends;
-- 修改表副本数
alter table db.tb set ("default.replication_num" = "3");
-- 授权
create user 'user1'@'10.%' identified by '13pgu9assk8';
grant select on *.* to 'user1'@'10.%';
flush privileges;
-- 查看be日志
select * from information_schema.be_logs
```

## 调优
```
-- 设置查询缓存 (新连接生效,持久保持不需要重启),查看 show variables like '%time_zone%';
set global enable_query_cache = true;
set global enable_scan_datacache = true;
-- set global enable_profile = true;
set global big_query_profile_threshold = '2s';
set global query_cache_entry_max_bytes=16777216;
set global query_cache_entry_max_rows=1638400;

-- fe参数修改（重启失效），查看 show frontend config;
admin set frontend config ("expr_children_limit" = "200000");
admin set frontend config ("enable_fast_schema_evolution" = "true");
admin set frontend config ("tablet_create_timeout_second"="20");
admin set frontend config("lake_enable_batch_publish_version"="true");

-- be参数修改，（重启失效），查看 select * from information_schema.be_configs where name like 'update_compaction_num_threads_per_disk';
update information_schema.be_configs set value = 4 where name like 'update_compaction_num_threads_per_disk';
update information_schema.be_configs set value = 5 where name like 'update_compaction_check_interval_seconds';
update information_schema.be_configs set value =10 where name like 'update_compaction_per_tablet_min_interval_seconds';
update information_schema.be_configs set value = 4 where name like 'number_tablet_writer_threads';
update information_schema.be_configs set value = 60 where name like 'update_cache_expire_sec';
update information_schema.be_configs set value = 33554432 where name like 'l0_max_mem_usage';
```
