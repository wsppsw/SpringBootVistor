package com.example.spring_system;

import com.example.spring_system.domain.Scenic;
import com.example.spring_system.domain.Station;
import com.example.spring_system.service.Imp.ScenicServiceImp;
import com.example.spring_system.service.Imp.StationServiceImp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

@SpringBootTest
class SpringSystemApplicationTests {


    @Test
    void aaa() throws JSONException {
        String urls="https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.8392 ";
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(urls);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while((line = in.readLine()) != null){
                result.append(line);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        String jsData = result.toString();
        String[] data = jsData.split("@");
        for(int i=0;i<data.length;i++){
           // System.out.println(data[i]);
           // String[] subdata = data[j].split("\\|");
            if(i!=0){
                String[] subdata = data[i].split("\\|");
                //Station station = new Station();
                //station.setT_id(0);
               // station.setT_station(subdata[1]);
                //station.setT_value(subdata[3]);
               // stationServiceImp.addSattion(station);
               // System.out.println(station);
                System.out.println(subdata[1]+"-------"+subdata[3]);
            }
        }
    }

    @Test
    public void abc() throws JSONException {
        String urls = "http://wthrcdn.etouch.cn/weather_mini?city=张家口";
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(urls);
            URLConnection conn = url.openConnection();
            InputStream stream = null;
            stream = new GZIPInputStream(conn.getInputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        JSONObject object = new JSONObject(result.toString());
        String[] arr = new String[3];
        JSONObject object1 =object.getJSONObject("data");
        JSONObject yes = object1.getJSONObject("yesterday");
        JSONArray array = object1.getJSONArray("forecast");
        arr[0]=object1.getString("city");
        arr[1]=object1.getString("ganmao");
        arr[2]=object1.getString("wendu")+"℃";
        for(int i=0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
        JSONObject jsonObject2 = array.getJSONObject(0);
        String type=jsonObject2.getString("type").toString();
        System.out.println(type);
        String josn="{\"data\":{\"wendu\":\""+arr[2]+"\",\"type\":\""+type+"\"}";
        System.out.println(josn);
    }
}
