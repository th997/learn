# spring cloud

## spring cloud demo create 
https://start.spring.io/

## spring boot actuator
http://www.ityouknow.com/springboot/2018/02/06/spring-boot-actuator.html

## example
https://github.com/piomin/sample-spring-microservices-new

https://github.com/intomylife/SpringCloud

## docker packeage
https://zhuanlan.zhihu.com/p/90122357

## spring cloud conifg
http://localhost:31000/common.yml

## spring cloud discovery eureka
http://localhost:31001
30s一次心跳，90s后断开

## source 
请求入口: DispatcherServlet.doService -> RequestMappingHandlerAdapter.handleInternal -> invokeHandlerMethod
url查找对应方法: AbstractHandlerMethodMapping.lookupHandlerMethod
初始化各种处理器: DispatcherServlet.initStrategies
http解码器: WebMvcConfigurationSupport.addDefaultHttpMessageConverters
参数解析器: RequestMappingHandlerAdapter.afterPropertiesSet()
请求客户端: RestTemplate.RestTemplate()
