
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

