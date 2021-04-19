docker run -itd --name=grafana -p 3000:3000 grafana/grafana

grafana-cli plugins install vertamedia-clickhouse-datasource

docker restart grafana

http://localhost:3000
admin/admin

datasource add clickhouse

databoard add panel

setting add Variables

...


http://localhost:28123/api/datasources/proxy/1/?query=SELECT * FROM datatest.w2000 limit 10

