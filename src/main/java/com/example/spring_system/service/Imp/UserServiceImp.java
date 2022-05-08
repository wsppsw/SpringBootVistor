package com.example.spring_system.service.Imp;

import com.example.spring_system.domain.User;
import com.example.spring_system.mapper.UserMapper;
import com.example.spring_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findalluser() {
        return userMapper.findalluser();
    }

    @Override
    public User findbyusername(String u_name) {
        return userMapper.findbyusername(u_name);
    }

    @Override
    public User findbyuserphone(String u_phone) {
        return userMapper.findbyuserphone(u_phone);
    }

    @Override
    public int adduser(User user) {
        return userMapper.adduser(user);
    }

    @Override
    public int updateuser(User user) {
        return userMapper.updateuser(user);
    }

    @Override
    public int adduserbrowse(String u_browse, Integer u_id) {
        return userMapper.adduserbrowse(u_browse, u_id);
    }

}
