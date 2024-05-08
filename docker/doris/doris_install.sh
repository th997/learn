#!/bin/bash

name=doris
# name=starrocks
dir_install=/d/soft
dir_java=/d/soft/java
dir_data=/e/data/$name

dir_fe_meta=$dir_data/meta
dir_be_data=$dir_data/data
dir_log=$dir_data/log

mkdir -p $dir_fe_meta $dir_be_data $dir_log

if ! grep -q "\* soft nofile 65535" /etc/security/limits.conf ; then
    echo "* soft nofile 65535">> /etc/security/limits.conf
    echo "* hard nofile 65535">> /etc/security/limits.conf
fi


if ! grep -q "vm.max_map_count=2000000" /etc/sysctl.conf ; then
    echo "vm.max_map_count=2000000">> /etc/sysctl.conf
fi

sysctl -w vm.max_map_count=2000000
ulimit -n 65535

systemctl enable ntpd --now

cd $dir_install
if [ "$name" = "doris" ]; then
    wget https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.2-bin-x64.tar.gz
    tar -zxvf apache-doris-2.1.2-bin-x64.tar.gz 
    ln -s apache-doris-2.1.2-bin-x64 $name
else 
    wget https://releases.starrocks.io/starrocks/StarRocks-3.2.6.tar.gz
    tar -zxvf StarRocks-3.2.6.tar.gz 
    ln -s StarRocks-3.2.6 $name
fi 

cd $name

# fe
cat >> fe/conf/fe.conf <<EOF
JAVA_HOME=$dir_java
priority_networks=10.0.0.0/8
LOG_DIR=$dir_log
sys_log_dir=$dir_log
meta_dir=$dir_fe_meta
EOF

# be
cat >> be/conf/be.conf <<EOF
JAVA_HOME=$dir_java
priority_networks=10.0.0.0/8
LOG_DIR=$dir_log
sys_log_dir=$dir_log
storage_root_path=$dir_be_data
disable_storage_page_cache=false
mem_limit=50%
EOF

# fe service
cat > /etc/systemd/system/"$name"fe.service <<EOF
[Unit]
After=network.target
[Service]
LimitNOFILE=65535
Environment="JAVA_HOME=$dir_java"
ExecStart=$dir_install/$name/fe/bin/start_fe.sh --daemon
ExecStop=$dir_install/$name/fe/bin/stop_fe.sh
Restart=always
RestartSec=60
Type=forking
[Install]
WantedBy=multi-user.target
EOF

systemctl enable "$name"fe --now

cat > /etc/systemd/system/"$name"be.service <<EOF
[Unit]
After=network.target
[Service]
LimitNOFILE=65535
Environment="JAVA_HOME=$dir_java"
ExecStart=$dir_install/$name/be/bin/start_be.sh --daemon
ExecStop=$dir_install/$name/be/bin/stop_be.sh
Restart=always
RestartSec=60
Type=forking
[Install]
WantedBy=multi-user.target
EOF

systemctl enable "$name"be --now

# mysql -h 127.0.0.1 -P9030 -uroot
# ALTER SYSTEM ADD BACKEND "10.10.10.88:9050"