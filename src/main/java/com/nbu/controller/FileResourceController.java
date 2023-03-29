package com.nbu.controller;


import com.nbu.service.FileResourceInfoService;
import com.nbu.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Api(value = "文件功能接口")
@RestController
@RequestMapping("/file")
public class FileResourceController {

    final private Logger logger = LoggerFactory.getLogger(FileResourceController.class);

    @Autowired
    @Qualifier("SimpleJsonUtil")
    private JsonUtil jsonUtil;

    @Autowired
    @Qualifier("CachedThreadPool")
    private Executor threadPool;
    @Autowired
    @Qualifier("SimpleFileResourceInfoService")
    private FileResourceInfoService fileResourceInfoService;


    @PostMapping("/upload")
    @ApiOperation("用于上传文件的接口")
    public String UploadFile(@RequestParam("upload_file") MultipartFile file) {
        try {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
                return jsonUtil.ObjectToJsonString(fileResourceInfoService.UploadFIleService(file));
            },threadPool);
            completableFuture.join();
            return completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/download/{file_name}")
    @ApiOperation("用于文件下载的接口")
    public String DownloadFile(@PathVariable("file_name") String fileName, HttpServletResponse response){
        try {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
                return jsonUtil.ObjectToJsonString(fileResourceInfoService.DownloadFileService(fileName,response));
            },threadPool);
            completableFuture.join();
            return completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/delete/{file_name}")
    @ApiOperation("删除文件的接口")
    public String DeleteFile(@PathVariable("file_name") String fileName){
        try {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
                return jsonUtil.ObjectToJsonString(fileResourceInfoService.DeleteFIleService(fileName));
            },threadPool);
            completableFuture.join();
            return completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/search/{page_of_query}")
    @ApiOperation("分页查找文件信息")
    public String QueryFile(@PathVariable("page_of_query") Integer pageOfQuery){
        try {
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
                return jsonUtil.ObjectToJsonString(fileResourceInfoService.QueryFileService(pageOfQuery));
            },threadPool);
            completableFuture.join();
            return completableFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
