package com.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class ClientTest1 {
    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            //b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    ch.pipeline().addLast(new HttpClientInboundHandler());
                }
            });
            URI uri = new URI("http://localhost:1353/");
            Channel ch = b.connect(uri.getHost(), 1353).sync().channel();
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            request.headers().set(HttpHeaderNames.HOST, uri.getHost());
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            for (int i = 0; i < 100; i++) {
                ch.writeAndFlush(request);
            }
            ch.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static class HttpClientInboundHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            if (msg instanceof HttpResponse) {
//                HttpResponse response = (HttpResponse) msg;
//                System.out.println("CONTENT_TYPE:" + response.headers().get(HttpHeaders.Names.CONTENT_TYPE));
//            }
//            if (msg instanceof HttpContent) {
//                HttpContent content = (HttpContent) msg;
//                ByteBuf buf = content.content();
//                System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
//                //buf.release();
//            }
        }
    }
}
