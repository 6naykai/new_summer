package com.nbu.service.impl;

import com.nbu.mapper.NewTHInfoMapper;
import com.nbu.pojo.NewTH;
import com.nbu.service.THMqttService;
import com.nbu.util.MqttClientBuilder;
import com.nbu.util.Result;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SimpleTHMqttService implements THMqttService {

    private static Logger logger = LoggerFactory.getLogger(SimpleTHMqttService.class);
    private static MqttClient mqttClient = null;
    @Autowired
    private NewTHInfoMapper newThInfoMapper;
    private String username = "";
    private String password = "";
    public SimpleTHMqttService() {
        StartMqttService("tcp://16.162.103.45:1883","mqtt_888999zsj","zsj","12345","/Word");
        ReceiveMqttMessage();
    }
    @Override
    public Result StartMqttService(String url,String clientId, String username, String password, String topic) {
        try {
            mqttClient = MqttClientBuilder.GetMqttBuilder().Build(url, clientId, username, password, topic);
        } catch (Exception e) {
            logger.warn("创立Mqtt服务失败，原因可能是无法连接到Mqtt服务，请检测网络");
            return new Result("创立Mqtt服务失败，原因可能是无法连接到Mqtt服务，请检测网络",false);
        }
        this.username = username;
        this.password = password;
        logger.info("建立Mqtt服务...");

        return new Result("创立Mqtt服务成功",true);
    }


    @Override
    public Result ReceiveMqttMessage() {
        if (mqttClient == null) {
            logger.warn("Mqtt服务未被建立就已经被使用");
            return new Result("请先创立Mqtt服务",false);
        }
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.warn("连接丢失，立马尝试重新连接");
                try {
                    logger.debug("重新连接将会在3秒后开始...");
                    Thread.sleep(3000);
                    MqttConnectOptions options = new MqttConnectOptions();
                    // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
                    // 这里设置为true表示每次连接到服务器都以新的身份连接
                    options.setCleanSession(true);
                    options.setUserName(username);
                    options.setPassword(password.toCharArray());
                    options.setConnectionTimeout(10);
                    options.setKeepAliveInterval(60);
                    logger.debug("开始重新连接");
                    mqttClient.connect(options);
                } catch (InterruptedException e) {
                    logger.warn("重连线程中断错误");
                } catch (MqttSecurityException e) {
                    logger.warn("Mqtt连接安全问题");
                } catch (MqttException e) {
                    logger.warn("Mqtt重新连接失败，原因未知");
                }
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String receiveMessage = new String(mqttMessage.getPayload());
                    NewTH recode = new NewTH();
                    logger.debug("Mqtt发送主题为"+s+"的消息");
                    logger.info("解析Mqtt发布数据.....");
                    receiveMessage.replace("{"," ").trim();
                    String[] split = receiveMessage.split(",");
                //TODO 这里差一个包的格式解析假设为｛10,10,dec01｝{device_id，temperature，humidity}
                    recode.setDeviceID(split[0]);
                    recode.setTemperature(split[1]);
                    recode.setHumidity(split[2]);
                    recode.setRecodeDate(new Date());
                    logger.info("Mqtt发布消息解析完毕，开始持久化....");
                    newThInfoMapper.InsertOneRecode(recode);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    logger.info("MQTT服务器连接验证中.....");
            }
        });
        return new Result("",true);
    }

    @Override
    public Result ReleaseResourceForConnect() {
        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
