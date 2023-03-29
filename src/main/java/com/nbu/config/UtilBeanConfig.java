package com.nbu.config;

import com.nbu.util.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class UtilBeanConfig {

    @Bean("SimpleJsonUtil")
    @Scope("singleton")
    public JsonUtil GetSimpleJsonUtil() {
        return new JsonUtil();
    }

    @Bean("RedisSession")
    @Scope("singleton")
    public RedisSession GetRedisSession() {
        return new RedisSession();
    }
    @Bean("SHA256EncodeUtil")
    @Scope("singleton")
    public EncodeUtil GetEncodeUtil(){
        return new EncodeUtil();
    }

    @Bean("CachedThreadPool")
    @Scope("singleton")
    public ExecutorService ThreadPool(){
        return Executors.newCachedThreadPool();
    }


//    @Bean("SerialPortTool")
//    @Scope("singleton")
//    public DSerialPort dSerialPort(){
//        return DSerialPort.GetDSerialPortSingleton();
//    }


//    @Bean("PoiUtil")
//    @Scope("singleton")
//    public PoiUtil GetPoiUtil(){
//        return new PoiUtil("");
//    }
}
