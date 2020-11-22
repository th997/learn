FROM alpine

LABEL description="http proxy,socks5 proxy... more see https://github.com/th997/goproxy"

COPY goproxy /bin/
RUN chmod +x /bin/goproxy

EXPOSE 33080

# docker build -t th9976/goproxy ./
# docker push th9976/goproxy
# docker run -it --rm -p 33080:33080 --name goproxy th9976/goproxy goproxy http
