wrk.method = "POST"
wrk.headers["Content-Type"] = "application/x-www-form-urlencoded"
wrk.body = "id=1234"

-- wrk -t 4 -c 500 -d 60s  -s stress.lua --latency http://localhost:32001/user/getUser/123
-- curl -X POST http://localhost:32001/user/getUser/1234
-- curl http://localhost:32002/order/getUser?id=1234