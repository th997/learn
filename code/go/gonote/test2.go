package main

import "fmt"

type student struct {
	Name string
	Age  int
}

func pase_student() {
	m := make(map[string]*student)
	stus := []student{
		{Name: "zhou", Age: 24},
		{Name: "li", Age: 23},
		{Name: "wang", Age: 22},
	}
	for _, stu := range stus {
		fmt.Printf("%v\n", stu)
		m[stu.Name] = &stu
	}
	for k, v := range m {
		fmt.Printf("%v,%v\n", k, v)
	}

}

func main() {
	pase_student()
}

/*
{zhou 24}
{li 23}
{wang 22}

zhou,&{wang 22}
li,&{wang 22}
wang,&{wang 22}
*/
