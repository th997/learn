## termux
```
# 更新
apt update
apt upgrade

# 查看用户
id

# 安装ssh
pkg install termux-auth -y
passwd
pkg install openssh -y
sshd

# 查看ip
ip a

# 登录
ssh -p8022 xxx@xxx
```
https://www.sqlsec.com/2018/05/termux.html#toc-heading-148


## adb
```
adb start-server
adb kill-server
adb devices
adb logcat
adb shell cat /proc/cpuinfo
# 重装app , 保留数据
adb install -r baidu.apk
adb uninstall <package_name>
adb shell top
adb shell top -m 6
# 复制文件
adb push <local> <remote>
adb pull <remote>  <local>
# 进入shell
adb shell
adb -s  913151231ex shell
# 显示所有应用信息
adb shell pm list packages 
# 显示系统应用信息
adb shell pm list packages -s  
# 显示已经禁用的 
adb shell pm list packages -d  
# 显示启用的系统包
adb shell pm list packages -e -s
# 显示第三方应用信息
adb shell pm list packages -3   
# 显示安装来源
adb shell pm list packages -i | grep com.huawei.appmarket
# 查看应用信息
adb shell dumpsys package [package_name] 
# 输出包和包相关联的文件(安装路径)
adb shell pm list packages -f
# 查看指定进程名或者是进程 id 的内存信息
adb shell dumpsys meminfo [package_name/pid] 
查看指定包名应用的数据库存储信息(包括存储的sql语句)
adb shell dumpsys dbinfo [package_name] 
# 卸载应用
adb shell pm uninstall --user 0  com.sohu.sohuvideo.emplayer
# 查询权限
adb shell pm  list permissions -g
# 禁用某个包某项权限
adb shell pm revoke --user 0 com.alphainventor.filemanager android.permission.xx
# 查看包拥有的权限
adb shell pm dumpsys package com.alphainventor.filemanager  
# 查看拥有权限的包
adb shell dumpsys package | grep -E "(QUERY_ALL_PACKAGES)|(Package \[)" 
# 禁用activty，去广告。。
adb shell pm revoke --user 1 com.unionpay com.unionpay.activity.UPActivityStartTransition
adb shell pm grant --user 1 com.unionpay com.unionpay.activity.UPActivityStartTransition

```

## android 反编译 抓包
https://github.com/iBotPeaches/Apktool

https://ibotpeaches.github.io/Apktool/build/

java -jar apktool.jar d base.apk

https://cloud.tencent.com/developer/article/1700840
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
	<base-config cleartextTrafficPermitted="true" >
		<trust-anchors>
            <certificates src="system" overridePins="true" />
            <certificates src="user" overridePins="true" />
        </trust-anchors>
	</base-config>
</network-security-config>
```

java -jar apktool.jar b base -o base1.apk

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ~/project/key/mykey.jks -storepass 12345678a base1.apk mykey
