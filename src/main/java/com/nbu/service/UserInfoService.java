package com.nbu.service;

import com.nbu.pojo.UserInfo;
import com.nbu.util.Result;

public interface UserInfoService {
    /**
     * 用户登录服务
     * @param username 提供的用户名
     * @param password 提供的密码
     * @return 返回操作结果
     */
    Result LoginService(String username,String password);

    /**
     * 用户的注册服务
     * @param registerInfo 提供的注册信息
     * @return 返回操作结果
     */
    Result RegisterService(UserInfo registerInfo);

    /**
     * 修改用户的密码
     * @param username 目标用户的用户名
     * @param oldPassword 目标用户的密码
     * @param newPassword 目标用户的新密码
     * @return 返回操作结果
     */
    Result ModifyPassword(String username,String oldPassword,String newPassword);

    /**
     * 修改用户的用户名
     * @param oldUsername 用户的老用户名
     * @param newUsername 用户的新用户名
     * @return 返回操作结果
     */
    Result ModifyUsername(String oldUsername,String newUsername);
}
