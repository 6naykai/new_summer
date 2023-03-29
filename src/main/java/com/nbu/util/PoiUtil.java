package com.nbu.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PoiUtil {
    private HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
    private String excelPath = null;
    private  Logger logger = LoggerFactory.getLogger(PoiUtil.class);


    public PoiUtil(String excelPath){
        this.excelPath = excelPath;
    }
    /**
     * 创立一个Excel文件.xsl
     */
    public void MakeAExcelFile(String excelName) {
            if (this.excelPath == null) {
                logger.warn("Excel file path not declare");
                logger.info("You should declare excelPath");
                return;
            }
        try {
            FileOutputStream outputStream = new FileOutputStream(this.excelPath+excelName);
            this.hssfWorkbook.write(outputStream);
            logger.info("Create Excel Succeed!");
            logger.info(this.excelPath+excelName);
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void InsertContentToExcelFile() {

    }

    public HSSFWorkbook getHssfWorkbook() {
        return this.hssfWorkbook;
    }

    /**
     * 结束工具的使用，回收资源
     */
    public void EndOfWork(){
        try {
            this.hssfWorkbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
