package com.example.spring_system.mapper;

import com.example.spring_system.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {
    public List<Order> findallorder();
    public List<Order> findorderbyuid(Integer u_id);
    public List<Order> findorderbysid(Integer s_id);
    public List<Order> findorderbyids(Integer u_id,Integer s_id);
    public int addorder(Order order);
    public int deleteorder(Integer u_id, Integer s_id, Date o_date,String o_type);
    public int deleteorderbysid(Integer s_id);
}
