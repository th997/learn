package main

import (
	"fmt"
	"runtime"
	"sync"
)

func main() {
	runtime.GOMAXPROCS(1)
	wg := sync.WaitGroup{}
	wg.Add(20)
	for i := 0; i < 10; i++ {
		go func() {
			fmt.Println("i: ", i) // 10,10,10...
			wg.Done()
		}()
	}
	for i := 0; i < 10; i++ {
		go func(i int) {
			fmt.Println("i: ", i)
			wg.Done()
		}(i)
	}
	wg.Wait()
}

/*
i:  9
i:  10
i:  10
i:  10
i:  10
i:  10
i:  10
i:  10
i:  10
i:  10
i:  10
i:  0
i:  1
i:  2
i:  3
i:  4
i:  5
i:  6
i:  7
i:  8
*/
