package com.nbu.controller;


import com.nbu.service.ExcelService;
import com.nbu.service.impl.SimpleExcelOutputForFileResourceInfoService;
import com.nbu.service.impl.SimpleExcelOutputForUserInfoService;
import com.nbu.util.JsonUtil;
import com.nbu.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api("Excel操作接口")
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    @Qualifier("SimpleExcelOutputForUserInfoService")
    private ExcelService simpleExcelOutputForUserInfoService;

    @Autowired
    @Qualifier("SimpleExcelOutputForFileResourceInfoService")
    private ExcelService simpleExcelOutputForFileResourceInfoService;

    @Autowired
    @Qualifier("SimpleJsonUtil")
    private JsonUtil jsonUtil;
    private Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @ApiOperation("打印信息为Excel表格")
    @PostMapping("/print")
    public String PrintExcel(@RequestParam("print_type") String printType, HttpServletResponse response) {
        Result result = null;
        switch (printType) {
            case "file_resource":
                logger.info("开始打印文件信息为Excel表格");
                simpleExcelOutputForFileResourceInfoService.OutPutExcelForClient(response);
                break;
            case "user_info":
                logger.info("开始打印用户信息为Excel表格");
                simpleExcelOutputForUserInfoService.OutPutExcelForClient(response);
                break;
            default:
                return jsonUtil.ObjectToJsonString(new Result("未知参数",false));
        }
        return result!=null? jsonUtil.ObjectToJsonString(result):jsonUtil.ObjectToJsonString(new Result("未知错误",false));
    }
}
