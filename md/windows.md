## windows 无线共享
1.创建一个虚拟网络,用于共享到其他设备(mynet,mykey123456可自己修改)

netsh wlan set hostednetwork mode=allow ssid=mynet key=12345678

2.将你真正使用能够联网的那个连接(本地连接或者宽带连接)共享到前一步新增的虚拟网络连接

3.启动虚拟网络

netsh wlan start hostednetwork

4.其他设备可以搜索并连接到刚刚创建的网络了

5.关闭虚拟网络

netsh wlan stop hostednetwork

## WIN拥有管理员权限的使用方法：

1.右键单击“计算机”，进入“管理”找到“用户和组”

2.找到administrators，右键调出属性，把“该账户已禁用”前面的勾去掉。

3.新建“记事本”,copy 如下内容：
```
Windows Registry Editor Version 5.00


[HKEY_CLASSES_ROOT\*\shell\runas]
@="管理员取得所有权"
"NoWorkingDirectory"=""

[HKEY_CLASSES_ROOT\*\shell\runas\command]
@="cmd.exe /c takeown /f \"%1\" && icacls \"%1\" /grant administrators:F"
"IsolatedCommand"="cmd.exe /c takeown /f \"%1\" && icacls \"%1\" /grant administrators:F"

[HKEY_CLASSES_ROOT\exefile\shell\runas2]
@="管理员取得所有权"
"NoWorkingDirectory"=""

[HKEY_CLASSES_ROOT\exefile\shell\runas2\command]
@="cmd.exe /c takeown /f \"%1\" && icacls \"%1\" /grant administrators:F"
"IsolatedCommand"="cmd.exe /c takeown /f \"%1\" && icacls \"%1\" /grant administrators:F"

[HKEY_CLASSES_ROOT\Directory\shell\runas]
@="管理员取得所有权"
"NoWorkingDirectory"=""

[HKEY_CLASSES_ROOT\Directory\shell\runas\command]
@="cmd.exe /c takeown /f \"%1\" /r /d y && icacls \"%1\" /grant administrators:F /t"
"IsolatedCommand"="cmd.exe /c takeown /f \"%1\" /r /d y && icacls \"%1\" /grant administrators:F /t"
```

OK，另存为随便起名，后缀必须是【 .reg】

win7获取管理员权限.reg

双击该注册表导入即可


## windows 端口映射
```
清除所有端口映射
netsh interface portproxy reset
添加
netsh interface portproxy add v4tov4 listenport=新开的监听端口 listenaddress=新开端口的绑定地址 connectaddress=要转发的地址 connectport=要转发的端口 protocol=tcp
netsh interface portproxy add v4tov4 listenport=1234 listenaddress=127.0.0.1 connectaddress=192.168.8.4 connectport=9000 protocol=tcp
netsh interface portproxy add v4tov4 listenport=1235 connectaddress=192.168.8.4 connectport=9000 protocol=tcp
显示
netsh interface portproxy show all
删除
netsh interface portproxy delete v4tov4 listenport=新开的监听端口 listenaddress=新开端口的绑定地址
netsh interface portproxy delete v4tov4 listenport=1234 listenaddress=192.168.0.4
```


## 修复磁盘错误
chkdsk /f D:

## 创建软链接
mklink /d d:\test D:\test1

## 删除软连接
rmdir 

## 映射为磁盘
subst X: G:\project\nginx_dev
subst /d X:

## widnows MTU 查看与设置
netsh interface ipv4 show subinterfaces

netsh interface ipv4 set subinterface "本地连接" mtu=1480 store=persistent


## windows 开机启动项
C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp

## 添加路由
route delete 104.224.153.84
route add 104.224.153.84 MASK 255.255.255.255  192.168.1.1 METRIC 3 -p
route change 104.224.153.84 MASK 255.255.255.255  192.168.1.1 METRIC 3 -p

##　磁盘性能查看
winsat disk

## bat
```
@echo off
set java=C:\ecc\Java\jre8\bin\javaw.exe
set curdir=%~dp0
cd /d %curdir%
if exist %java% (
start %java% -Dfile.encoding=UTF-8 -server -Xmx1024m -Xms1024m -jar test.jar
) 
exit
```
## widows store 代理
netsh winhttp import proxy source=ie


