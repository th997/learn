# openwrt 

## download
https://mirrors.a-m-v.pl/downloads.openwrt.org/

https://mirrors.a-m-v.pl/downloads.openwrt.org/releases/19.07.3/targets/x86/64/openwrt-19.07.3-x86-64-combined-ext4.img.gz

## network
```
vim /etc/config/network
config interface 'lan'
    option type 'bridge'
    option ifname 'eth0'
    option proto 'static'
    option ipaddr '10.10.10.2'
    option netmask '255.255.255.0'

config interface 'wlan'
    option ifname 'eth0'
    option proto 'dhcp'

/etc/init.d/network reload

if kvm:
nic -> birdge -> device model e1000

```

## login
passwd ...

http://10.10.10.2

## firewall
iptables -t nat -I POSTROUTING -j MASQUERADE

## install package
```
add source  https://github.com/songchenwen/nanopi-r2s-opkg-feeds/tree/master/packages
export https_proxy=http://10.10.10.106:1080;export http_proxy=http://10.10.10.106:1080
opkg update 
opkg install luci-app-passwall	--force-depends
(opkg remove dnsmasq)
```


