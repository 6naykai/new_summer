package com.nbu.service.impl;

import com.nbu.mapper.UserInfoMapper;
import com.nbu.mapper.UserInfoMapperForOpenGauss;
import com.nbu.pojo.UserInfo;
import com.nbu.service.UserInfoService;
import com.nbu.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 为了满足网络课设的用户信息服务
 * 重写了用户的登录部分，为的是获取更多的信息
 */
@Service("NetWorkDesignUserInfoService")
public class NetWorkDesignUserInfoService extends SimpleUserInfoService implements UserInfoService {

    private Logger logger = LoggerFactory.getLogger(NetWorkDesignUserInfoService.class);

    @Autowired
    @Qualifier("MySQLSimpleUserMapper")
    private UserInfoMapper userInfoMapperForOpenGauss;
    @Override
    public Result LoginService(String username, String password) {
        Result result = super.LoginService(username,password);
        if (result.getState() == true) {
            Integer id = userInfoMapperForOpenGauss.GetUserInfoIdByUsername(username);
            UserInfo userInfo =userInfoMapperForOpenGauss.SearchUserInfoById(id);
            userInfo.setPassword("不会告诉你");
            result = new Result("登录成功",userInfo,true);
        }
        return result;
    }
}
