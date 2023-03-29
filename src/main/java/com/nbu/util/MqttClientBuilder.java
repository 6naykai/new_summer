package com.nbu.util;

import com.nbu.TestUnit;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientBuilder {

    private static Logger logger = LoggerFactory.getLogger(MqttClientBuilder.class);

    /**
     * 我懒得搞层级关系了，这里手写单例了，就不用Spring帮我管理了，虽然不好
     * 但我偷懒了 哈哈。。。
     */
    private static MqttClientBuilder singleton = null;
    private MqttClientBuilder(){}
    public static MqttClientBuilder GetMqttBuilder() {
        if (singleton == null) {
            synchronized (MqttClientBuilder.class) {
                if (singleton == null) {
                    singleton = new MqttClientBuilder();
                }
            }
        }
        return singleton;
    }

    public  MqttClient Build(String url,String clientId,String username,String password,String subscribeTopic) throws Exception {
            MqttClient mqttClient = new MqttClient(url, clientId, new MemoryPersistence());
            // 配置参数信息
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
            // 这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置用户名
            options.setUserName(username);
            // 设置密码
            options.setPassword(password.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*60秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(60);
            // 连接
            mqttClient.connect(options);
            // 订阅
            mqttClient.subscribe(subscribeTopic);
            return mqttClient;
    }
}
