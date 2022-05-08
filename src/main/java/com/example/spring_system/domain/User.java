package com.example.spring_system.domain;

import lombok.Data;

@Data
public class User {
    private Integer u_id;//编号
    private String u_name;//姓名
    private String u_phone;//预留手机
    private String u_password;//密码
    private String u_type;//用户类型
    private String u_browse;//浏览记录
}
