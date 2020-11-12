
## 参考文档 
https://github.com/ctripcorp/apollo/tree/master/scripts/helm

https://my.oschina.net/u/4352597/blog/4524482

## 获取源
helm repo add apollo http://ctripcorp.github.io/apollo/charts

helm search repo apollo

## 获取配置文件
helm pull apollo/apollo-service --untar

helm pull apollo/apollo-portal --untar

## 导入db
https://github.com/ctripcorp/apollo/tree/master/scripts/sql

## 安装service
cp apollo-service/values.yaml ./values-service-pro.yml

vim values-service-pro.yml　
修改configdb等参数
configdb.host=xx
configdb.userName=xx
configdb.password=xx
configdb.service.enabled=true

验证配置文件
helm install --dry-run --debug apollo-service-pro ./apollo-service -f values-service-pro.yml 

安装
helm install apollo-service-pro ./apollo-service -f values-service-pro.yml 

复制控制台地址,后面用
http://apollo-service-pro-apollo-configservice.default:8080

修改参数可以卸载后重新安装
helm uninstall apollo-service-pro

## 安装portal
cp apollo-portal/values.yaml ./values-portal.yml


vim values-portal.yml　
修改configdb等参数
将安装service的控制台地址复制到参数 config.pro
portaldb.host=xx
portaldb.userName=xx 
portaldb.password=xx
portaldb.service.enabled=true
config.envs="pro"
config.metaServers.pro=http://apollo-service-pro-apollo-configservice.default:8080

验证配置文件
helm install --dry-run --debug apollo-portal ./apollo-portal -f values-portal.yml 

安装
helm install apollo-portal ./apollo-portal -f values-portal.yml 

修改参数可以卸载后重新安装
helm uninstall apollo-portal    

## 登录
http://ip:8070
默认帐号 apollo/admin

## 使用指南
https://github.com/ctripcorp/apollo/wiki/Java%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97

## 添加环境
修改库表  ApolloPortalDB.ServerConfig apollo.portal.envs,apollo.portal.meta.servers
删除 configMap apollo-portal 中 apollo.portal.envs， apollo-env.properties

