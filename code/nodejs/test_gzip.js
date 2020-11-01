zlib = require('zlib');
data = Buffer.from("asdfasdfasd11");
gzData = zlib.gzipSync(data);
unGzData = zlib.gunzipSync(gzData);
console.log(unGzData.toString())