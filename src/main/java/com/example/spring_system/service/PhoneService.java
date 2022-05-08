package com.example.spring_system.service;

import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PhoneService {
    public void sendPhone(String phone,String vcode) throws Exception {
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com","108329" , "f45f01b4-d09c-479c-95fd-e4a110b4dc2f"
        );
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("number", phone);
        params.put("templateId", "4017");
        String[] templateParams = new String[2];
        templateParams[0] = vcode;
        templateParams[1] = "5分钟";
        params.put("templateParams", templateParams);
        String result =client.send(params);
        System.out.println(result);
        System.out.println(client.balance());
    }
}
