# spring cloud
spring cloud + k8s

## modules
config-server: spring cloud config 

discovery-server: spring eureka discovery

user-server: spring rest server 

order-server: spring rest client


## spring boot run
cd xx

mvn spring-boot:run

## k8s build run
cd xx

mvn clean package && mvn dockerfile:build 

kubectl replace --force -f k8s/*-server-k8s.yml