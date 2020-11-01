var fs = require('fs')
var crypto = require('crypto')
var myKey = fs.readFileSync('/home/th/project/key/rsa_pub.key');
console.log("myKey:", myKey.toString())


// npm install node-rsa
var NodeRSA = require('node-rsa');
var pubKey = new NodeRSA('-----BEGIN PUBLIC KEY----- ' + myKey.toString() + ' -----END PUBLIC KEY-----');
console.log(pubKey.getKeySize())
var text = crypto.randomBytes(16).toString('hex').toUpperCase();
console.log("text:", text)
var encrypted = pubKey.encrypt(text, 'base64');
console.log('encrypted:', encrypted);

// error
pubKey.decrypt(encrypted, 'utf8');
