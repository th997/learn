i. 安装ADT
  http://developer.android.com/sdk/installing/installing-adt.html
  https://dl.google.com/android/ADT-23.0.3.zip
i. 下载SDK
i. new a android virtual device
i. 安装卸载
  adb install d:/test.apk
  adb uninstall com.app.test
i. 目录分析
  src 源码目录
  res 资源目录
      drawable 图片资源
	  layout 界面文件,类似于网页html
	  values 应用数据,文字部分
  AndroidMainfest.xml 项目清单文件,列举出应用所提供的功能和需要的权限等
  assets 资源文件,不会生成ID,使用时需指明路径
  default.properties 项目环境信息,一般不需要修改
  gen 开发工具自动生成的
i. AndroidMainfest.xml
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.android.hellospellchecker"
    android:versionCode="1"
    android:versionName="1.0" >
    <uses-sdk android:minSdkVersion="14" />
	<!--最低SDK版本-->
    <application
        android:label="@string/app_name" >
        <activity
            android:label="@string/app_name"
            android:name=".HelloSpellCheckerActivity" >
			<!-- android:name=".HelloSpellCheckerActivity"
			     android:name="HelloSpellCheckerActivity"
				 android:name=".module.HelloSpellCheckerActivity"-->
            <intent-filter >
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
			<!--应用列表中显示,只能一个-->
            </intent-filter>
        </activity>
    </application>
  </manifest>
i. .java-->.class-->.dex--dx->打包(签名)-->.apk
i. 布局
  LinearLayout(线性布局):垂直(vertical),水平()
  RelativeLayout(相对布局)
  TableLayout(表格布局)
  FrameLayout(帧布局)
i. 显示单位
  px(pixels) 像素
  dip或dp 独立像素
  sp 比例像素
  in(inches) 英寸
  mm(millimeters)毫米
  pt(points)点 1/72英寸
  推荐使用dip 文字sp
i. 通知方式: Toast,对话框,通知栏


android sdk 国内镜像
mirrors.neusoft.edu.cn 80
add-on sites
http://dl-ssl.google.com/android/repository/addon.xml
http://dl-ssl.google.com/android/repository/addons_list.xml
http://dl-ssl.google.com/android/repository/repository.xml

//
文件权限
位置0 -文件 d目录
位置1-3  当前用户
位置4-6  当前用户所在组
位置7-9  其他用户
r可读 w可写 x可执行
rw- 4+2+0=6
chmod 0666 public.txt 修改权限为 其他用户可读写

activity 生命周期
oncreate activity创建
onstart 当activity变成用户可见的时候调用
onresume 界面获取焦点的时候调用
onpause 界面失去焦点
onstop 界面不可见
ondestroy activity销毁,app退出

activity: 与用户交互的界面UI
service: 后台长期运行的组件
receiver: 接收系统自带的广播
content provider: 提供数据 把一个应用程序私有的数据库暴漏给别的应用

应用程序与进程:
应用程序是一组组件的结合
进程是运行这些组件的载体

任务栈: task stack->只针对activity而言
一个重要的作用:用来维护界面(activity)体验

singleTop 栈顶唯一
singleTask 栈内唯一
singleInstance 唯一实例,独立栈

系统目录结构
/data
 /system




工具箱:
db, 权限管理,流量监控,短信,进程管理,root,相机声音,gps跟踪

NavigationDrawer


android studio 目录下 idea.properties 添加一行,防止启动时更新
disable.android.first.run=true

自定义控件




#Android目录结构
* data
	* app：用户安装的应用
	* data：应用的专属文件夹
	* system：系统的配置信息，注册表文件
	* anr：anr异常的记录信息

* dev：devices的缩写
	* 存放设备所对应的文件

* mnt：mount的缩写
	* 挂载在系统上的设备：sdcard，u盘

* proc：硬件配置，状态信息
	* cpuinfo、meminfo

* sbin：system bin
	* 系统重要的二进制执行文件
	* adbd：服务器的adb进程

* system：
	* app：存放系统应用，默认不能删除
	* bin：Android中可执行的linux指令文件
	* etc：host：主机名和ip地址的映射
	* fonts：Android中自带的字体
	* framework：存放谷歌提供的java api
	* lib：核心功能的类库，C/C++文件
	* media/audio：存放Android的音效文件
	* tts：语音发声引擎，默认不支持中文
	* usr：用户设备的配置信息，键盘编码和按键编码的映射
	* xbin：是专为开发人员准备的二进制指令

#Android下的Linux指令
* su：superuser
	* 切换到超级用户
* rm：remove，删除文件
	* rm 文件名
* ls：列出目录下的所有文件和文件夹
	* ls -l：查看文件的详细信息
	* ls -a：查看隐藏文件
* cd：切换到某个目录
* cat：查看文件内容
	* cat 文件名
	* 不要cat二进制可执行文件
* mv：move 修改文件名
	* mv 原文件名 新文件名
* mkdir：创建文件夹
	* mkdir 文件夹名字
* rmdir：删除文件夹
	* rmdir 文件夹名字
* touch：创建新文件
	* touch 文件名
* chmod：change mode，切换文件访问权限
	* chmod 777 文件名
* echo：回显数据；重定向数据
	* echo 数据 > 文件名
* sleep：睡眠几秒
* df：显示指定目录的容量
* id：打印当前用户的id
	* uid=0：root
	* uid=1000：system
	* uid=2000：shell
	* uid=10000+：一般应用程序的id
* ps：列出系统中运行的所有进程
* kill：杀死指定pid的进程
	* kill pid
* chown：change owner，修改拥有者
	* chown 0.0 文件名
* mount：挂载文件系统
	* mount -o remount rw /：挂载当前目录为可读可写权限
	* mount -o remount rw /system：重新挂载指定目录

# Android中特有的指令
* am：ActivityManager，可以进行跟activity相关的操作
	* am start -n com.itheima.createfile/com.itheima.createfile.MainActivity：开启指定Activity
	* am kill com.itheima.createfile：结束非前台进程
	* am force-stop com.itheima.createfile：结束进程

* pm：PackageManager
	* pm disable 包名：冻结指定应用
	* pm enable 包名：解冻指定应用

* monkey -p com.itheima.createfile 1000：自动点击指定应用1000次

---
#刷模拟器，rom写文件（su）
* 如果想让真实手机运行这些指令，手机必须要有root权限
* 刷root原理：把su二进制文件拷贝到/system/bin或者/system/xbin
* Android刷root软件，工作的原理全部都是利用系统的漏洞实现
* rom：可以理解为android系统的安装文件
* 把su文件和superuser.apk写入img文件
* 执行su指令

		Runtime.getRuntime().exec("su");

---
#小案例：冻结解冻应用
* 冻结和解冻指定的应用
*
		RootTools.sendShell("pm disable " + package, 300000);
		RootTools.sendShell("pm enable " + package, 300000);

---
#小案例：零权限读取用户隐私数据
* 直接修改短信数据库访问权限

		RootTools.sendShell("chmod 777 data/data/com.android.providers.telephony/databases/mmssms.db", 300000);
		SQLiteDatabase db = SQLiteDatabase.openDatabase("data/data/com.android.providers.telephony/databases/mmssms.db", null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("sms", new String[]{"body", "address"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			String body = cursor.getString(0);
			String address = cursor.getString(1);
			System.out.println(body + ";" + address);
		}
		RootTools.sendShell("chmod 660 data/data/com.android.providers.telephony/databases/mmssms.db", 300000);

---
#静默安装
* 为什么有静默安装的需求
	1. 正规应用。电子市场，方便用户静默安装
	2. 流氓软件。后台偷偷下载安装。
* 自动下载应用，然后静默安装

		//静默安装
		RootTools.sendShell("pm install sdcard/flowstat.apk", 30000);
		//打开
		RootTools.sendShell("am start -n com.jijian.flowstat/com.jijian.flowstat.TrafficWidgetSetting", 30000);
		//卸载应用
		RootTools.sendShell("pm uninstall com.jijian.flowstat", 30000);
		//删除下载的apk包
		RootTools.sendShell("rm sdcard/flowstat.apk", 30000);

---
#修改字体
* 把ttf文件刷进img中
* Android系统默认的中文字体为DroidSansFallBack.ttf
* 用你想使用的字体ttf文件替换掉这个文件即可

---
#修改开机动画
* 从真机中得到bootanimation.zip
* 把bootanimation.zip放入system/media目录下

---
#删除锁屏密码
* 删除data/system下的key文件
	* 文本密码为password.key
	* 手势密码为gesture.key
















  
