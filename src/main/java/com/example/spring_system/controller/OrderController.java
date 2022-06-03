package com.example.spring_system.controller;

import com.example.spring_system.domain.Order;
import com.example.spring_system.domain.Scenic;
import com.example.spring_system.domain.User;
import com.example.spring_system.service.Imp.OrderServiceImp;
import com.example.spring_system.service.Imp.ScenicServiceImp;
import com.example.spring_system.service.Imp.UserServiceImp;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {

    @Autowired
    private OrderServiceImp orderServiceImp;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private ScenicServiceImp scenicServiceImp;

    //订票操作
    @RequestMapping("/user/toorder")
    @ResponseBody
    public String Userorder(HttpServletRequest request) throws ParseException {
        String sid = request.getParameter("sid");
       // String uid = request.getParameter("uid");
        String name =(String) request.getSession().getAttribute("username");
        System.out.println(name);
        if(name!=null){
            User user = userServiceImp.findbyusername(name);
            System.out.println(user);
            String sdate = request.getParameter("date");
            String type = request.getParameter("type");
            Order order = new Order();
            order.setO_id(UUID.randomUUID().toString());
            order.setU_id(user.getU_id());
            order.setS_id(Integer.parseInt(sid));
            //时间转换格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date date = formatter.parse(sdate);
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            String dates =formatter1.format(date);
            order.setO_date(formatter1.parse(dates));
            order.setO_type(type);
            orderServiceImp.addorder(order);
            return "true";
        }else {
            return "false";
        }
    }

    //查看订票记录
    @RequestMapping("/user/queryOrder")
    @ResponseBody
    public String toqueryOrder(HttpServletRequest request) throws JSONException {
        String name = (String) request.getSession().getAttribute("username");
        User user = userServiceImp.findbyusername(name);
        List<Order> list = null;
        list = orderServiceImp.findorderbyuid(user.getU_id());
        JSONArray array = new JSONArray();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                JSONObject jsonObject = new JSONObject();
                Scenic scenic = scenicServiceImp.findbySid(list.get(i).getS_id());
                jsonObject.put("s_id",scenic.getS_id());
                jsonObject.put("p_name",scenic.getS_name());
                jsonObject.put("p_address",scenic.getS_address());
                jsonObject.put("p_date",list.get(i).getO_date().toLocaleString().substring(0,10));
                jsonObject.put("p_type",list.get(i).getO_type());
                array.put(jsonObject);
            }
        }
        System.out.println(array);
        return  array.toString();
    }

    //后台所有的查看订票记录
    @RequestMapping("/manage/queryOrder")
    @ResponseBody
    public String tomanqueryOrder(HttpServletRequest request) throws JSONException {
        List<Order> list = null;
        list = orderServiceImp.findallorder();
        JSONArray array = new JSONArray();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                JSONObject jsonObject = new JSONObject();
                Scenic scenic = scenicServiceImp.findbySid(list.get(i).getS_id());
                User user = userServiceImp.findbyuid(list.get(i).getU_id());
                jsonObject.put("u_id",list.get(i).getU_id());
                jsonObject.put("s_id",scenic.getS_id());
                jsonObject.put("p_name",scenic.getS_name());
                jsonObject.put("p_address",scenic.getS_address());
                jsonObject.put("u_name",user.getU_name());
                jsonObject.put("p_date",list.get(i).getO_date().toLocaleString().substring(0,10));
                jsonObject.put("p_type",list.get(i).getO_type());
                array.put(jsonObject);
            }
        }
        System.out.println(array);
        return  array.toString();
    }

    //退票
    @RequestMapping("/cancelTicket")
    @ResponseBody
    public String tocancelTicket(HttpServletRequest request) throws JSONException, ParseException {
        String name = (String) request.getSession().getAttribute("username");
        String sid = request.getParameter("sid");
        String sdate = request.getParameter("date");
        String type = request.getParameter("type");
        User user = userServiceImp.findbyusername(name);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = formatter.parse(sdate);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        String dates =formatter1.format(date);
        orderServiceImp.deleteorder(user.getU_id(),Integer.parseInt(sid),formatter1.parse(dates),type);
        List<Order> list = orderServiceImp.findorderbyuid(user.getU_id());
        JSONArray array = new JSONArray();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                JSONObject jsonObject = new JSONObject();
                Scenic scenic = scenicServiceImp.findbySid(list.get(i).getS_id());
                jsonObject.put("s_id",scenic.getS_id());
                jsonObject.put("p_name",scenic.getS_name());
                jsonObject.put("p_address",scenic.getS_address());
                jsonObject.put("p_date",list.get(i).getO_date().toLocaleString().substring(0,10));
                jsonObject.put("p_type",list.get(i).getO_type());
                array.put(jsonObject);
            }
        }
        return array.toString();
    }

    //退票
    @RequestMapping("/admincancelTicket")
    @ResponseBody
    public String tocanceladmincancelTicket(HttpServletRequest request) throws JSONException, ParseException {
        String uids = request.getParameter("uid");
        Integer uid = Integer.valueOf(uids);
        String sid = request.getParameter("sid");
        String sdate = request.getParameter("date");
        String type = request.getParameter("type");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = formatter.parse(sdate);
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        String dates =formatter1.format(date);
        orderServiceImp.deleteorder(uid,Integer.parseInt(sid),formatter1.parse(dates),type);
        List<Order> list = orderServiceImp.findallorder();
        JSONArray array = new JSONArray();
        if(list!=null){
            for(int i=0;i<list.size();i++){
                JSONObject jsonObject = new JSONObject();
                Scenic scenic = scenicServiceImp.findbySid(list.get(i).getS_id());
                User user = userServiceImp.findbyuid(list.get(i).getU_id());
                jsonObject.put("u_id",list.get(i).getU_id());
                jsonObject.put("s_id",scenic.getS_id());
                jsonObject.put("p_name",scenic.getS_name());
                jsonObject.put("p_address",scenic.getS_address());
                jsonObject.put("u_name",user.getU_name());
                jsonObject.put("p_date",list.get(i).getO_date().toLocaleString().substring(0,10));
                jsonObject.put("p_type",list.get(i).getO_type());
                array.put(jsonObject);
            }
        }
        return array.toString();
    }

    //导出订单
    @RequestMapping("/ExportOrder")
    public void toExportOrder(HttpServletResponse response) throws IOException {
        //创建一个工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("订单表");
        //获取全部数据
        List<Order> list = orderServiceImp.findallorder();
        // 在sheet里创建第二行
        HSSFRow row1 = sheet.createRow(0);
        // 创建单元格并设置单元格内容
        row1.createCell(0).setCellValue("订单编码");
        row1.createCell(1).setCellValue("订单用户编码");
        row1.createCell(2).setCellValue("订单景点编码");
        row1.createCell(3).setCellValue("订单日期");
        row1.createCell(4).setCellValue("订单票价");

        HSSFRow rows;
        int rownum = 1;

        for(Order s:list){
            rows = sheet.createRow(rownum);
            rows.createCell(0).setCellValue(s.getO_id());
            rows.createCell(1).setCellValue(s.getU_id());
            rows.createCell(2).setCellValue(s.getS_id());
            rows.createCell(3).setCellValue(s.getO_date());
            rows.createCell(4).setCellValue(s.getO_type());
            rownum++;
        }

        // 输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=OrderData.xls");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
        wb.close();
    }
}
