wrk.method = "POST"
wrk.headers["Content-Type"] = "application/x-www-form-urlencoded"
wrk.body = "id=1234"

-- wrk -t 4 -c 500 -d 60s  -s stress.lua --latency http://10.10.10.106:31003/user/getUser/123
-- curl -X POST http://localhost:32001/user/getUser/1234
-- curl http://localhost:32002/order/getUser?id=1234

docker run -it --rm --name test1 -v stress.lua:/stress.lua williamyeh/wrk  wrk -t 8 -c 100 -d 10s  -s /stress.lua --latency http://10.10.10.106:31003/order/getUser/123



mysqldump --databases datatest \
  --no-tablespaces \
  --single-transaction \
  --compress \
  --order-by-primary \
  -e --max_allowed_packet=41943040 --net_buffer_length=163840 \
  -u root \
  -h 10.10.10.106 \
  -P 53306 \
  -p > test.sql