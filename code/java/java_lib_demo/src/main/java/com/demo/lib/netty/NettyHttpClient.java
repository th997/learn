package com.demo.lib.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class NettyHttpClient {
    public static void main(String[] args) throws InterruptedException, SSLException {
        NettyHttpClient client = new NettyHttpClient(true);
        int count = 100000;
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 1; i <= count; i++) {
            HttpReq req = new HttpReq("th.tdhere.com", 443, null);
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/");
//            request.headers().set(HttpHeaderNames.HOST, req.getHost());
//            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
//            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
            req.setRequest(request);
            client.doRequest(req, res -> {
                ByteBuf buf = res.getHttpContent().content();
                String str = buf.toString(io.netty.util.CharsetUtil.UTF_8);
                buf.release();
                countDownLatch.countDown();
                int loc = count - (int) countDownLatch.getCount();
                if (loc % 1000 == 0) {
                    System.out.println(str);
                    System.out.println(String.format("%s,time=%s,channelSize=%s", loc, (System.currentTimeMillis() - start), client.channelSize()));
                }
            });
        }
        countDownLatch.await();
        client.shutdown();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HttpReq {
        private String host;
        private int port;
        private FullHttpRequest request;
    }

    @Data
    public static class HttpRes {
        private HttpResponse httpResponse;
        private HttpContent httpContent;
    }

    public interface HttpHandler {
        void handler(HttpRes res);
    }

    @ChannelHandler.Sharable
    public static class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            if (msg instanceof HttpResponse) {
                HttpRes res = new HttpRes();
                res.setHttpResponse((HttpResponse) msg);
                ctx.channel().attr(httpRes).set(res);
            }
            if (msg instanceof LastHttpContent) {
                HttpRes res = ctx.channel().attr(httpRes).get();
                res.setHttpContent((HttpContent) msg);
                ctx.channel().attr(httpPromise).get().setSuccess(res);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            channels.add(ctx.channel());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    final static AttributeKey<HttpRes> httpRes = AttributeKey.valueOf("httpRes");
    final static AttributeKey<Promise<HttpRes>> httpPromise = AttributeKey.valueOf("httpPromise");
    final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private EventLoopGroup eventLoopGroup;
    private EventExecutor eventExecutor;
    private Bootstrap bootstrap;
    private HttpClientInboundHandler httpClientInboundHandler;
    private SslContext sslCtx;
    private Semaphore concurrent;

    public NettyHttpClient(boolean useSsl) throws SSLException {
        httpClientInboundHandler = new HttpClientInboundHandler();
        eventLoopGroup = new NioEventLoopGroup();
        eventExecutor = new DefaultEventExecutor(eventLoopGroup);
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        concurrent = new Semaphore(1000);
        if (useSsl) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .sslProvider(SslProvider.OPENSSL)
                    // [TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, TLS_RSA_WITH_AES_128_GCM_SHA256, TLS_RSA_WITH_AES_128_CBC_SHA, TLS_RSA_WITH_AES_256_CBC_SHA, TLS_AES_128_GCM_SHA256, TLS_AES_256_GCM_SHA384]
                    //.ciphers(Arrays.asList("AES-128-GCM-SHA256"))
                    .build();
        }
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                if (useSsl) {
                    ch.pipeline().addLast(sslCtx.newHandler(ch.alloc(), eventLoopGroup));
                }
                ch.pipeline().addLast(new HttpResponseDecoder());
                ch.pipeline().addLast(new HttpRequestEncoder());
                ch.pipeline().addLast(new HttpContentDecompressor());
                ch.pipeline().addLast(httpClientInboundHandler);
            }
        });
    }

    public void doRequest(HttpReq req, HttpHandler handler) throws InterruptedException {
        concurrent.acquire();
        bootstrap.connect(req.getHost(), req.getPort()).addListener((ChannelFutureListener) future -> {
            Channel ch = future.channel();
            Promise<HttpRes> promise = new DefaultPromise<>(eventExecutor);
            ch.attr(httpPromise).set(promise);
            ch.writeAndFlush(req.getRequest());
            promise.addListener(l -> {
                handler.handler((HttpRes) l.get());
                ch.close();
                concurrent.release();
            });
        });
    }

    public int channelSize() {
        return channels.size();
    }

    public void shutdown() {
        this.eventExecutor.shutdownGracefully();
        this.eventLoopGroup.shutdownGracefully();
    }
}
