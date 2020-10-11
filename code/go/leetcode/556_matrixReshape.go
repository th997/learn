package main

import (
	"fmt"
)

func main() {
	var nums = [][]int{{1, 2}, {3, 4}, {5, 6}}
	fmt.Printf("%v", matrixReshape(nums, 2, 3))
}

func matrixReshape(nums [][]int, r int, c int) [][]int {
	r0 := len(nums)
	c0 := len(nums[0])
	if r*c != r0*c0 {
		return nums
	}
	newNums := make([][]int, r)
	loc := 0
	for i := 0; i < r; i++ {
		newNums[i] = make([]int, c)
		for j := 0; j < c; j++ {
			newNums[i][j] = nums[loc/c0][loc%c0]
			loc++
		}
	}
	return newNums
}
