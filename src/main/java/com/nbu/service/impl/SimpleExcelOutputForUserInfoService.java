package com.nbu.service.impl;

import com.nbu.mapper.TableInfoMapperForOpenGauss;
import com.nbu.mapper.UserInfoMapper;
import com.nbu.mapper.UserInfoMapperForOpenGauss;
import com.nbu.pojo.UserInfo;
import com.nbu.service.ExcelService;
import com.nbu.util.Result;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * 简单的对于用户信息表的Excel打印服务
 */
@Service("SimpleExcelOutputForUserInfoService")
public class SimpleExcelOutputForUserInfoService implements ExcelService{

    private Logger logger = LoggerFactory.getLogger(SimpleExcelOutputForUserInfoService.class);

    @Autowired
    @Qualifier("MySQLSimpleUserMapper")
    private UserInfoMapper userInfoMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Autowired
    @Qualifier("TableInfoMapperForOpenGauss")
    private TableInfoMapperForOpenGauss tableInfoMapper;

    @Override
    public Result OutputExcelForLocal(String targetPath, String fileName) {
        logger.debug(targetPath.replace("\\","/") +fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetPath.replace("\\","/")+fileName);
            ExcelContentBuild(fileOutputStream);
            return new Result("用户信息Excel文件打印成功至"+targetPath.replace("\\","/") +fileName,true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result OutPutExcelForClient(HttpServletResponse response) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ExcelContentBuild(outputStream);
            return new Result("用户信息Excel导出成功",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result InputExcel(String objectExcel) {
            return new Result("还没有创立",false);
    }


    /**
     * 辅助创立Excel
     * @param outputStream 需要一个输出流来进行创立内容的输出
     * @throws IOException 资源释放的异常
     */
    private void ExcelContentBuild(OutputStream outputStream) throws IOException {
        logger.info("建立了Excel表格");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        logger.info("建立页表");
        HSSFSheet sheet = hssfWorkbook.createSheet();
        logger.info("开始对表中内容在文件中进行填写");
        logger.info("初始话表头");
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("用户名");
        titleRow.createCell(1).setCellValue("账户创建日期");
        titleRow.createCell(2).setCellValue("最近更改日期");
        titleRow.createCell(3).setCellValue("身份");
        Integer lineNum = tableInfoMapper.CounterUserInfoLineNum();
        for (int i = 1;i<=lineNum;i++) {
            UserInfo[] userInfo = userInfoMapper.SearchUserInfoLimit(i-1,1); //根据主键查询，如果主键中间断开，那么就没有机会查询
            if (userInfo == null) break;
            HSSFRow row = sheet.createRow(i); // 找到对应的行准备开始写入内容
            for (int j = 0;j < 4;j++) {       // 找到对应的单元格开始写入
                HSSFCell cell = row.createCell(j);
                switch (j) {
                    case 0:
                        cell.setCellValue(userInfo[0].getUsername());
                        break;
                    case 1:
                        cell.setCellValue(simpleDateFormat.format(userInfo[0].getCreateDate()));
                        break;
                    case 2:
                        cell.setCellValue(simpleDateFormat.format(userInfo[0].getUpdateDate()));
                        break;
                    case 3:
                        if (userInfo[0].getLevel() == 0) {
                            cell.setCellValue("管理员");
                        }else {
                            cell.setCellValue("普通用户");
                        }
                        break;
                    default:
                        logger.warn("表结构匹配不正确");
                }
            }
            if (i == lineNum){
                logger.debug("打印结束");
            }
        }
        hssfWorkbook.write(outputStream);
        hssfWorkbook.close();
        outputStream.close();
    }
}
