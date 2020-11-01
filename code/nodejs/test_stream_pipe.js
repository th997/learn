let byte2Base64Stream = require("stream").Transform({
  transform: function (chunk, encoding, next) {
    this.push(Buffer.from(chunk, 'binary').toString("base64").toUpperCase());
    next();
  }
});

let fs = require("fs")
let rs = fs.createReadStream("/home/th/temp/test.txt")
rs.pipe(byte2Base64Stream).pipe(process.stdout)