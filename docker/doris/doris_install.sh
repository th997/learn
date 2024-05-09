#!/bin/bash
set -x

# please modify dir !!!
dir_java=/d/soft/java
dir_install=/d/soft
dir_data=/e/data/$name

# sub dir
name=starrocks
#name=doris
dir_fe_meta=$dir_data/meta
dir_be_data=$dir_data/data
dir_log=$dir_data/log
mkdir -p $dir_fe_meta $dir_be_data $dir_log

# download
cd $dir_install
if [[ "$name" = "doris" && ! -d "$name" ]]; then
    wget https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.2-bin-x64.tar.gz
    tar -zxvf apache-doris-2.1.2-bin-x64.tar.gz
    ln -s apache-doris-2.1.2-bin-x64 $name
fi     

if [[ "$name" = "starrocks" && ! -d "$name" ]]; then
    wget https://releases.starrocks.io/starrocks/StarRocks-3.2.6.tar.gz
    tar -zxvf StarRocks-3.2.6.tar.gz
    ln -s StarRocks-3.2.6 $name
fi
cd $name
mkdir fe/log

# system config
# apt install openjdk-17-jdk
systemctl enable ntpd --now
if ! grep -q "vm.max_map_count=2000000" /etc/sysctl.conf ; then
    echo "vm.max_map_count=2000000">> /etc/sysctl.conf
fi
if ! grep -q "vm.overcommit_memory=1" /etc/sysctl.conf ; then
    echo "vm.overcommit_memory=1">> /etc/sysctl.conf
fi
if ! grep -q "vm.swappiness=0" /etc/sysctl.conf ; then
    echo "vm.swappiness=0">> /etc/sysctl.conf
fi
if ! grep -q "net.ipv4.tcp_abort_on_overflow=1" /etc/sysctl.conf ; then
    echo "net.ipv4.tcp_abort_on_overflow=1">> /etc/sysctl.conf
fi
if ! grep -q "net.core.somaxconn=1024" /etc/sysctl.conf ; then
    echo "net.core.somaxconn=1024">> /etc/sysctl.conf
fi
sysctl -p

# fe config
cat >> fe/conf/fe.conf <<EOF
JAVA_HOME=$dir_java
priority_networks=10.0.0.0/8
LOG_DIR=$dir_log
sys_log_dir=$dir_log
meta_dir=$dir_fe_meta
EOF

# be config
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
LimitNPROC=65535
LimitNOFILE=655350
LimitSTACK=infinity
LimitMEMLOCK=infinity
Environment="JAVA_HOME=$dir_java"
ExecStart=$dir_install/$name/fe/bin/start_fe.sh --daemon
ExecStop=$dir_install/$name/fe/bin/stop_fe.sh
Restart=always
RestartSec=60
Type=forking
[Install]
WantedBy=multi-user.target
EOF

# be service
cat > /etc/systemd/system/"$name"be.service <<EOF
[Unit]
After=network.target
[Service]
LimitNPROC=65535
LimitNOFILE=655350
LimitSTACK=infinity
LimitMEMLOCK=infinity
Environment="JAVA_HOME=$dir_java"
ExecStart=$dir_install/$name/be/bin/start_be.sh --daemon
ExecStop=$dir_install/$name/be/bin/stop_be.sh
Restart=always
RestartSec=60
Type=forking
[Install]
WantedBy=multi-user.target
EOF

# install
systemctl enable "$name"fe --now
systemctl enable "$name"be --now

# config
# mysql --connect-timeout 2 -h 10.10.10.88 -P9030 -uroot -e 'ALTER SYSTEM ADD BACKEND "10.10.10.88:9050";'
