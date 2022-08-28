# AdGuardHome

## install
```shell
sudo systemctl stop systemd-resolved.service
sudo systemctl disable systemd-resolved.service

wget https://static.adguard.com/adguardhome/release/AdGuardHome_linux_amd64.tar.gz
tar xvf AdGuardHome_linux_amd64.tar.gz
mv AdGuardHome /d/soft/
sudo ./AdGuardHome -s install
sudo systemctl enable AdGuardHome.service
sudo systemctl start AdGuardHome.service

vim /etc/resolv.conf
change nameserver

vim AdGuardHome.yaml
...

```

