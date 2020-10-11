特性
1 高阶函数(Higher-order functions)
2 闭包(closures)
3 模式匹配(Pattern matching)
4 单一赋值(Single assignment)
5 延迟计算(Lazy evaluation)
6 类型推导(Tail call optimization)
7 尾部调用优化(Type inference)

val 不变量
var 可变量
lazy 定义时不赋值，使用时才赋值
return 返回时可以不加return
不指定返回值时，会根据类型推断
Unit 表示不存在返回值，相当于void

基本类型，均为对象
Byte
Short
Int
Long
Char
String
Float
Double
Boolean

val a=3.14  //Double
val b=3.14F //Float
val c=3.14f  //Float
val d=0.314e1 // 0.314*10=3.14
var e='A' //Char
println("""原样输出\n""")
var f=(1).+(2) //1+2 调用1的加方法
var g=1+ -3 // -2
var x="Hello"
var y="Hello"
x==y //true

var str = "Hello"
println(str.toLowerCase) //hello
println(str.reverse) //olleH
println(str.map(_.toUpper)) //HELLO
println(str drop 3) //lo
println(str slice(1,4)) //ell

var x=if("he"=="he")1 else 0
var files=(new java.io.File(".")).listFiles
for(file<-files)println(file)
for(i <- 0 to files.length-1)println(files(i))
for(i <- 0 until files.length)println(files(i))
for(file<-files if file.isFile; if file.getName.endsWith(".scala"))println(file)
scala.io.Source.fromFile(new java.io.File("d:/test.txt")).getLines.toList
var scalas=for(file <- files if file.getName.endsWith(".scala"))yield file  //Array[java.io.File]
