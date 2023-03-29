package com.nbu.mapper;

import com.nbu.pojo.Device;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mysql> describe device;
 * +-----------+-------------+------+-----+---------+----------------+
 * | Field     | Type        | Null | Key | Default | Extra          |
 * +-----------+-------------+------+-----+---------+----------------+
 * | id        | int         | NO   | PRI | NULL    | auto_increment |
 * | user_id   | int         | NO   | MUL | NULL    |                |
 * | device_id | varchar(20) | NO   |     | NULL    |                |
 * +-----------+-------------+------+-----+---------+----------------+
 */
@Mapper
@Repository("DeviceMapper")
public interface DeviceMapper {

    @Select("SELECT device_id " +
            "FROM device " +
            "WHERE user_id = #{user_id}")
    List<String> QueryDeviceIdByUserId(@Param("user_id") Integer userId);

    @Insert("INSERT INTO device (user_id,device_id) " +
            "VALUES(#{device_param.userID}," +
            "#{device_param.deviceID})")

    void InsertOneRecode(@Param("device_param") Device device);


    @Delete("DELETE FROM device " +
            "WHERE user_id = #{device_param.userID} " +
            "AND device_id = #{device_param.deviceID}")
    @Results({@Result(property = "userID",column = "user_id"),
            @Result(property = "deviceID",column = "device_id")})
    void DeleteOneDeviceRecode(@Param("device_param") Device device);
}
