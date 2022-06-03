# homeassistant

## install
```sh
docker pull linuxserver/homeassistant:version-2021.5.4
docker pull homeassistant/home-assistant
2021.2.3

docker run -d \
  --name hass \
  --privileged \
  --restart=unless-stopped \
  -e TZ=Asia/Shanghai \
  -e http_proxy="http://10.10.10.106:1080" \
  -e https_proxy="http://10.10.10.106:1080" \
  -v /hass:/config \
  --network=host \
  homeassistant/home-assistant

```

## havcs
wget https://github.com/cnk700i/havcs/archive/refs/tags/2021.5.4.zip

https://dueros.baidu.com/dbp/bot/index#/configservice/eb6734d6-666b-3088-af0c-4dbc16a7e96c

## configuration.yaml
```yml
http:
  server_port: 8123
  use_x_forwarded_for: true
  trusted_proxies:
    - 10.10.10.0/24
    - 127.0.0.1
    - ::1
  ssl_certificate: /config/fullchain.pem
  ssl_key: /config/privkey.pem
  ip_ban_enabled: true
  login_attempts_threshold: 10
  cors_allowed_origins:
    - https://google.com
    - https://www.home-assistant.io

havcs:
  platform:
    - dueros
  http:
    clients:
      duerosth: GU13KJe9w
    ha_url: https://tp.tdhere.com:58123
```
