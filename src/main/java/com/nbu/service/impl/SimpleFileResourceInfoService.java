package com.nbu.service.impl;

import com.nbu.mapper.FileResourceInfoMapper;
import com.nbu.mapper.FileResourceInfoMapperForOpenGauss;
import com.nbu.pojo.FileResourceInfo;
import com.nbu.service.FileResourceInfoService;
import com.nbu.util.JsonUtil;
import com.nbu.util.RedisSession;
import com.nbu.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Service("SimpleFileResourceInfoService")
public class SimpleFileResourceInfoService implements FileResourceInfoService {

    private final Logger logger = LoggerFactory.getLogger(SimpleFileResourceInfoService.class);

    private final String BASE_PATH = System.getProperty("user.dir");
    private final String FILE_SAVE_PATH = BASE_PATH.replace("\\","/")+"/src/main/resources/upload/";

    private final Integer PAGE_SIZE_OF_QUERY = 10;

    @Autowired
    @Qualifier("MySQLFileResourceInfoMapper")
    private FileResourceInfoMapper fileMapper;

    @Autowired
    @Qualifier("RedisSession")
    private RedisSession redisSession;

    @Autowired
    @Qualifier("SimpleJsonUtil")
    private JsonUtil jsonUtil;
    @Override
    @Transactional
    public Result UploadFIleService(MultipartFile file) {
        for (int page = 0;page <= maxBufferPage; page++) {
            redisSession.StringDel("QueryPage"+page);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        maxBufferPage = -1;
        for (int page = 0;page <= maxBufferPage; page++) {
            redisSession.StringDel("QueryPage"+page);
        }
        logger.debug(FILE_SAVE_PATH);
        //检验链
        if(file == null) return new Result("文件上传失败，参数为空",false);
        String[] FileNameSet = file.getOriginalFilename().split("\\.");
        if (FileNameSet.length>2) return new Result("文件上传失败，为安全考虑只允许单后缀文件",false);
        if (!FileNameSet[1].equals("txt") && !FileNameSet[1].equals("png") && !FileNameSet[1].equals("pdf")
            && !FileNameSet[1].equals("jpeg") && !FileNameSet[1].equals("doc") && !FileNameSet[1].equals("docx"))
            return new Result("文件上传失败，上传文件类型不匹配",false);

        String result = (String) redisSession.StringGet(file.getOriginalFilename()); //先重缓存里看
        if(result == null) {
            if(fileMapper.SearchFileResourceInfoByFileName(file.getOriginalFilename()) != null)
                return new Result("文件上传失败，文件已存在",false);
        }else {
            logger.debug("缓存命中");
            return new Result("文件上传失败，文件已存在",false);
        }
        //检验结束，上传
        try {
            file.transferTo(new File(FILE_SAVE_PATH+FileNameSet[1]+"/"+file.getOriginalFilename()));
            redisSession.StringSet(file.getOriginalFilename(),"exit"); //将信息写入缓存
            FileResourceInfo uploadFileInfo = new FileResourceInfo(); //将信息写入数据库
            uploadFileInfo.setFileName(FileNameSet[0]);
            uploadFileInfo.setFileType("."+FileNameSet[1]);
            uploadFileInfo.setUploadTime(new Date());
            uploadFileInfo.setDownloadTimes(0);
            fileMapper.InsertFileResourceInfo(uploadFileInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Result("上传成功",true);
    }

    @Override
    @Transactional
    public Result DownloadFileService(String fileName, HttpServletResponse response) {
        if (fileName==null) return new Result("下载失败，为接受到文件名",false);
        String[] FileNameSet = fileName.split("\\.");
        File file = new File(FILE_SAVE_PATH+FileNameSet[1]+"/"+fileName);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            logger.debug("开始向前端传输文件....");
            while (inputStream.read(buffer) != -1) {
                outputStream.write(buffer);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
            response.setHeader("Content-Disposition","attachment;filename="+fileName);
            fileMapper.IncrementFileDownloadTimes(FileNameSet[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Result("文件下载成功",true);
    }

    @Override
    @Transactional
    public Result DeleteFIleService(String fileName) {
        for (int page = 0;page <= maxBufferPage; page++) {
            redisSession.StringDel("QueryPage"+page);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        maxBufferPage = -1;
        for (int page = 0;page <= maxBufferPage; page++) {
            redisSession.StringDel("QueryPage"+page);
        }
        String[] FileNameSet = fileName.split("\\.");
        if (!FileNameSet[1].equals("txt") && !FileNameSet[1].equals("png") && !FileNameSet[1].equals("pdf")
                && !FileNameSet[1].equals("jpeg") && !FileNameSet[1].equals("doc") && !FileNameSet[1].equals("docx"))
            return new Result("文件类型不匹配，文件不存在",false);

        String bufferValue =(String) redisSession.StringGet(fileName);
        if (bufferValue == null) {
            FileResourceInfo result=fileMapper.SearchFileResourceInfoByFileName(FileNameSet[0]);
            if (result == null) return new Result("文件不存在",false);
        }
        File file = new File(FILE_SAVE_PATH+FileNameSet[1]+"/"+fileName);
        if (!file.delete()) return new Result("删除失败",false);
        fileMapper.DeleteFileResourceInfoByFileNameAndFileType(FileNameSet[0],"."+FileNameSet[1]);
        if (bufferValue != null) redisSession.StringDel(fileName);
        return new Result("删除成功",true);
    }

    private Integer maxBufferPage = -1;
    @Override
    @Transactional
    public Result QueryFileService(Integer pageOfQuery) {
        if (pageOfQuery > maxBufferPage) maxBufferPage = pageOfQuery;
        String buffer=(String) redisSession.StringGet("QueryPage"+pageOfQuery);
        FileResourceInfo[] fileResourceInfos = null;
        if (buffer != null)  fileResourceInfos=(FileResourceInfo[]) jsonUtil.JsonStringToObject(buffer,FileResourceInfo[].class);
        if (fileResourceInfos != null) {
            logger.info("缓存命中");
            logger.debug(buffer);
            return new Result("查询文件当前页"+pageOfQuery,fileResourceInfos,true);
        } //先走一遍缓存，如果缓存里面没有就去数据库里拿

        fileResourceInfos=fileMapper.QueryFileResourceInfoByLimit(pageOfQuery*PAGE_SIZE_OF_QUERY,PAGE_SIZE_OF_QUERY);
        if (fileResourceInfos != null) {
            redisSession.StringSet("QueryPage"+pageOfQuery,fileResourceInfos);
        }
        return new Result("查询文件当前页"+pageOfQuery,fileResourceInfos,true);
    }
}
