# flutter

## install 
wget https://storage.googleapis.com/flutter_infra/releases/stable/linux/flutter_linux_2.0.2-stable.tar.xz
tar xf flutter_linux_2.0.2-stable.tar.xz
export PATH="$PATH:`pwd`/flutter/bin"
vs code install flutter plugin
cd xx
flutter run 
flutter build

## dart入门教程
https://juejin.cn/post/6844903903016796174

vs code install dart plugin

## 命名规划
大写驼峰: 类名
小写驼峰: 变量
小写下划线: 文件,文件夹

## flutter 工程目录结构 
lib 核心代码
pubspec.yaml 包管理文件,类似maven pom.xml
pubspec.lock 当前依赖
android/ios/macos/web/windows/linux 不同平台代码 
test 测试文件
build 编译输出

