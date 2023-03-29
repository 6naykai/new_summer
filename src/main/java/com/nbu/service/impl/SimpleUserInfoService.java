package com.nbu.service.impl;

import com.nbu.mapper.UserInfoMapper;
import com.nbu.mapper.UserInfoMapperForOpenGauss;
import com.nbu.pojo.UserInfo;
import com.nbu.service.UserInfoService;
import com.nbu.util.EncodeUtil;
import com.nbu.util.RedisSession;
import com.nbu.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("SimpleUserInfoService")
public class SimpleUserInfoService implements UserInfoService {

    final private Logger logger = LoggerFactory.getLogger(SimpleUserInfoService.class);

    @Autowired
    @Qualifier("MySQLSimpleUserMapper")
    private UserInfoMapper userInfoMapper;

    @Autowired
    @Qualifier("SHA256EncodeUtil")
    private EncodeUtil SHA256;

    @Autowired
    @Qualifier("RedisSession")
    private RedisSession redisSession;

    @Override
    public Result LoginService(String username, String password) {
        if(username == null || password == null) {
            logger.warn("用户名或密码为空，请检查参数");
            return new Result("用户名或密码为空",false);
        }
        String bufferInfo =(String) redisSession.StringGet(username);
        String encodingPassword = SHA256.getSHA256StrJava(password);
        if (bufferInfo != null) {
            if (!bufferInfo.equals("\""+encodingPassword+"\"")){
                logger.info("缓存命中，登录失败");
                logger.debug(bufferInfo);
                logger.debug(encodingPassword);
                return new Result("登录失败，用户或密码错误",false);
            }
            logger.info("缓存命中，成功登录");
            logger.debug(bufferInfo.toString());
            return new Result("登录成功",true);
        }

        Integer id =userInfoMapper.GetUserInfoIdByUsernameAndPassword(username,encodingPassword);
        if (id == null) {
            logger.info("登录失败，用户或密码错误");
            return new Result("登录失败，用户或密码错误",false);
        }
        logger.info("登录成功,设置缓存");
        redisSession.StringSet(username,encodingPassword); //以用户名为键，密码为值，做缓存
        return new Result("登录成功",true);
    }
    @Override
    public Result RegisterService(UserInfo registerInfo) {
        if (registerInfo == null){
            logger.warn("注册信息为空，请检测参数");
            return new Result("注册信息为空",false);
        }
        String bufferInfo =(String) redisSession.StringGet(registerInfo.getUsername());
        if (bufferInfo != null) {
            logger.warn("缓存命中，说明用户存在");
            return new Result("用户已经存在",false);
        }
        Integer id = userInfoMapper.GetUserInfoIdByUsername(registerInfo.getUsername());
        if (id != null) {
            logger.warn("用户已经存在");
            return new Result("用户已经存在",false);
        }
        String encodingPassword =SHA256.getSHA256StrJava(registerInfo.getPassword());
        registerInfo.setPassword(encodingPassword);
        logger.debug(registerInfo.toString());
        userInfoMapper.InsertUserInfo(registerInfo);
        redisSession.StringSet(registerInfo.getUsername(),encodingPassword);  //以用户名为键，密码为值，做缓存
        return new Result("注册成功",true);
    }
    @Override
    public Result ModifyPassword(String username,String oldPassword, String newPassword) {  //延时双删，解决缓存不一致问题
        String buffer = (String) redisSession.StringGet(username);
        if (buffer != null) redisSession.StringDel(username);
        userInfoMapper.ModifyPasswordByUsernameAndPassword(username,SHA256.getSHA256StrJava(oldPassword),SHA256.getSHA256StrJava(newPassword));
        try {
            Thread.sleep(500);
            buffer = (String) redisSession.StringGet(username);
            if (buffer != null) redisSession.StringDel(username);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Result("密码修改成功",true);
    }
    @Override
    public Result ModifyUsername(String oldUsername, String newUsername) { //延时双删,解决缓存不一致问题
        String buffer=(String) redisSession.StringGet(oldUsername);
        if (buffer!=null)  redisSession.StringDel(oldUsername);

        userInfoMapper.ModifyUsernameByUsername(oldUsername,newUsername);

        try {
            Thread.sleep(500);
            buffer = (String) redisSession.StringGet(oldUsername);
            if (buffer != null) redisSession.StringDel(oldUsername);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Result("用户名修改成功",true);
    }
}
