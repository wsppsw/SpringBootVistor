package com.example.spring_system.domain;

import lombok.Data;

@Data
public class Scenic {
    private Integer s_id;//编号
    private String s_name;//景区名称
    private String s_address;//景区地址
    private String s_city;//所在城市
    private String s_score;//景区评分
    private Double s_price;//价格
    private String s_time;//开放时间
    private String s_coordinate;//景区坐标
    private String s_introduce;//介绍
    private String s_imgsrc;//图片路径
}
