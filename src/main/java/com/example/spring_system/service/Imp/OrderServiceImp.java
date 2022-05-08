package com.example.spring_system.service.Imp;

import com.example.spring_system.domain.Order;
import com.example.spring_system.mapper.OrderMapper;
import com.example.spring_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> findallorder() {
        return orderMapper.findallorder();
    }

    @Override
    public List<Order> findorderbyuid(Integer u_id) {
        return orderMapper.findorderbyuid(u_id);
    }

    @Override
    public List<Order> findorderbysid(Integer s_id) {
        return orderMapper.findorderbysid(s_id);
    }

    @Override
    public List<Order> findorderbyids(Integer u_id, Integer s_id) {
        return orderMapper.findorderbyids(u_id, s_id);
    }

    @Override
    public int addorder(Order order) {
        return orderMapper.addorder(order);
    }

    @Override
    public int deleteorder(Integer u_id, Integer s_id, Date o_date, String o_type) {
        return orderMapper.deleteorder(u_id, s_id, o_date, o_type);
    }

    @Override
    public int deleteorderbysid(Integer s_id) {
        return deleteorderbysid(s_id);
    }


}
