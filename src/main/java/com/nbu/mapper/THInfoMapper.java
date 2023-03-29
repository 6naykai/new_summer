package com.nbu.mapper;

import com.nbu.pojo.TH;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("THInfoMapper")
public interface THInfoMapper {

    @Select("SELECT id,temperature,humidity FROM th_info ORDER BY id DESC LIMIT 10")
    List<TH> GetTHInfoLimit();

    @Insert("INSERT INTO th_info(id,temperature,humidity) VALUES(" +
            "#{id}," +
            "#{temperature}," +
            "#{humidity}" +
            ")")
    void InsertTHInfo(TH thInfo);

    @Delete("DELETE FROM th_info WHERE id=#{id}")
    void DeleteTHInfo(Integer id);

    @Select("SELECT id FROM th_info ORDER BY id DESC LIMIT 0,1")
    Integer GetLastID();
}
