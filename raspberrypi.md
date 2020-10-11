# ssh
```
sudo apt-get install chkconfig
sudo systemctl enable ssh
sudo chkconfig ssh on
```
# vnc1
```
sudo apt-get update
sudo apt-get install realvnc-vnc-server
sudo raspi-config
Interfacing Options -> VNC > Yes
vnc host:5900
```

# vnc2
```
sudo apt-get install tightvncserver
vncserver :1
... 设置密码
```
vi /etc/init.d/tightvncserver
```
#!/bin/sh
### BEGIN INIT INFO
# Provides: tightvncserver
# Required-Start: $syslog $remote_fs $network
# Required-Stop: $syslog $remote_fs $network
# Default-Start: 2 3 4 5
# Default-Stop: 0 1 6
# Short-Description: Starts VNC Server on system start.
# Description: Starts tight VNC Server. Script written by James Swineson.
### END INIT INFO

# /etc/init.d/tightvncserver
VNCUSER='pi'
case "$1" in
 start)
 su $VNCUSER -c '/usr/bin/tightvncserver :1'
 echo "Starting TightVNC Server for $VNCUSER"
 ;;
 stop)
 pkill Xtightvnc
 echo "TightVNC Server stopped"
 ;;
 *)
 echo "Usage: /etc/init.d/tightvncserver {start|stop}"
 exit 1
 ;;
esac
exit 0
```
```
sudo chmod 755 /etc/init.d/tightvncserver
sudo update-rc.d tightvncserver defaults
sudo chkconfig tightvncserver on
```

# source
```
sudo cp /etc/apt/sources.list /etc/apt/sources.list.backup
sudo vi /etc/apt/sources.list
deb http://mirrors.aliyun.com/raspbian/raspbian/ jessie main contrib non-free rpi firmware
deb-src http://mirrors.aliyun.com/raspbian/raspbian/ jessie main contrib non-free rpi firmware

```

# config
sudo raspi-config
sudo apt install git-core -y

# 分辨率
```
sudo vim /boot/config.txt
hdmi_drive=2
hdmi_group=2
hdmi_mode=16

```
# vnc 分辨率
```
sudo raspberry-config
advanced options -> resolution
```

# 摄像头
```
sudo raspi-config
Enable Camera
拍照（2s后）
raspistill -o temp.jpg -t 2000
```
