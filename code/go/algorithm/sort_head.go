package main

import (
	"fmt"
)

func main() {
	var nums = []int{4, 3, 6, 1, 2, 5}
	headSort(nums)
	fmt.Printf("%v", nums)
}

// 堆排序
// 性质一: 索引为i的左孩子的索引是 (2*i+1);
// 性质二: 索引为i的左孩子的索引是 (2*i+2);
// 性质三: 索引为i的父结点的索引是 floor((i-1)/2);
func maxHeapDown(a []int, start int, end int) {
	start := len(a)/2 - 1
	for ; start >= 0; start-- {
		parent := (start - 1) / 2
		if a[start] > a[parent] {
			a[start], a[parent] = a[parent], a[start]
		}
	}
}
