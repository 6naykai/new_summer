package com.nbu.mapper;


import com.nbu.pojo.Essay;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 +------------+--------------+------+-----+---------+----------------+
 | Field      | Type         | Null | Key | Default | Extra          |
 +------------+--------------+------+-----+---------+----------------+
 | id         | int          | NO   | PRI | NULL    | auto_increment |
 | my_project | varchar(200) | NO   |     | NULL    |                |
 | my_assay   | varchar(200) | NO   |     | NULL    |                |
 | owner      | varchar(20)  | NO   |     | NULL    |                |
 | score      | int          | NO   |     | NULL    |                |
 +------------+--------------+------+-----+---------+----------------+
 */
@Mapper
@Repository("EssayMapper")
public interface EssayMapper {

    @Select("SELECT * FROM essay WHERE owner = #{username}")
    @Results({@Result(property = "id",column = "id"),
            @Result(property = "myProject",column = "my_project"),
            @Result(property = "myEssay",column = "my_assay"),
            @Result(property = "owner",column = "owner"),
            @Result(property = "score",column = "score")})
    List<Essay> SelectMyProjectAndEasyNom(@Param("username") String username);


    @Select("SELECT * FROM essay")
    @Results({@Result(property = "id",column = "id"),
            @Result(property = "myProject",column = "my_project"),
            @Result(property = "myEssay",column = "my_assay"),
            @Result(property = "owner",column = "owner"),
            @Result(property = "score",column = "score")})
    List<Essay> SelectMyProjectAndEasyAdm();

    @Delete("DELETE FROM essay WHERE owner = #{username}")
    void DeleteInfoByUsername(@Param("username")String username);

    @Insert("INSERT INTO essay (id,my_project,my_assay,owner,score) " +
            "VALUES(#{essay.id}," +
            "#{essay.myProject}," +
            "#{essay.myEssay}," +
            "#{essay.owner}," +
            "#{essay.score})")
    void InsertAInfo(@Param("essay")Essay essay);

    @Update("UPDATE essay SET my_project = #{my_project} " +
            "WHERE owner = #{username}")
    void UpdateInfoByUsernameProject(@Param("username")String username,@Param("my_project") String  myProject);

    @Update("UPDATE essay SET my_assay  = #{my_assay} " +
            "WHERE owner = #{username}")
    void UpdateInfoByUsernameEssay(String username,String  my_assay);

    @Update("UPDATE essay SET score  = #{score} " +
            "WHERE owner = #{username}")
    void UpdateScoreByUsername(String username,Integer score);


}
