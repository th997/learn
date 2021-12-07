package com.demo.java.mqtt;

import ch.qos.logback.classic.Level;
import io.github.quickmsg.common.config.BootstrapConfig;
import io.github.quickmsg.core.Bootstrap;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class MqttTest2 {

    public static void main(String[] args) throws MqttException {
        Bootstrap.builder()
                .rootLevel(Level.DEBUG)
                .tcpConfig(
                        BootstrapConfig
                                .TcpConfig
                                .builder()
                                .port(18831)
//                                .username("smqtt")
//                                .password("smqtt")
                                .build())
                .httpConfig(
                        BootstrapConfig
                                .HttpConfig
                                .builder()
                                .enable(true)
                                .accessLog(true)
                                .build())
                .clusterConfig(
                        BootstrapConfig.
                                ClusterConfig
                                .builder()
                                .enable(true)
                                .namespace("smqtt")
                                .node("node-1")
                                .port(7773)
                                .url("127.0.0.1:7771,127.0.0.1:7772").
                                build())
                .build()
                .startAwait();
    }
}