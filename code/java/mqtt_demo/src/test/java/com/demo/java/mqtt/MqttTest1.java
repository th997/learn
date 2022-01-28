package com.demo.java.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttTest1 {


    public static void main(String[] args) throws MqttException {
        //String broker = "tcp://xxxx.mqtt.aliyuncs.com:1883";
        String broker = "tcp://localhost:1883";
        for (int i = 0; i < 1; i++) {
            String topic = "tip/topic" + i;
            String clientId = "GID_tip1" + i;
            // 阿里云例子 https://code.aliyun.com/aliware_mqtt/mqtt-demo/blob/master/lmq-java-demo/src/main/java/com/aliyun/openservices/lmq/example/demo/MQ4IoTSendMessageToMQ4IoTUseSignatureMode.java
            // 阿里云 https://code.aliyun.com/aliware_mqtt/mqtt-demo/blob/master/lmq-java-demo/src/main/java/com/aliyun/openservices/lmq/example/util/ConnectionOptionWrapper.java
            //参数说明 https://blog.csdn.net/zhetmdoubeizhanyong/article/details/104871483
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions = new MqttConnectOptions();
//            mqttConnectOptions.setUserName("Signature|" + accessKey + "|" + instanceId);
//            mqttConnectOptions.setPassword(Tools.macSignature(clientId, secretKey).toCharArray());
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setKeepAliveInterval(100);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            mqttConnectOptions.setConnectionTimeout(5000);
            // 建立连接
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            // 客户端设置好发送超时时间，防止无限阻塞
            mqttClient.setTimeToWait(5000);
            // mqttClient.setManualAcks(false);
            mqttClient.connect(mqttConnectOptions);
            //　订阅消息　
            mqttClient.subscribe(topic);
            mqttClient.setCallback(new MqttCallback() {
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String msg = String.format("messageArrived topic=%s,msgId=%s,qos=%s,content=%s", topic, message.getId(), message.getQos(), new String(message.getPayload()));
                    System.out.println(msg);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete");
                }

                public void connectionLost(Throwable throwable) {
                    System.out.println("connectionLost");
                }
            });

            // 发送消息
            for (int j = 0; j < 2; j++) {
                String msg = "message-" + j;
                MqttMessage message = new MqttMessage(msg.getBytes());
                System.out.println("publish message " + msg);
                mqttClient.publish(topic, message);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            System.out.println("client:" + i);
        }
    }
}