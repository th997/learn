const crypto = require('crypto');
const encoding = "utf8"

function aesEncrypt(key, str) {
    // console.log("ken len:", key.length)
    let iv = Buffer.from(crypto.randomBytes(8), encoding)
    //console.log("iv:", iv.toString("hex"))
    let cipher = crypto.createCipheriv('aes-128-gcm', key, iv);
    cipher.setAutoPadding(true)
    return Buffer.concat([Buffer.from([iv.length]), iv, cipher.update(str, encoding), cipher.final(), cipher.getAuthTag()]).toString("hex")
}

function aesDecrypt(key, crypted) {
    crypted = Buffer.from(crypted, 'hex');
    let ivLen = crypted.readInt8(0);
    console.log("ivLen:", ivLen)
    let iv = crypted.slice(1, ivLen + 1)
    console.log("iv:", iv.toString("hex"))
    let cipher = crypto.createDecipheriv('aes-128-gcm', key, iv);
    let ret = cipher.update(crypted.slice(1 + ivLen), 'binary');
    return ret.slice(0, ret.length - key.length).toString(encoding);
}

module.exports = {
    aesEncrypt,
    aesDecrypt
}

// key 16-> aes 128, key 32-> aes 256
let key = crypto.randomBytes(16).toString('hex').toUpperCase();
key = Buffer.from(key, 'hex');
key = Buffer.from("8BC6B689EE67EC49097856AF9A6F111B", 'hex');
console.log("key:", key.toString('hex'))
// 原始数据
let data = '呵呵哈哈hello world';
// 加密
let enData = aesEncrypt(key, data);
console.log("enData:", enData);
// 解密
let deData = aesDecrypt(key, enData);
console.log("data:", deData);