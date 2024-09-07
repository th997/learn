#!/bin/bash
set -x

name=starrocks
#name=doris

# please modify !!!
fe_ip="10.10.10.106"
dir_java=/d/soft/java
dir_install=/d/soft
dir_data=/e/data/$name
dir_log=$dir_data/log

# sub dir
dir_fe_meta=$dir_data/meta
dir_be_data=$dir_data/data
mkdir -p $dir_fe_meta $dir_be_data $dir_log

# ip addr
network_prefix=$(echo $fe_ip | cut -d '.' -f 1)
all_ips=$(hostname -I)
local_ip=''
for ip in $all_ips; do
    if [[ $ip =~ ^$network_prefix\.[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        local_ip=$ip
        break
    fi
done
local_net="$network_prefix.0.0.0/8"

# download
cd $dir_install
if [[ "$name" = "doris" && ! -d "$name" ]]; then
    wget https://apache-doris-releases.oss-accelerate.aliyuncs.com/apache-doris-2.1.2-bin-x64.tar.gz
    tar -zxvf apache-doris-2.1.2-bin-x64.tar.gz
    ln -s apache-doris-2.1.2-bin-x64 $name
fi     

if [[ "$name" = "starrocks" && ! -d "$name" ]]; then
    # wget https://releases.starrocks.io/starrocks/StarRocks-3.2.6.tar.gz
    # tar -zxvf StarRocks-3.2.6.tar.gz
    # ln -s StarRocks-3.2.6 $name
    wget https://releases.starrocks.io/starrocks/StarRocks-3.3.0.tar.gz
    tar -zxvf StarRocks-3.3.0.tar.gz
    ln -s StarRocks-3.3.0 $name
fi
cd $name
mkdir fe/log

# jdk
# if [ ! -d "/usr/local/jdk17"]; then
#     wget https://corretto.aws/downloads/latest/amazon-corretto-17-x64-linux-jdk.tar.gz 
#     tar -zxvf amazon-corretto-17*.tar.gz /usr/local
#     mv /usr/local/amazon-corretto-17* /user/local/jdk17
# fi    

# or apt install openjdk-17-jdk

# system config

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
sed -i "/^LOG_DIR/c\LOG_DIR=$dir_log" fe/conf/fe.conf
sed  -i '/^priority_networks/,$d' fe/conf/fe.conf
cat >> fe/conf/fe.conf <<EOF
priority_networks=$local_net
sys_log_dir=$dir_log
meta_dir=$dir_fe_meta
EOF

# be config
sed  -i '/^priority_networks/,$d' be/conf/be.conf
cat >> be/conf/be.conf <<EOF
priority_networks=$local_net
sys_log_dir=$dir_log
storage_root_path=$dir_be_data,medium:ssd
disable_storage_page_cache=false
mem_limit=60%
EOF

# fe service
#chown -R service:service $dir_data $dir_log $dir_install/$name/
fe_start_exec="$dir_install/$name/fe/bin/start_fe.sh --daemon --helper '$fe_ip:9010'"
if [[ "$fe_ip" = "$local_ip" ]]; then
    fe_start_exec="$dir_install/$name/fe/bin/start_fe.sh --daemon"
fi
cat > /etc/systemd/system/"$name"fe.service <<EOF
[Unit]
After=network.target
[Service]
#User=service
#Group=service
LimitNPROC=65535
LimitNOFILE=655350
LimitSTACK=infinity
LimitMEMLOCK=infinity
Environment="JAVA_HOME=$dir_java"
ExecStart=$fe_start_exec
ExecStop=$dir_install/$name/fe/bin/stop_fe.sh
Restart=always
RestartSec=10
Type=forking
[Install]
WantedBy=multi-user.target
EOF

# be service
cat > /etc/systemd/system/"$name"be.service <<EOF
[Unit]
After=network.target
[Service]
#User=service
#Group=service
LimitNPROC=65535
LimitNOFILE=655350
LimitSTACK=infinity
LimitMEMLOCK=infinity
Environment="JAVA_HOME=$dir_java"
ExecStart=$dir_install/$name/be/bin/start_be.sh --daemon
ExecStop=$dir_install/$name/be/bin/stop_be.sh
Restart=always
RestartSec=10
Type=forking
[Install]
WantedBy=multi-user.target
EOF

# install
systemctl enable "$name"fe --now
systemctl enable "$name"be --now

# config
mysql --connect-timeout 15 -h $fe_ip -P9030 -uroot -e "ALTER SYSTEM ADD BACKEND '$local_ip:9050';"
