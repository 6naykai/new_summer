package com.nbu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
public class TestUnit {

    private static Logger logger = LoggerFactory.getLogger(TestUnit.class);

    public static void main(String[] args) {
                try {
                    // host为主机名，clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
                    // MemoryPersistence设置clientid的保存形式，默认为以内存保存
                    MqttClient mqttClient = new MqttClient("tcp://16.162.103.45:1883", "mqttjs_812e65587f", new MemoryPersistence());
                    // 配置参数信息
                    MqttConnectOptions options = new MqttConnectOptions();
                    // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
                    // 这里设置为true表示每次连接到服务器都以新的身份连接
                    options.setCleanSession(true);
                    // 设置用户名
                    options.setUserName("admin");
                    // 设置密码
                    options.setPassword("public".toCharArray());
                    // 设置超时时间 单位为秒
                    options.setConnectionTimeout(10);
                    // 设置会话心跳时间 单位为秒 服务器会每隔1.5*60秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
                    options.setKeepAliveInterval(60);
                    // 连接
                    mqttClient.connect(options);
                    // 订阅
                    mqttClient.subscribe("/Word");
                    // 设置回调
                    mqttClient.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable throwable) {
                            // 连接失败时调用  重新连接订阅
                            System.out.println("连接丢失.............");
                            try {
                                System.out.println("开始重连");
                                Thread.sleep(3000);
                                mqttClient.connect(options);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (MqttSecurityException e) {
                                e.printStackTrace();
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                            logger.info("接收消息主题 : " + topic);
                            logger.info("接收消息Qos : " + mqttMessage.getQos());
                            logger.info("接收消息内容 : " + new String(mqttMessage.getPayload()));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                            //认证过程
                            logger.info("deliveryComplete.............");
                        }
                    });
                    // 创建消息
                    MqttMessage message = new MqttMessage("JAVA!".getBytes());
                    // 设置消息的服务质量
                    message.setQos(1);
                    // 发布消息
                    mqttClient.publish("/Word", message);
                    while(true){
                        mqttClient.publish("/Word", message);
                    }
                    // 断开连接
//                    mqttClient.disconnect();
                    // 关闭客户端
//                    mqttClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }



    }
}
