# mongo

## key
```shell
openssl rand -base64 741 > key.file
chmod 600 key.file
chown 999 key.file
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

## router
```shell
docker exec -it mongo_mongos01_1 mongo --port 27017
sh.addShard("shard01:27018")
sh.addShard("shard02:27018")
use admin
db.createUser({user:"root",pwd:"123456",roles:[{role:"root",db:"admin"}] })
db.auth('root', '123456')
```

## shard
```shell
docker exec -it mongo_shard01_1 mongo --port 27018
rs.initiate({
    _id: "shard01",
    members: [
        { _id : 0, host : "shard01:27018" }
    ]
});

docker exec -it mongo_shard02_1 mongo --port 27018
rs.initiate({
    _id: "shard02",
    members: [
        { _id : 0, host : "shard02:27018" }
    ]
});
```


