
# android
./adb.exe shell
mount -o remount -o rw /system
共享文件..
rm /system/etc/security/cacerts/269953fb.0
cp /sdcard/Download/269953fb.0 /system/etc/security/cacerts/
ls /system/etc/security/cacerts/ | grep 269953fb
restart android
https://blog.csdn.net/djzhao627/article/details/102812783

# linux
certmgr.msc 根证书 导出 der 格式
openssl x509 -inform DER -in cert.cer -out cert.crt
scp to linux
sudo cp -rf cert.crt /usr/local/share/ca-certificates/cert.crt
sudo update-ca-certificates
curl https://github.com/

# chrome
cert export ...
certutil -d sql:$HOME/.pki/nssdb -A -t P -n <certificate nickname> -i <certificate filename>