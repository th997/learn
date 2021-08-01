# docker mongodb
https://hub.docker.com/_/mongo

## key
```shell
openssl rand -base64 741 > key.file
chmod 600 key.file
chown 999:999 key.file
```

## config
```shell
docker exec -it mongo_config01_1 mongo --port 27019
rs.initiate({
    _id: "rs_config_server",
    configsvr: true,
    members: [
        { _id : 0, host : "config01:27019" },
        { _id : 1, host : "config02:27019" }
    ]
});
```

## 2 replica 1 arbiter
```shell
docker exec -it mongo_shard01_1 mongo --port 27018
rs.initiate({
    _id: "shard01",
    members: [
        { _id : 0, host : "shard01:27018" },
        { _id : 1, host : "shard02:27018" },
        { _id : 2, host : "shard03:27018",arbiterOnly: true }
    ]
});
```

## router
```shell
docker exec -it mongo_mongos01_1 mongo --port 27017
sh.addShard("shard01/shard01:27018,shard02:27018,shard03:27018")
use admin
db.createUser({user:"root",pwd:"123456",roles:[{role:"root",db:"admin"}] })
db.auth('root', '123456')
```


## 2 shared 
```shell
docker exec -it mongo_shard01_1 mongo --port 27018
rs.initiate({
    _id: "shard01",
    members: [
        { _id : 0, host : "shard01:27018" },
    ]
});

docker exec -it mongo_shard02_1 mongo --port 27018
rs.initiate({
    _id: "shard02",
    members: [
        { _id : 0, host : "shard02:27018" },
    ]
});

docker exec -it mongo_mongos01_1 mongo --port 27017
sh.addShard("shard01/shard01:27018")
sh.addShard("shard02/shard02:27018")
use admin
db.createUser({user:"root",pwd:"123456",roles:[{role:"root",db:"admin"}] })
db.auth('root', '123456')

sh.enableSharding("test")
sh.shardCollection("test.user", {"_id": "hashed" },false,{"numInitialChunks":2})
sh.shardCollection("test.user", {"c1": 1 },false,{"numInitialChunks":2})
sh.disableBalancing("test.user")
# https://docs.mongodb.com/v4.2/reference/method/sh.shardCollection/
```


