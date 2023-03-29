package com.nbu.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("MySQLTableInfoMapper")
public interface TableInfoMapper {

    @Select("SELECT count(id) FROM user_info")
    public Integer CounterUserInfoLineNum();

    @Select("SELECT count(id) FROM file_resource_info")
    public Integer CounterFileResourceInfoLineNum();
}
