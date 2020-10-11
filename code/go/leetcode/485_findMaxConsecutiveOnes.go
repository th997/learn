package main

import (
	"fmt"
)

func main() {
	var nums = []int{1, 1, 0, 1}
	fmt.Printf("%v", findMaxConsecutiveOnes(nums))
}

func findMaxConsecutiveOnes(nums []int) int {
	max := 0
	curMax := 0
	for _, v := range nums {
		if v == 1 {
			curMax++
		} else {
			curMax = 0
		}
		if curMax > max {
			max = curMax
		}
	}
	return max
}
