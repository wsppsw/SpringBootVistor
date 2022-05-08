package com.example.spring_system.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private String o_id;//订单号
    private Integer u_id;//用户id
    private Integer s_id;//景点id
    private Date o_date;//订票日期
    private String o_type;//订票类型
}
