package com.example.spring_system.service;

import com.example.spring_system.domain.Scenic;
import com.github.pagehelper.PageInfo;


import java.util.List;

public interface ScenicService {
    //查询
    public List<Scenic> findall();
    public Scenic findbySid(Integer s_id);
    public List<Scenic> findallLimit(Integer value);
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
    //分页查询
    public PageInfo<Scenic> findallPage(Integer pageNum, Integer pageSize);
    public PageInfo<Scenic> findbyNamePage(String s_name,Integer pageNum, Integer pageSize);
    public PageInfo<Scenic> findbyAddressPage(String s_address,Integer pageNum, Integer pageSize);
    public PageInfo<Scenic> findbyScorePage(String s_score,Integer pageNum, Integer pageSize);
    public PageInfo<Scenic> findbyPricePage(Double s_price,Integer pageNum, Integer pageSize);
    public List<Scenic> findbrowse(List list);
}
