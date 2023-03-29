package com.nbu.service;

import com.nbu.util.Result;

public interface THMqttService {

    Result StartMqttService(String url,String clientId,String username, String password, String topic);

    Result ReceiveMqttMessage();

    Result ReleaseResourceForConnect();
}
