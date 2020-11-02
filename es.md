# es
 
# install 
https://hub.docker.com/_/elasticsearch


docker run -d --name es  -p 9200:9200 -p 9300:9300 \
-e discovery.type=single-node \
elasticsearch:7.9.3

支持跨域访问
docker run -d --name es  -p 9200:9200 -p 9300:9300 \
-e discovery.type=single-node \
-e http.port=9200  \
-e http.cors.enabled=true  \
-e http.cors.allow-origin=* \
-e http.cors.allow-headers=X-Requested-With,X-Auth-Token,Content-Type,Content-Length,Authorization  \
-e http.cors.allow-credentials=true \
elasticsearch:7.9.3

# es web ui
docker run -it --rm -p 59800:9800 containerize/elastichd
http://localhost:59800/
http://elastic:password@ip:9200


docker run -it --rm -p 59100:9100 docker.io/mobz/elasticsearch-head:5


docker run -it --rm -p 51358:1358  appbaseio/dejavu

# create index
curl -X PUT -H "Content-Type: application/json" http://localhost:9200/user -d'
{
    "settings" : {
        "index" : {
            "number_of_shards" : 5, 
            "number_of_replicas" : 1 
        }
    }
}'

# add data
curl -X PUT -H "Content-Type: application/json" http://localhost:9200/user/_doc/1 -d '{"name":"mike","age":20,"region":"cn"}'


