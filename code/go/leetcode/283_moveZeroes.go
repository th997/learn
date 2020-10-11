package main

import (
	"fmt"
)

func main() {
	var nums = []int{0, 1, 0, 3, 12}
	moveZeroes(nums)
	fmt.Printf("%v", nums)
}

func moveZeroes(nums []int) {
	size := len(nums)
	loc := 0
	for i := 0; i < size; i++ {
		if nums[i] != 0 {
			nums[loc] = nums[i]
			loc++
		}
	}
	for ; loc < size; loc++ {
		nums[loc] = 0
	}
}
