package com.nbu.mapper;

import com.nbu.pojo.Device;
import com.nbu.pojo.NewTH;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * describe new_th_info;
 * +-------------+-------------+------+-----+---------+-------+
 * | Field       | Type        | Null | Key | Default | Extra |
 * +-------------+-------------+------+-----+---------+-------+
 * | device_id   | varchar(20) | NO   | PRI | NULL    |       |
 * | temperature | varchar(20) | NO   |     | NULL    |       |
 * | humidity    | varchar(20) | NO   |     | NULL    |       |
 * | recode_date | datetime    | NO   |     | NULL    |       |
 * +-------------+-------------+------+-----+---------+-------+
 */
@Mapper
@Repository("NewTHInfoMapper")
public interface NewTHInfoMapper {

    @Insert("INSERT INTO new_th_info (device_id,temperature,humidity,recode_date ) " +
            "VALUES(#{recode.deviceID}," +
            "#{recode.temperature}," +
            "#{recode.humidity}," +
            "#{recode.recodeDate})")
    @Results(
            {@Result(property = "deviceID",column = "device_id"),
             @Result(property = "temperature",column = "temperature"),
             @Result(property = "humidity",column = "humidity"),
             @Result(property = "recodeDate",column = "recode_date")})
    void InsertOneRecode(@Param("recode") NewTH recode);


    @Select("SELECT temperature,humidity,recode_date " +
            "FROM new_th_info " +
            "WHERE device_id = #{device_id} LIMIT 20")
    List<Device> QueryTHByDeviceIdLimit(@Param("device_id") String deviceId);
}
