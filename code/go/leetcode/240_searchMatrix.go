package main

import (
	"fmt"
)

func main() {
	var nums = [][]int{{1, 2}, {3, 4}, {5, 6}}
	fmt.Printf("%v", searchMatrix(nums, 2))
}

func searchMatrix(matrix [][]int, target int) bool {
	if matrix == nil || len(matrix) == 0 || len(matrix[0]) == 0 {
		return false
	}
	r := len(matrix)
	n := len(matrix[0])
	for i := 0; i < r; i++ {
		for j := 0; j < n; j++ {
			if matrix[i][j] > target {
				break
			}
			if matrix[i][j] == target {
				return true
			}
		}
	}
	return false
}
