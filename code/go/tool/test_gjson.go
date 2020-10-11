package main

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"

	"github.com/tidwall/gjson"
)

const json = `{"name":{"first":"Janet","last":"Prichard"},"age":47}`

func main() {
	resp, _ := http.Get("http://localhost:15101/actuator/mappings")
	buf, _ := ioutil.ReadAll(resp.Body)
	list := gjson.GetBytes(buf, "contexts.*.mappings.dispatcherServlets.dispatcherServlet")
	for _, item := range list.Array() {
		path := item.Get("predicate").String()
		if !strings.Contains(path, "/actuator/") {
			continue
		}
		fmt.Printf("%s\n", path)
	}

}
