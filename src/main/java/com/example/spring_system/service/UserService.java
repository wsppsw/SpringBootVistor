package com.example.spring_system.service;

import com.example.spring_system.domain.User;

import java.util.List;

public interface UserService {
    public List<User> findalluser();
    public User findbyuid(Integer u_id);
    public User findbyusername(String u_name);
    public User findbyuserphone(String u_phone);
    public int adduser(User user);
    public int updateuser(User user);
    public int adduserbrowse(String u_browse,Integer u_id);
}
