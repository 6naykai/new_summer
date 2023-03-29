package com.nbu.service;

import com.nbu.util.Result;

public interface NetWorkWYService {

    Result AddDevice(String username,String deviceID);

    Result DeleteDevice(String username,String deviceID);

    Result QueryTHByDeviceLimit(String deviceID);

    Result QueryUserOwnDevice(String username);
}
