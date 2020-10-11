package main

import (
	"fmt"
)

func main() {
	var nums = []int{4, 3, 6, 1, 2, 5}
	quickSort(nums, 0, len(nums)-1)
	fmt.Printf("%v", nums)
}

// 快速排序 小于a[l]放左边，大于a[l]放右边 递归
func quickSort(a []int, l int, r int) {
	if l < r {
		fmt.Printf("%v\n", a)
		i := l
		j := r
		x := a[i]
		for i < j {
			for i < j && a[j] > x {
				j--
			}
			if i < j {
				a[i] = a[j]
				i++
			}
			for i < j && a[i] < x {
				i++
			}
			if i < j {
				a[j] = a[i]
				j--
			}
		}
		a[i] = x
		quickSort(a, l, i-1)
		quickSort(a, i+1, r)
	}
}
