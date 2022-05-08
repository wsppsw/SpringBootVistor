package com.example.spring_system.mapper;

import com.example.spring_system.domain.Scenic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScenicMapper {
    //查询
    public List<Scenic> findall();
    public List<Scenic> findallLimit(Integer value);
    public Scenic findbySid(Integer s_id);
    public List<Scenic> findbyName(String s_name);
    public List<Scenic> findbyAddress(String s_address);
    public List<Scenic> findbyScore(String s_score);
    public List<Scenic> findbyPrice(Double s_price);
    //添加
    public int addScenic(Scenic scenic);
    //更改
    public int updateScenic(Scenic scenic);
    //删除
    public int deleteScenic(Integer s_id);
    //查找浏览记录
    public List<Scenic> findbrowse(List list);

}
