nginx简易安装与配置
#参考网址: http://nginx.org/en/download.html
#参考网址: http://nginx.org/en/docs/
wget http://nginx.org/packages/centos/6/noarch/RPMS/nginx-release-centos-6-0.el6.ngx.noarch.rpm
rpm -ivh nginx-release-centos-6-0.el6.ngx.noarch.rpm
yum install nginx
service nginx start
#默认目录结构 /etc/nginx
conf.d(目录,内有用户配置)   koi-utf  mime.types  scgi_params   win-utf
fastcgi_params  koi-win     nginx.conf(主配置文件)  uwsgi_params
cd conf.d
cp default.conf default.conf.bak
vim default.conf
#将
    location ~ \.php$ {
        proxy_pass   http://127.0.0.1;
    }
#去掉注释修改为
    location ~ \.jsp$ {
        proxy_pass   http://127.0.0.1:8080;
    }
#由此jsp请求便会发到 http://127.0.0.1:8080;



rpm -Uvh http://mirror.webtatic.com/yum/el6/latest.rpm

yum install nginx16

#开机启动
chkconfig nginx on

#https
1.
yum install openssl
yum install openssl-devel
2.
# 自签名证书 https://blog.csdn.net/weixin_30531261/article/details/80891360
openssl genrsa -out server.key 2048
openssl req -new -x509 -key server.key -out server.pem -days 3650
openssl x509 -in server.pem -inform pem -outform der -out server.crt

server {
    listen 443 ssl;
    ssl_certificate  /usr/local/nginx/conf/server.crt;
    ssl_certificate_key  /usr/local/nginx/conf/server_nopwd.key;
}
rpm 安装包
http://nginx.org/packages/mainline/centos/6/x86_64/RPMS/

负载均衡
nginx.conf: http 下
 upstream myapp1 {
        server 192.168.0.195:8000;
        server 192.168.0.196:8000;
	    server 192.168.0.197:8000;
    }

    server {
        listen 10000;

        location / {
            proxy_pass http://myapp1;
			proxy_next_upstream http_404;
			proxy_read_timeout 300;
        }
    }

HTTP Basic Authentication
location 下加入:
auth_basic "Password please";
auth_basic_user_file  /mnt/soft/htpasswd;
生成密码文件
wget http://trac.edgewall.org/export/10770/trunk/contrib/htpasswd.py
chmod 0755 htpasswd.py
./htpasswd.py -c -b htpasswd ecc aguiuwe2314

TCP代理

stream {
    server {
        listen 3306;
        proxy_pass db;
    }
    upstream db {
        server db1:3306;
        server db2:3306;
        server db3:3306;
    }

	server {
        listen 8000;
        proxy_pass test.test.com:8000;
    }
}

haproxy
listen mysql
    bind  *:3308
    mode  tcp
    option tcplog
    server  s1 10.162.55.49:3308

tar 安装
apt-get install libpcre3 libpcre3-dev
apt-get install zlib1g-dev
yum -y install pcre-devel openssl openssl-devel
./configure --with-stream --with-http_stub_status_module --with-http_ssl_module
make
make install

# 创建软链接
ln -s /usr/local/nginx/sbin/nginx /usr/sbin/nginx
ln -s /usr/local/nginx/conf/ /etc/nginx/conf

##端口映射 rinetd
# 下载
wget http://www.boutell.com/rinetd/http/rinetd.tar.gz
tar xf rinetd.tar.gz

# 安装
cd rinetd
sed -i 's/65536/65535/g' rinetd.c
mkdir /usr/man
make
make install

# 配置
cat << EOF >> /etc/rinetd.conf
0.0.0.0 80 192.168.8.66 8080
EOF

# 运行
启动 /usr/sbin/rinetd
检查 ps -elf | grep rinetd
停止 pkill rinetd
重启
pkill rinetd
/usr/sbin/rinetd


端口映射
	SRC_HOST=192.168.8.165
	SRC_PORT=2000
	DEST_HOST=192.168.8.66
	DEST_PORT=8000
	iptables -t nat -A PREROUTING -p tcp --dport $SRC_PORT -j DNAT --to-destination $DEST_HOST:$DEST_PORT
	iptables -t nat -A POSTROUTING -p tcp -s $DEST_HOST --sport $DEST_PORT -j SNAT --to-source $SRC_HOST

	iptables -t nat -A PREROUTING -p tcp --dport 2000 -j DNAT --to-destination 192.168.8.66:8000
	iptables -t nat -A POSTROUTING -p tcp -s 192.168.8.66 --sport 8000 -j SNAT --to-source 192.168.8.165
	测试通过


nginx 默认60秒超时 重试
http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_read_timeout
http://nginx.org/en/docs/http/ngx_http_proxy_module.html#proxy_next_upstream


server {
    listen       80;
    server_name  vip.esayget.com;
    location / {
	    if ( $host = "vip.test.com" ) {
          rewrite  (.*) http://vip.test.com/ecc_ecmw$1;
	   }
           if ( $host = "nm.test.com" ) {
          rewrite  (.*) http://nm.test.com/netmanager$1;
	   }
	  if ( $host = "ecbos.test.com" ) {
          rewrite  (.*) http://ecbos.test.com/ecc_ecbos$1;
	   }
    }
    location /ecc_ecmw {
        proxy_pass http://localhost:8000/ecc_ecmw;
        proxy_set_header Host $host;
        proxy_set_header X-Real-Ip $remote_addr;
 	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    location /netmanager {
        proxy_pass http://localhost:8000/netmanager;
        proxy_set_header Host $host;
        proxy_set_header X-Real-Ip $remote_addr;
 	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    location /ecc_ecbos {
        proxy_pass http://localhost:8000/ecc_ecbos;
        proxy_set_header Host $host;
        proxy_set_header X-Real-Ip $remote_addr;
 	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

}