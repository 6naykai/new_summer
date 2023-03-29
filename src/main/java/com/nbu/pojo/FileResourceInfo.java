package com.nbu.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * +----------------+---------------------------------------------------+------+-----+---------+----------------+
 * | Field          | Type                                              | Null | Key | Default | Extra          |
 * +----------------+---------------------------------------------------+------+-----+---------+----------------+
 * | id             | int                                               | NO   | PRI | NULL    | auto_increment |
 * | file_name      | varchar(200)                                      | NO   |     | NULL    |                |
 * | file_type      | enum('.txt','.pdf','.png','.jpeg','.doc','.docx') | NO   |     | NULL    |                |
 * | upload_time    | datetime                                          | NO   |     | NULL    |                |
 * | download_times | int                                               | NO   |     | NULL    |                |
 * +----------------+---------------------------------------------------+------+-----+---------+----------------+
 */

@JsonPropertyOrder({"id","file_name","file_type","upload_time","download_times"})
public class FileResourceInfo {
    private Integer id;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_type")
    private String fileType;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("upload_time")
    private Date uploadTime;

    @JsonProperty("download_times")
    private Integer downloadTimes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    @Override
    public String toString() {
        return "FileResourceInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", uploadTime=" + uploadTime +
                ", downloadTimes=" + downloadTimes +
                '}';
    }

}
