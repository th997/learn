# DOClever

## install 
git clone https://github.com/sx1989827/DOClever

docker pull mongo:latest
docker run -itd --name mongo -p 27017:27017 mongo --auth
docker exec -it mongo mongo admin
db.createUser({user:"root",pwd:"123456",roles:["root"] })
db.auth('root', '123456')

node  Server/bin/www

vim config.json

## script
``` js
//header['cookie'] = ''; // 不需要cookie
header['secure'] = 'RSA'; // 加密方式
var aesKey = 'jkPkLMrT7gpuclRs'; // aes原始密码,随机
var aesKeyRsa = 'xxx'; // rsa 公钥加密的密钥
header['securekey'] = aesKeyRsa;
var reqContent = ''+ CryptoJS.AES.encrypt(''+body,aesKey);
header['body'] = JSON.stringify(body)+reqContent;
body = reqContent;
obj.bodyInfo.rawText = reqContent;
```


