package com.nbu.service.impl;

import com.nbu.mapper.DeviceMapper;
import com.nbu.mapper.NewTHInfoMapper;
import com.nbu.mapper.UserInfoMapper;
import com.nbu.pojo.Device;
import com.nbu.service.NetWorkWYService;
import com.nbu.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("NewWorkWYServiceImpl")
public class NewWorkWYServiceImpl implements NetWorkWYService {

    private Logger logger = LoggerFactory.getLogger(NetWorkWYService.class);

    @Autowired
    private NewTHInfoMapper newTHInfoMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Result AddDevice(String username, String deviceID) {
        Integer userID = userInfoMapper.GetUserInfoIdByUsername(username);
        Device device = new Device();
        if (userID == null) return new Result("设备数据插入失败",false);
        device.setUserID(userID);
        device.setDeviceID(deviceID);
        deviceMapper.InsertOneRecode(device);
        return new Result("设备数据插入成功",true);
    }

    @Override
    public Result DeleteDevice(String username, String deviceID) {
        Integer userID = userInfoMapper.GetUserInfoIdByUsername(username);
        Device device = new Device();
        if (userID == null) return new Result("设备数据删除失败",false);
        device.setUserID(userID);
        device.setDeviceID(deviceID);
        deviceMapper.DeleteOneDeviceRecode(device);
        return new Result("设备数据删除成功",true);
    }

    @Override
    public Result QueryTHByDeviceLimit(String deviceID) {
        List<Device> results = newTHInfoMapper.QueryTHByDeviceIdLimit(deviceID);
        return new Result("用户设备查询结果,若不足20条则返回不到20条，至多20条",results);
    }

    @Override
    public Result QueryUserOwnDevice(String username) {
        Integer userID = userInfoMapper.GetUserInfoIdByUsername(username);
        List<String> results = deviceMapper.QueryDeviceIdByUserId(userID);
        return new Result("查询用户拥有设备",results);
    }
}
