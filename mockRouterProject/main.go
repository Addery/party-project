package main

import (
	"fmt"
	"net/http"

	"github.com/gin-gonic/gin"
)

func echoHandler(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{
		"msg":  "Hello from external httpbin service",
		"path": c.Request.URL.Path,
	})
}

func main() {
	gin.SetMode(gin.ReleaseMode)
	r := gin.Default()

	r.Any("/*any", echoHandler)
	fmt.Println("Server running at http://127.0.0.1:9001")
	r.Run(":9001")
}
