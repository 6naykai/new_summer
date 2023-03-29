package com.nbu.controller;


import com.nbu.service.ExcelService;
import com.nbu.service.impl.SimpleExcelOutputForUserInfoService;
import com.nbu.util.JsonUtil;
import com.nbu.util.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@Api("测试用接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    @Qualifier("SimpleExcelOutputForFileResourceInfoService")
    private ExcelService excelService;

    @Autowired
    private JsonUtil jsonUtil;

    @PostMapping("/excel")
    public String TestController(HttpServletResponse response){
        excelService.OutputExcelForLocal(System.getProperty("user.dir").replace("\\","/")
                +"/src/main/resources/xls/","fileResourcesInfo.xls");
        return jsonUtil.ObjectToJsonString(excelService.OutPutExcelForClient(response));
    }
}
