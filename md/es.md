# es
 
# install 
https://hub.docker.com/_/elasticsearch


docker run -d --name es1  -p 9200:9200  \
-e discovery.type=single-node \
elasticsearch:7.14.2

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

# query data
http://localhost:9200/user/_search?q=k1:v1+k2:v2+k3:>1

http://localhost:9200/user/_count/

http://localhost:9200/user/_mapping/

http://localhost:9200/user/_settings/

http://localhost:9200/_cat/indices

curl -X POST -H "Content-Type: application/json" http://localhost:9200/_sql?format=txt -d'
{
  "query": "SELECT c7,count(*) co FROM user group by c7 order by co desc limit 12"
}'

# delete index
curl -X DELETE "http://localhost:9200/user1"

url -X DELETE 'http://localhost:9200/_all'


# add index filed
curl -X PUT "localhost:9200/user/_mapping?pretty" -H 'Content-Type: application/json' -d'
{
  "properties": {
    "c2": { 
      "type":     "text",
      "enable": false
    }
  }
}'

# modify max_result_window
curl -X PUT localhost:9200/user/_settings -H 'Content-Type: application/json' -d '{ "index.max_result_window" :"1200000"}'

# modify max_buckets
curl -X PUT localhost:9200/_cluster/settings -H 'Content-Type: application/json' -d '{"persistent": {"search.max_buckets": 100000}}'


# 参考 
https://cloud.tencent.com/developer/article/1587375