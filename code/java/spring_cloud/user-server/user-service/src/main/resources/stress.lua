wrk.method = "POST"
wrk.headers["Content-Type"] = "application/x-www-form-urlencoded"
wrk.body = "id=1234"

-- wrk -t 8 -c 32 -d 10s  -s stress.lua --latency http://10.10.10.106:31003/order/getUser/123
-- wrk -t 8 -c 32 -d 10s  -s stress.lua --latency http://10.10.10.106:32001/user/getUser/123
-- wrk -t 8 -c 32 -d 10s  -s stress.lua --latency http://10.10.10.106:9080/user/getUser/123
-- curl -X POST http://localhost:32001/user/getUser/1234
-- curl -X POST http://localhost:32002/order/getUser/234
-- curl -X POST http://localhost:31003/user/getUser/234
-- curl -X POST http://localhost:31003/order/getUser/234

-- docker run -it --rm --name test1 -v stress.lua:/stress.lua williamyeh/wrk  wrk -t 8 -c 100 -d 10s  -s /stress.lua --latency http://10.10.10.106:31003/order/getUser/123
