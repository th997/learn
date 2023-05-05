# podman 

## install
apt install podman

## config
vim /etc/containers/registries.conf
```conf
unqualified-search-registries = ["docker.io"]
[[registry]]
prefix = "docker.io"
location = "hub-mirror.c.163.com"
insecure = true
```
podman network ls

podman network inpect xxx

podman network rm xxx

podman network create --driver bridge --subnet 10.20.0.0/16 --gateway 10.20.0.1 podman

## auto start
podman generate systemd -n -f xxx

mv container-xxx.service /etc/systemd/system/

systemctl enable --now container-xxx

