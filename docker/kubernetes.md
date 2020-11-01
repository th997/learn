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

## k8s 安装
关闭swap  https://liangxinhui.tech/2020/04/16/ubuntu-swap-config/

安装文档1 推荐 https://thenewstack.io/how-to-deploy-a-kubernetes-cluster-with-ubuntu-server-18-04/

```
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add
sudo apt-add-repository "deb http://apt.kubernetes.io/ kubernetes-xenial main"
sudo apt-get install kubeadm kubelet kubectl -y

（注意　普通用户　--pod-network-cidr和kube-flannel.yml　中ｉｐ一致
sudo kubeadm reset
sudo kubeadm init --pod-network-cidr=10.244.0.0/16
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
kubectl taint nodes --all node-role.kubernetes.io/master-
kubectl apply/delete -f xxx
https://kuboard.cn/learning/k8s-basics/expose.html#%E5%AE%9E%E6%88%98-%E4%B8%BA%E6%82%A8%E7%9A%84-nginx-deployment-%E5%88%9B%E5%BB%BA%E4%B8%80%E4%B8%AA-service
）
```

## 跨主机通信
docker 原生的 overlay 和 macvlan

第三方方案：常用的包括 flannel、weave 和 calico

## 书籍
https://www.funtl.com/zh/service-mesh-kubernetes/

https://kuboard.cn/learning


## 命令
```shell
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
```

## master 上可以安装
kubectl taint nodes --all node-role.kubernetes.io/master-

## 忘记token
kubeadm token create --print-join-command

https://www.cnblogs.com/linyouyi/p/10850904.html

sudo kubeadm join 10.10.10.106:6443 --token lavfj7.4umo22l5vsorv88g \
    --discovery-token-ca-cert-hash sha256:d7c159d973bcdf45023fdba4e6751cdd6c2a6085ddec89001f847613b15c26b3

## 重置集群
sudo kubeadm reset

sudo rm -rf $HOME/.kube/config

sudo ifconfig cni0 down    

sudo ip link delete cni0

## 安装面板
https://github.com/kubernetes/dashboard

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.4/aio/deploy/recommended.yaml

kubectl proxy &

http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/overview?namespace=_all

https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md

eyJhbGciOiJSUzI1NiIsImtpZCI6IlRQbnB5NlFTRDZJbG00dHo3TDdwSm5zLUw2RnkyNDhkUXN3QmlMRE54MHcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLTR3Z245Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI3NzIyNjI1ZC0yNzZhLTQ1NTgtOTM5Mi1lNWFjYzdkNzFiMDkiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.TsrdQ71jbWgaZfL2Lf-KS70l7QVE3ARl5WhhviDsFu3Bcqf5-4fMHtCShQmH_T37Cd1298sP7XEorf1hN0Znp0lH8l_K2TscpikZUExalyRnGdbLTMJogufjJWPQHbSxEsTNPuQBdcb5pQdlmlk8EAlU08wIMX0FhiI7qZc2lzTiE-Zws1XF1zBPwXFd3BKxfOJCEwY2zM0oKsSlaMymmSTA5e_jW6gITOY1OylltdBIU8Hn9njIMUkqiJRzEjuJehBpb-ho5BAtDqNS6I-xs0KGN0MKXN4HRmCCiwJNk-mETv2M5ZRDfb8Ka8Rt7-dahgq02zPlzy5Q6_PZU8UajQ

## spark 部署
https://kubernetes.feisky.xyz/practice/introduction/spark

## 50+ 顶级开源 Kubernetes 工具列表
https://www.infoq.cn/article/RPA-wswoEyjuRZfTMcut

## nginx ingress
https://kubernetes.github.io/ingress-nginx/deploy/

kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.40.2/deploy/static/provider/baremetal/deploy.yaml

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

