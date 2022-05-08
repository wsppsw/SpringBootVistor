package com.example.spring_system.service.Imp;

import com.example.spring_system.domain.Scenic;
import com.example.spring_system.mapper.ScenicMapper;
import com.example.spring_system.service.ScenicService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicServiceImp implements ScenicService {

    @Autowired
    private ScenicMapper scenicMapper;

    @Override
    public List<Scenic> findall() {
        return scenicMapper.findall();
    }

    @Override
    public Scenic findbySid(Integer s_id) {
        return scenicMapper.findbySid(s_id);
    }

    @Override
    public List<Scenic> findallLimit(Integer value) {
        return scenicMapper.findallLimit(value);
    }

    @Override
    public List<Scenic> findbyName(String s_name) {
        return scenicMapper.findbyName(s_name);
    }

    @Override
    public List<Scenic> findbyAddress(String s_address) {
        return scenicMapper.findbyAddress(s_address);
    }

    @Override
    public List<Scenic> findbyScore(String s_score) {
        return scenicMapper.findbyScore(s_score);
    }

    @Override
    public List<Scenic> findbyPrice(Double s_price) {
        return scenicMapper.findbyPrice(s_price);
    }

    @Override
    public int addScenic(Scenic scenic) {
        return scenicMapper.addScenic(scenic);
    }

    @Override
    public int updateScenic(Scenic scenic) {
        return scenicMapper.updateScenic(scenic);
    }

    @Override
    public int deleteScenic(Integer s_id) {
        return scenicMapper.deleteScenic(s_id);
    }

    @Override
    public PageInfo<Scenic> findallPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Scenic> list = scenicMapper.findall();
        PageInfo<Scenic> pageInfo =new PageInfo<Scenic>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Scenic> findbyNamePage(String s_name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Scenic> list = scenicMapper.findbyName(s_name);
        PageInfo<Scenic> pageInfo =new PageInfo<Scenic>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Scenic> findbyAddressPage(String s_address, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Scenic> list = scenicMapper.findbyAddress(s_address);
        PageInfo<Scenic> pageInfo =new PageInfo<Scenic>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Scenic> findbyScorePage(String s_score, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Scenic> list = scenicMapper.findbyScore(s_score);
        PageInfo<Scenic> pageInfo =new PageInfo<Scenic>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Scenic> findbyPricePage(Double s_price, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Scenic> list = scenicMapper.findbyPrice(s_price);
        PageInfo<Scenic> pageInfo =new PageInfo<Scenic>(list);
        return pageInfo;
    }

    @Override
    public List<Scenic> findbrowse(List list) {
        return scenicMapper.findbrowse(list);
    }
}
