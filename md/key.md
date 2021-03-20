## 根证书生成
openssl genrsa -out ca.key 2048
openssl req -x509 -new -sha256 -days 36500 -key ca.key -out ca.crt

## 根证书导入
sudo mkdir /usr/share/ca-certificates/extra
sudo cp ca.crt /usr/share/ca-certificates/extra/
sudo dpkg-reconfigure ca-certificates
sudo update-ca-certificates

## chrome
// https://grox.net/sysadm/unix/chrome.import_ca_cert
mkdir -p ~/.pki/nssdb
certutil -d sql:$HOME/.pki/nssdb -A -t TCP,TCP,TCP -n test -i ca.crt

