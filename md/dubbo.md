# dubbo
https://github.com/alibaba/dubbo

https://github.com/alibaba/dubbo/issues/50

http://dubbo.io/

https://github.com/dangdangdotcom/dubbox

https://blog.csdn.net/candyzh/article/details/52700595

## 入口
```
DubboNamespaceHandler extends NamespaceHandlerSupport 解析xml, 入口定义在 META-INF/spring.handlers,META-INF/spring.schemas

dubbo:application
dubbo:registry
dubbo:protocol
dubbo:service
dubbo:reference
dubbo:config-center
dubbo:provider
dubbo:consumer
dubbo:method
dubbo:argument
dubbo:parameter
dubbo:module
dubbo:monitor
```

## 源码
```
NettyTransporter implements Transporter -> NettyServer,NettyClient -> NettyServerHandler,NettyClientHandler

ExtensionLoader.getAdaptiveExtension, createAdaptiveExtension

ReferenceBean.get()   ReferenceConfig.createProxy()  REF_PROTOCOL.refer()->MockClusterInvoker

ProtocolListenerWrapper
    MockClusterInvoker
        FailoverClusterInvoker
        invoker=RegistryDirectory.$InvokerDelegate
        invokers = list(invocation);
            RegistryDirectory.invoker=ListenerInvokerWrapper
                ListenerInvokerWrapper.invoker=ProtocolFilterWrapper.$CallbackRegistrationInvoker
                    CallbackRegistrationInvoker.filterInvoker filter
                    filterInvoker=ProtocolFilterWrapper.$1
                    invoker=AsyncToSyncInvoker
                        AsyncToSyncInvoker
                            invoker=DubboInvoker

filter
https://www.jianshu.com/p/d279349435cd

client 
ProxyFactory(JavassistProxyFactory) -> proxyObject -> invoker

JavassistProxyFactory.getProxy
org.apache.dubbo.common.bytecode.Proxy0..1..2 implements com.alibaba.dubbo.rpc.service.EchoService,xxxService
```

## 
ApplicationConfig
ProtocolConfig
RegistryConfig
ServiceConfig

## 
