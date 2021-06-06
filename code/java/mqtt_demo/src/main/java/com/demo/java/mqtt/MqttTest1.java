package com.demo.java.mqtt;

import java.text.MessageFormat;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttTest1 {


    public static void main(String[] args) throws MqttException {
        String broker = "tcp://localhost:1883";
        for (int i = 0; i < 2; i++) {
            String topic = "demo/topic" + i;
            String clientId = i + "";
            // 建立连接
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            //https://blog.csdn.net/zhetmdoubeizhanyong/article/details/104871483
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            mqttClient.connect(connOpts);
            //　订阅消息　
            mqttClient.subscribe(topic);
            mqttClient.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String msg = String.format("topic=%s,msgId=%s,qos=%s,content=%s", topic, message.getId(), message.getQos(), new String(message.getPayload()));
                    System.out.println(msg);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

                public void connectionLost(Throwable throwable) {
                }
            });

            // 发送消息
            for (int j = 0; j < 100; j++) {
                String msg = "message-" + j;
                MqttMessage message = new MqttMessage(msg.getBytes());
                mqttClient.publish(topic, message);
            }
        }
    }
}