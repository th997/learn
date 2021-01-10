# golang

### 下载
https://golang.org/dl/

http://goclipse.github.io/releases/

### 命令
* go get 获取远程包(需要安装git或者hg)
* go run 直接运行程序
* go build 测试编译,检测是否有编译错误
* go fmt 格式化源码
* go install 编译包文件并编译整个程序
* go test 运行测试文件
* go doc 查看文档

### 环境设置

tar -C /usr/local -xzf go$VERSION.$OS-$ARCH.tar.gz

export GOROOT=/usr/local/go
export GOPATH=$HOME/go
export PATH=$PATH:$GOROOT/bin

linux
alias go='https_proxy=http://127.0.0.1:1080 go'
win
set https_proxy=http://127.0.0.1:1080

### GCC安装
http://tdm-gcc.tdragon.net/download
tdm-gcc

### 编译
--- make the go executable smaller

go build -ldflags "-s -w" test.go

upx test.exe

### code
```
//包名
package main

// 导入
import "fmt"

// 常量
const PI = 3.14

// 全局变量
var name = "golang"

// 一般类型声明
type newType int

// 结构的声明
type goType struct{}

// 接口的声明
type goinf interface{}

//main 入口,
func main() {
	fmt.Print(strings.Contains("abc", "abc"))
	a := "abcd"
	fmt.Println(a == "abcd")
	fmt.Println(strings.Join([]string{"abc", "cdf"}, ","))
}

```

## gin 
Context (Engine,[]HandlerFunc,req,res)

Engine extends RouterGroup (Engine,[]HandlerFunc)

Engine implements http.Handler (.ServeHTTP) 

https://zhuanlan.zhihu.com/p/29694027

## 交叉编译
```
CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 go build -ldflags "-s -w" -o bin/mac/npc cmd/npc/*.go
CGO_ENABLED=0 GOOS=windows GOARCH=amd64 go build -ldflags "-s -w" -o bin/win/npc.exe cmd/npc/*.go
CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -ldflags "-s -w" -o bin/linux/npc cmd/npc/*.go
CGO_ENABLED=0 GOOS=linux GOARCH=arm go build -ldflags "-s -w" -o bin/arm/npc cmd/npc/*.go
upx bin/*/*
cp -rf ./bin/*  ~/soft/mycloud/dl/
```

## debug 
https://www.jianshu.com/p/25a2e6b52457

go get -u github.com/derekparker/delve/cmd/dlv

create cert ..

sudo codesign -s dlv_cert $GOPATH/binmac/dlv

vscode F5

## go mod fork
go mod init

```
require (
	github.com/snail007/goproxy v1.2.3
)
replace github.com/snail007/goproxy => ../goproxy
```

go mod tiny








