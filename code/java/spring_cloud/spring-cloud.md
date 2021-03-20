# spring cloud 

## eureka server
eureka.server.use-read-only-response-cache = true
eureka.server.response-cache-update-interval-ms = 5000
eureka.server.enable-self-preservation = true

## eureka client
eureka.client.registry-fetch-interval-seconds = 5
ribbon.ServerListRefreshInterval = 5

## spring boot
server.shutdown = graceful

## k8s
containers.lifecycle.preStop.exec.command: ["sleep","15"]
liveness ...
