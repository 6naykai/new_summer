package com.nbu.controller;


import com.nbu.mapper.UserInfoMapper;
import com.nbu.pojo.UserInfo;
import com.nbu.service.UserBanService;
import com.nbu.service.UserInfoService;
import com.nbu.util.JsonUtil;
import com.nbu.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "负责用户登录，注册，修改信息")
@RequestMapping("/user")
public class UserController {

    final private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("NetWorkDesignUserInfoService")//"NetWorkDesignUserInfoService" "SimpleUserInfoService"
    private UserInfoService userInfoService;

    @Autowired
    @Qualifier("SimpleJsonUtil")
    private JsonUtil jsonUtil;

    @PostMapping("/login")
    @ApiOperation(value = "登录接口")
    public String Login(@RequestParam("username") String username,@RequestParam("password") String password){
//        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
//            return jsonUtil.ObjectToJsonString(userInfoService.LoginService(username,password));
//        });
//        completableFuture.join();
//        try {
//            return completableFuture.get();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }  并发并没有让效果变得更好
      return jsonUtil.ObjectToJsonString(userInfoService.LoginService(username,password));
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册接口")
    public String Set(String username,String password){
        UserInfo registerInfo = new UserInfo();
        registerInfo.setUsername(username);
        registerInfo.setPassword(password);
        registerInfo.setLevel(1);
        registerInfo.setCreateDate(new Date());
        registerInfo.setUpdateDate(new Date());

        return jsonUtil.ObjectToJsonString(userInfoService.RegisterService(registerInfo));
    }

    @PostMapping("/modify/username")
    @ApiOperation(value = "修改用户名接口")
    public String ModifyUsername(String oldUsername,String newUsername){
       return jsonUtil.ObjectToJsonString(userInfoService.ModifyUsername(oldUsername, newUsername));
    }
    @PostMapping("/modify/password")
    @ApiOperation(value = "修改用户密码的接口")
    public String ModifyPassword(String username,String oldPassword,String newPassword){
        return jsonUtil.ObjectToJsonString(userInfoService.ModifyPassword(username,oldPassword,newPassword));
    }


    @Autowired
    @Qualifier("SimpleUserBanService")
    private UserBanService userBanService;
    @PostMapping("/ban/{username}/{level}")
    @ApiOperation(value = "禁止用户登录")
    public String BanUser(@PathVariable("username") String username, @PathVariable("level") Integer level) {
        return jsonUtil.ObjectToJsonString(userBanService.BanUser(username));
    }

    @PostMapping("/release/{username}/{level}")
    @ApiOperation(value = "释放用户登录")
    public String ReleaseUser(@PathVariable("username") String username,@PathVariable("level") Integer level) {
        return jsonUtil.ObjectToJsonString(userBanService.ReleaseUser(username));
    }

    @Autowired
    private UserInfoMapper userInfoMapper;
    @ApiOperation("垃圾接口未来重构")
    @PostMapping("/user_list/{start_page}")
    public String GetUserList(@PathVariable("start_page") Integer startPage){
        return jsonUtil.ObjectToJsonString(new Result("用户信息",userInfoMapper.SearchUserInfoLimit(startPage*10,10)));
    }
}
