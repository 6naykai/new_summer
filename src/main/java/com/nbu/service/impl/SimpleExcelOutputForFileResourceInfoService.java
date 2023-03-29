package com.nbu.service.impl;

import com.nbu.mapper.*;
import com.nbu.pojo.FileResourceInfo;
import com.nbu.service.ExcelService;
import com.nbu.util.Result;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;


@Service("SimpleExcelOutputForFileResourceInfoService")
public class SimpleExcelOutputForFileResourceInfoService implements ExcelService {

    private Logger logger = LoggerFactory.getLogger(SimpleExcelOutputForFileResourceInfoService.class);
    @Autowired
    private FileResourceInfoMapper fileResourceInfoMapper;

    @Autowired
    private TableInfoMapperForOpenGauss tableInfoMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Result OutputExcelForLocal(String targetPath, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(targetPath+fileName);
            ExcelContentBuild(fileOutputStream);
            return new Result("资源文件Excel文档本地导出成功",true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Result OutPutExcelForClient(HttpServletResponse response) {
        try {
            ExcelContentBuild(response.getOutputStream());
            return new Result("资源文件Excel文档导出成功",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Result InputExcel(String objectExcel) {
        return new Result("还没有实现",false);
    }

    private void ExcelContentBuild(OutputStream outputStream) throws IOException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet();
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("资源名称");
        titleRow.createCell(1).setCellValue("资源类型");
        titleRow.createCell(2).setCellValue("文件上传时间");
        titleRow.createCell(3).setCellValue("文件下载次数");
        Integer lineNum = tableInfoMapper.CounterFileResourceInfoLineNum();

        for(int rowNum = 1;rowNum <= lineNum;rowNum++) {
            HSSFRow row = sheet.createRow(rowNum);
            FileResourceInfo[] fileResourceInfo = fileResourceInfoMapper.QueryFileResourceInfoByLimit(rowNum-1, 1);
            if (fileResourceInfo == null) break;
            for (int cellNum = 0;cellNum < 4 ;cellNum++) {
                HSSFCell cell = row.createCell(cellNum);
                switch (cellNum) {
                    case 0:
                        cell.setCellValue(fileResourceInfo[0].getFileName());
                        break;
                    case 1:
                        if (fileResourceInfo[0].getFileType().equals(".doc") || fileResourceInfo[0].getFileType().equals(".docx")) {
                            cell.setCellValue("文档");
                        }else if (fileResourceInfo[0].getFileType().equals(".jpeg") || fileResourceInfo[0].getFileType().equals(".png")) {
                            cell.setCellValue("图片");
                        } else if (fileResourceInfo[0].getFileType().equals(".txt")) {
                            cell.setCellValue("txt文本");
                        } else if (fileResourceInfo[0].getFileType().equals(".pdf")) {
                            cell.setCellValue("pdf文件");
                        }
                        break;
                    case 2:
                        cell.setCellValue(simpleDateFormat.format(fileResourceInfo[0].getUploadTime()));
                        break;
                    case 3:
                        cell.setCellValue(fileResourceInfo[0].getDownloadTimes());
                        break;
                    default:
                }
            }
        }
        hssfWorkbook.write(outputStream);
        hssfWorkbook.close();
        outputStream.close();
    }
}
