package com.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientTest1 {
    private static long start = System.currentTimeMillis();

    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup(128);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            final HttpClientInboundHandler httpClientInboundHandler = new HttpClientInboundHandler();
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(httpClientInboundHandler);
                }
            });
            for (int i = 0; i < 1000000; i++) {
                URI uri = new URI("http://localhost:1353/");
                Channel ch = b.connect(uri.getHost(), uri.getPort()).sync().channel();
                FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
//            request.headers().set(HttpHeaderNames.HOST, uri.getHost());
//            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
                ch.writeAndFlush(request);
                //ch.closeFuture().sync();
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    @ChannelHandler.Sharable
    public static class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

        private AtomicInteger ac = new AtomicInteger();

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                buf.toString(io.netty.util.CharsetUtil.UTF_8);
                buf.release();
                ctx.close();
                int co = ac.addAndGet(1);
                if (co % 10000 == 0) {
                    System.out.println(co + ":" + (System.currentTimeMillis() - start));
                }
            }
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                response.headers();
            }
        }
    }
}
