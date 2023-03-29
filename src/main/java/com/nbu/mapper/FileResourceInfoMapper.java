package com.nbu.mapper;

import com.nbu.pojo.FileResourceInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


/**
 * file_resource_info
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

@Mapper
@Repository("MySQLFileResourceInfoMapper")
public interface FileResourceInfoMapper {

    @Results(id = "fileResourceMap",value =
            {@Result(property = "id",column = "id"),
             @Result(property = "fileName",column = "file_name"),
             @Result(property = "fileType",column = "file_type"),
             @Result(property = "uploadTime",column = "upload_time"),
             @Result(property = "downloadTimes",column = "download_times")
            })
    @Select("SELECT file_name,file_type,upload_time,download_times " +
            "FROM file_resource_info " +
            "LIMIT #{start_page},#{end_page}")
    FileResourceInfo[] QueryFileResourceInfoByLimit(@Param("start_page") Integer startPage, @Param("end_page") Integer endPage);
    @Select("SELECT file_name,file_type,upload_time,download_times " +
            "FROM file_resource_info " +
            "WHERE file_name = #{file_name}")

    FileResourceInfo SearchFileResourceInfoByFileName(@Param("file_name") String fileName);

    @Update("UPDATE file_resource_info " +
            "SET download_times = download_times+1 " +
            "WHERE file_name = #{file_name}")
    void IncrementFileDownloadTimes(@Param("file_name") String fileName);

    @Insert("INSERT INTO file_resource_info (id,file_name,file_type,upload_time,download_times) " +
            "VALUES (null,#{file_resource_info.fileName},#{file_resource_info.fileType},#{file_resource_info.uploadTime}," +
            "#{file_resource_info.downloadTimes})")
    void InsertFileResourceInfo(@Param("file_resource_info") FileResourceInfo fileResourceInfo);

    @Delete("DELETE " +
            "FROM file_resource_info " +
            "WHERE file_name=#{file_name} AND file_type=#{file_type}")
    void DeleteFileResourceInfoByFileNameAndFileType(@Param("file_name") String fileName,
                                                     @Param("file_type")String fileType);
}
