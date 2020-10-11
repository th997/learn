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
# 显示第三方应用信息
adb shell pm list packages -3   
# 查看应用信息
adb shell dumpsys package [package_name] 
# 查看指定进程名或者是进程 id 的内存信息
adb shell dumpsys meminfo [package_name/pid] 
查看指定包名应用的数据库存储信息(包括存储的sql语句)
adb shell dumpsys dbinfo [package_name] 

```

## 反编译
https://blog.csdn.net/weixin_41454168/article/details/105406874

https://www.jianshu.com/p/79910362a0e0

java -jar apktool.jar d link_cn.apk -o link1 --only-main-classes --use-aapt2

java -jar apktool.jar b link1 -o xxx_new.apk --only-main-classes --use-aapt2


## 模拟器
https://docs.anbox.io/userguide/install.html

xDroid 


## ssl 中间人
https://www.wangan.com/articles/149


## android 修改xml
unzip -d cn xxx.apk 
wget https://github.com/hzw1199/xml2axml/releases/download/1.1.0/xml2axml-1.1.0-SNAPSHOT.jar
mv xml2axml-1.1.0-SNAPSHOT.jar xml2axml.jar
jar -jar xml2axml.jar d cn/AndroidManifest.xml cn/AndroidManifest_un.xml
vim cn/AndroidManifest_un.xml
jar -jar xml2axml.jar e cn/AndroidManifest_un.xml cn/AndroidManifest.xml
zip -q -r xxx_new.apk ./cn/*
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ~/project/key/mykey.jks -storepass Z9thththa xxx_new.apk mykey


https://juejin.im/post/6844903831579394055

java -jar ./ManifestEditor.jar ./link_cn.apk -o ./new_build.apk -d 1 -s

https://github.com/iamyours/ApkCrack