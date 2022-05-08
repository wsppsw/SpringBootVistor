package com.example.spring_system.mapper;


import com.example.spring_system.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> findalluser();
    public User findbyusername(String u_name);
    public User findbyuserphone(String u_phone);
    public int adduser(User user);
    public int updateuser(User user);
    public int adduserbrowse(String u_browse,Integer u_id);
}
