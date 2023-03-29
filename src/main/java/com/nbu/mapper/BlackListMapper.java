package com.nbu.mapper;

import com.nbu.pojo.BlackListItem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("BlackListMapper")
public interface BlackListMapper {



    @Select("SELECT ban_user_id " +
            "FROM black_list " +
            "LIMIT #{start_page},#{end_page}")
    @Results({@Result(property = "id",column = "id"),
              @Result(property = "banUserId",column = "ban_user_id")})
    BlackListItem[] SelectBlackListLimit(@Param("start_page") Integer startPage,@Param("end_page") Integer endPage);


    @Select("SELECT ban_user_id " +
            "FROM black_list " +
            "WHERE ban_user_id = #{user_id}")
    Integer SelectBlackItemIdByUserId(@Param("user_id") Integer userId);

    @Select("SELECT " +
            "CASE " +
            " WHEN max(id) IS NULL THEN 0" +
            " ELSE max(id) +1 " +
            "END AS next_id " +
            "FROM black_list")
    Integer GetTheLastId();


    @Delete("DELETE FROM black_list " +
            "WHERE ban_user_id=#{user_id}")
    void DeleteBlackListItemByUserId(@Param("user_id") Integer userId);

    @Insert("INSERT INTO black_list (id,ban_user_id) " +
            "VALUES(#{item.id},#{item.banUserId})")
    void InsertBlackList(@Param("item") BlackListItem blackListItem);

}
