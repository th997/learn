package com.demo.java.mqtt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class NettyMqttServer implements InitializingBean {

    private Logger log = LoggerFactory.getLogger(NettyMqttServer.class);

    @Value("${netty.mqtt.bossGroupThreadNum:1}")
    private Integer bossGroupThreadNum = 1;
    @Value("${netty.mqtt.workerGroupThreadNum:0}")
    private Integer workerGroupThreadNum = 0;
    @Value("${netty.mqtt.port:1883}")
    private Integer port = 1883;
    @Value("${netty.mqtt.idleTime:60}")
    private Integer idleTime = 60;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(bossGroupThreadNum);
        workerGroup = new NioEventLoopGroup(workerGroupThreadNum);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                ch.pipeline().addLast("decoder", new MqttDecoder());
                ch.pipeline().addLast("heartBeatHandler", new IdleStateHandler(idleTime, 0, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast("handler", MqttBrokerHandler.INSTANCE);
            }
        });
        ChannelFuture f = b.bind(port).sync();
        f.channel().closeFuture().sync();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("NettyMqttServer start");
        start();
    }

    @PreDestroy
    public void shutdown() {
        log.info("NettyMqttServer shutdown");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }


}
