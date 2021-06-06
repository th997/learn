# kubernetes (k8s)

## master 
etcd,kube-apiserver,kube-controller-manager,kube-scheduler

## node
kube-proxy,kubelet,docker 

## 基本对象
### pod 
最小部署单元,1个pod有1个或者多个容器组成,共享网络或存储,在同一台docker主机上运行
### service 
应用服务对象,定义pod逻辑集合
### volume 
数据卷
### namespace 
虚拟集群
### lable 
标签用于区分对象(如pod,service),键值对存在,每个对象可有多个标签

## 高层次抽象对象
### replicaset
### deployment
### statefulset
### daemonset
### job

## 主要功能
数据卷

应用程序健康检查

复制应用程序实例

弹性收缩

服务发现

负载均衡

滚动更新

服务编排

提供认证和授权


## 跨主机通信
docker 原生的 overlay 和 macvlan

第三方方案：常用的包括 flannel、weave 和 calico

## k8s 安装
```

关闭swap  https://liangxinhui.tech/2020/04/16/ubuntu-swap-config/
sudo swapon --show
sudo swapoff -v /swap.img 
vim /etc/fstab 
rm -rf /swap.img

安装  https://thenewstack.io/how-to-deploy-a-kubernetes-cluster-with-ubuntu-server-18-04/

curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add
sudo apt install  software-properties-common
sudo apt-add-repository "deb http://apt.kubernetes.io/ kubernetes-xenial main"
sudo apt-get install kubeadm kubelet kubectl -y

重置集群
sudo kubeadm reset
sudo rm -rf $HOME/.kube/config
sudo ifconfig cni0 down    
sudo ip link delete cni0
sudo rm -rf /var/lib/cni/
sudo rm -rf /etc/cni/

初始化集群
sudo kubeadm init --pod-network-cidr=10.244.0.0/16 
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

安装网络  (注意--pod-network-cidr和kube-flannel.yml　中ｉｐ一致)
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
（calico　https://docs.projectcalico.org/master/manifests/calico.yaml）

设置主节点可以安装
kubectl taint nodes --all node-role.kubernetes.io/master-

加入集群 （ 忘记token  kubeadm token create --print-join-command ）
sudo kubeadm join 10.10.10.106:6443 --token lavfj7.4umo22l5vsorv88g \
    --discovery-token-ca-cert-hash sha256:d7c159d973bcdf45023fdba4e6751cdd6c2a6085ddec89001f847613b15c26

安装面板
https://github.com/kubernetes/dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/master/aio/deploy/recommended.yaml
kubectl proxy &
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/overview?namespace=_all
https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md
查看token kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')
修改回话超时
kubectl edit deployment kubernetes-dashboard -n kubernetes-dashboard //args 修改新增 - '--token-ttl=43200'

配置优化 nodeport 支持所有端口
vim /etc/kubernetes/manifests/kube-apiserver.yaml
command  下添加
- --service-node-port-range=1-65535

配置优化 kubectl get cs 状态问题
vim /etc/kubernetes/manifests/kube-controller-manager.yaml
注释　- --port=0
vim /etc/kubernetes/manifests/kube-scheduler.yaml
注释　- --port=0

kube-apiserver参数优化 (根据集群配置调整)
- --default-watch-cache-size=16
- --target-ram-mb=64
- --etcd-count-metric-poll-period=2m
- --audit-log-mode=batch
- --audit-log-batch-max-wait=10s
- --profiling=false
pod 资源限制(requests启动所需资源，不能大于limits，cpu 500m 代表 0.5个cpu)
resources:
  limits:
    cpu: 500m
    memory: 512Mi

节点资源配置
vim /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
--eviction-hard=memory.available<100Mi
systemctl restart kubelet


启动失败问题排查
tail -f /var/log/syslog

```

## 命令
```shell
# 查看健康状态
kubectl get cs
# 查看集群信息
kubectl cluster-info
# 获取类型为Pod的信息，　deployment/service/node
kubectl get pod
kubectl get pod -o wide
kubectl get pod --all-namespaces -o wide
kubectl describe pod kube-flannel-ds-amd64-tjdbg --namespace kube-system
# 在名称为nginx-pod-xxxxxx的Pod中运行bash
kubectl exec -it nginx-pod-xxxxxx /bin/bash
# 查看日志
kubectl logs  kube-flannel-ds-amd64-tjdbg --namespace kube-system
# 进入容器内部
kubectl exec -it podnamexxx /bin/sh
# 重启pods
kubectl replace --force -f xxx.yml
# 扩缩容
kubectl scale deployment --replicas=1  apollo-portal
# 工具
kubectl run -it --rm --image=radial/busyboxplus sh
# 密钥
kubectl create secret generic tls-secret --from-file=tls.key=./privkey.pem --from-file=tls.crt=./fullchain.pem -n kubernetes-dashboard
# 导出配置
kubectl get service xxx -o yaml > backup.yaml
# 编辑配置
kubectl -n kubernetes-dashboard edit service kubernetes-dashboard
# 查看资源
kubectl api-resources
kubectl api-versions
# 查看资源参数
kubectl explain ingress --recursive
kubectl explain ingress --recursive --api-version=networking.k8s.io/v1 
kubectl explain  --api-version=networking.k8s.io/v1 ingress.spec.rules.http.paths
kubectl explain  --api-version=v1 Pod.spec.containers.resources
# 文档
https://kubernetes.io/zh/docs/reference/kubectl/cheatsheet/

```

## 端口
10250 kubelet 监听 通信端口
10248 kubelet 监听 健康检查端口
6443 api server 监听，通信端口

## spark 
https://kubernetes.feisky.xyz/practice/introduction/spark

## nginx ingress
https://kubernetes.github.io/ingress-nginx/deploy/

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/baremetal/deploy.yaml

https://github.com/kubernetes/ingress-nginx/blob/master/deploy/static/provider/baremetal/deploy.yaml

## rancher
https://github.com/rancher/rancher

https://www.bookstack.cn/read/rancher-2.4.4-zh/%E4%BA%A7%E5%93%81%E4%BB%8B%E7%BB%8D.md

sudo docker run -d --restart=unless-stopped -p 53201:80 -p 53200:443 --privileged \
  -e HTTP_PROXY="http://10.10.10.106:1080" \
  -e HTTPS_PROXY="http://10.10.10.106:1080" \
  -e NO_PROXY="localhost,127.0.0.1,0.0.0.0,10.0.0.0/8,192.168.10.0/24" \
rancher/rancher

## mysql
https://kubernetes.io/zh/docs/tasks/run-application/run-replicated-stateful-application/

## pv pvc
https://www.qikqiak.com/k8strain/storage/local/

## docker 优化， apiserver cpu占用高
vim /etc/docker/daemon.json
{
  "exec-opts": ["native.cgroupdriver=systemd"]
}

## kubectl top
```
wget https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml -O metrics-server.yaml
vim metrics-server.yaml
command:
    - /metrics-server
    - --kubelet-preferred-address-types=InternalIP
    - --kubelet-insecure-tls
kubectl apply -f metrics-server.yaml

```

## prometheus
kubectl apply -f https://raw.githubusercontent.com/giantswarm/prometheus/master/manifests-all.yaml

kubectl delete namespace monitoring

node
pod
application

## 查看证书是否过期
sudo kubeadm certs check-expiration

## 更新证书
sudo kubeadm certs renew all

## 更新配置
kubeadm init phase kubeconfig all

## k8s 网络
https://blog.csdn.net/weixin_29115985/article/details/78963125

## 书籍
https://www.funtl.com/zh/service-mesh-kubernetes/

https://kuboard.cn/learning

https://jeremyxu2010.github.io/2019/11/kubernetes%E9%9B%86%E7%BE%A4%E9%83%A8%E7%BD%B2%E8%BF%90%E8%90%A5%E5%AE%9E%E8%B7%B5%E6%80%BB%E7%BB%93/#heading

## 清除无效pod
kubectl  get pods | grep Evicted | awk '{print$1}'| xargs kubectl delete pods