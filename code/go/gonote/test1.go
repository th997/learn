package main

import (
	"fmt"
)

func main() {
	defer_call()
}

func defer_call() {
	defer func() { fmt.Println("打印前") }() // 3
	defer func() { fmt.Println("打印中") }() // 2
	defer func() { fmt.Println("打印后") }() // 1

	panic("触发异常") // 4
}
