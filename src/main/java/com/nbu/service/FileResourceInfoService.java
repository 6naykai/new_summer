package com.nbu.service;

import com.nbu.util.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileResourceInfoService {

    Result UploadFIleService(MultipartFile file);

    Result DownloadFileService(String fileName,HttpServletResponse response);

    Result DeleteFIleService(String fileName);

    Result QueryFileService(Integer pageOfQuery);
}
