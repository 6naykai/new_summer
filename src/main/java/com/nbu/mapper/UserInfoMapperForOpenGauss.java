package com.nbu.mapper;

import com.nbu.pojo.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * GS
 * user_info
 * +-------------+---------------+------+-----+---------+----------------+
 * | Field       | Type          | Null | Key | Default | Extra          |
 * +-------------+---------------+------+-----+---------+----------------+
 * | id          | int           | NO   | PRI | NULL    |                |
 * | username    | varchar(200)  | NO   | UNI | NULL    |                |
 * | password    | varchar(200)  | NO   |     | NULL    |                |
 * | create_date | datetime      | NO   |     | NULL    |                |
 * | update_date | datetime      | NO   |     | NULL    |                |
 * | level       | enum('0','1') | NO   |     | NULL    |                |
 * +-------------+---------------+------+-----+---------+----------------+
 */


@Mapper
@Repository("OpenGaussSimpleUserInfoMapper")
public interface UserInfoMapperForOpenGauss {

    @Select("SELECT id,username,password,create_date,update_date,level " +
            "FROM user_info " +
            "WHERE id=#{id}")
    @Results(id="UserInfo",
            value =
            {@Result(property = "id",column = "id"),
             @Result(property = "username" , column = "username"),
             @Result(property = "password" , column = "password"),
                    @Result(property = "createDate",column = "create_date"),
            @Result(property = "updateDate",column = "update_date"),
            @Result(property = "level",column = "level")})
    UserInfo SearchUserInfoById(@Param("id") Integer id);

    @Select("SELECT id,username,password,create_date,update_date,level " +
            "FROM user_info " +
            "LIMIT #{start_page},#{end_page}")
    @Results({@Result(property = "id",column = "id"),
                            @Result(property = "username" , column = "username"),
                            @Result(property = "password" , column = "password"),
                            @Result(property = "createDate",column = "create_date"),
                            @Result(property = "updateDate",column = "update_date"),
                            @Result(property = "level",column = "level")})
    UserInfo[] SearchUserInfoLimit(@Param("start_page") Integer startPage,@Param("end_page") Integer EndPage);
    @Select("SELECT id " +
            "FROM user_info " +
            "WHERE username=#{username} AND password=#{password}")
    Integer GetUserInfoIdByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    @Select("SELECT id " +
            "FROM user_info " +
            "WHERE username=#{username}")
    Integer GetUserInfoIdByUsername(@Param("username") String username);

    @Select("SELECT level " +
            "FROM user_info " +
            "WHERE id=#{id}")
    Integer GetUserLevelById(@Param("id") Integer id);

    @Update("UPDATE user_info " +
            "SET username=#{new_username} " +
            "WHERE username=#{old_username}")
    void ModifyUsernameByUsername(@Param("old_username") String oldUsername,@Param("new_username") String newUsername);

    @Update("UPDATE user_info " +
            "SET password=#{new_password} " +
            "WHERE username=#{username} AND password=#{old_password}")
    void ModifyPasswordByUsernameAndPassword(@Param("username")String username,
                                             @Param("old_password") String oldPassword,
                                             @Param("new_password") String newPassword);

    @Insert("INSERT INTO user_info " +
            "VALUES(" +
            "(SELECT " +
            " CASE" +
            "    WHEN MAX(id) IS NULL THEN 0" +
            "    ELSE MAX(id) + 1" +
            " END AS number" +
            " FROM user_info)," +
            "#{userInfo.username}," +
            "#{userInfo.password}," +
            "#{userInfo.createDate}," +
            "#{userInfo.updateDate}," +
            "#{userInfo.level})")
    void InsertUserInfo(@Param("userInfo") UserInfo userInfo);  //手动实现了自增主键


}
