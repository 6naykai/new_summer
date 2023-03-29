package com.nbu.service;

import com.nbu.util.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;

public interface ExcelService {
    /**
     * 导出Excel文件在本地
     * @param targetPath 导出文件的目标路径
     */
    Result OutputExcelForLocal(String targetPath, String fileName);

    /**
     * 导出Excel文件到客户端，用于前后端
     * @param response 对前端的响应
     */
    Result OutPutExcelForClient(HttpServletResponse response);
    /**
     * 导入Excel的内容到数据库
     * @param objectExcel 目标的Excel文件的路径
     */
    Result InputExcel(String objectExcel);



}
