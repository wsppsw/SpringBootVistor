package com.example.spring_system.controller;

import com.example.spring_system.domain.Scenic;
import com.example.spring_system.domain.User;
import com.example.spring_system.service.Imp.ScenicServiceImp;
import com.example.spring_system.service.Imp.UserServiceImp;
import com.example.spring_system.service.PhoneService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private ScenicServiceImp scenicServiceImp;


    private String v_code="";

    //登录
    @RequestMapping("/login")
    public String toindex(){
        return "vue_login";
    }

    //注册
    @RequestMapping("/regist")
    public String toregist(){
        return "vue_regist";
    }

    //退出
    @RequestMapping("/exit")
    public String toexit(HttpServletRequest request){
        request.getSession().invalidate();
        return "vue_login";
    }

    //更改手机号
    @RequestMapping("/user/phone")
    public String tomodifyphone(){
        return "main/vue_user";
    }

    //更改密码
    @RequestMapping("/user/pwd")
    public String tomodifyPwd(){
        return "main/vue_modifyPwd";
    }

    //后台管理
    @RequestMapping("/management")
    public String tomanagement(){
        return "vue_management";
    }

    //忘记密码验证（一）
    @RequestMapping("/forgetPassword")
    public String toforgetPassword(){
        return "main/vue_forgetPwd";
    }

    //忘记密码（修改密码）
    @RequestMapping("/forgetAndmodfiy")
    public String toforgetAndmodify(HttpServletRequest request,Model model){
        String name = request.getParameter("username");
        model.addAttribute("name",name);
        return "main/vue_modifyPassword";
    }



    //验证用户（忘记密码一）
    @RequestMapping("/ForgetUser")
    @ResponseBody
    public String toForgetUser(HttpServletRequest request){
        String name = request.getParameter("username");
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");

        System.out.println("获取"+phone);
        User user = new User();
        user = userServiceImp.findbyusername(name);
        System.out.println(user.getU_phone());

        if(user==null){
            return "用户名不存在!";
        }else if(!user.getU_phone().equals(phone)){
            return "预留手机号不正确!";
        }else if(!code.equals(v_code)){
            return "验证码不正确!";
        }else {
            return "true";
        }
    }

    //验证密码（忘记密码二）
    @RequestMapping("/ModifyUserpassword")
    @ResponseBody
    public String toModifyUserpassword(HttpServletRequest request){
        String name = request.getParameter("name");
        String pass1 = request.getParameter("password1");
        String pass2 = request.getParameter("password2");
        User user = userServiceImp.findbyusername(name);
        if(user.getU_password().equals(pass1)){
            return "不能与旧密码不一致!";
        }
        else if(!pass1.equals(pass2)){
            return "两次密码不一致!";
        }else {

            user.setU_password(pass1);
            userServiceImp.updateuser(user);
            return "true";
        }

    }


    //发送手机验证用于找回密码
    @RequestMapping("/sendPhoneForget")
    @ResponseBody
    public String sendPhoneForget(HttpServletRequest request){
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        User user = userServiceImp.findbyusername(name);
        if(!user.getU_phone().equals(phone)){
            return "false";
        }else {
            String code="";
            Random r = new Random();
            for(int i=0;i<4;i++){
                code+=r.nextInt(10);
            }
            v_code=code;
            // phoneService.sendPhone(phone,v_code);
            return v_code;
        }
    }

    //发送手机验证码登录
    @RequestMapping("/sendphone")
    @ResponseBody
    public String send(HttpServletRequest request) throws Exception {
        String phone = request.getParameter("phone");
        System.out.println(phone);
        String code="";
        Random r = new Random();
        for(int i=0;i<4;i++){
            code+=r.nextInt(10);
        }
        v_code=code;
       // phoneService.sendPhone(phone,v_code);
        return v_code;
    }

    //发送手机验证码注册
    @RequestMapping("/sendphoneregist")
    @ResponseBody
    public String sendcode(HttpServletRequest request) throws Exception {
        String phone = request.getParameter("phone");
        Boolean flag=false;
        List<User> list = userServiceImp.findalluser();
        for (User u:list){
            if(phone.equals(u.getU_phone())){
                flag=true;
                break;
            }
        }
        if(flag){
              return "false";
        }else {
            String code = "";
            Random r = new Random();
            for (int i = 0; i < 4; i++) {
                code += r.nextInt(10);
            }
            v_code = code;
            // phoneService.sendPhone(phone,v_code);
            System.out.println(phone+"---"+v_code);
            return v_code;
        }
    }

    //用户名登录
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping("/loginusername")
    @ResponseBody
    public String tologin(HttpServletRequest request) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        System.out.println(username+"---"+password);
        user=userServiceImp.findbyusername(username);
        if(user==null){
            return "用户或密码不存在!";
        }else if(!user.getU_password().equals(password)){
            return "用户或密码不存在!";
        }else {
            request.getSession().setAttribute("username",user.getU_name());
            return "true";
        }
    }

    //手机号登录
    @RequestMapping("/loginuserphone")
    @ResponseBody
    public String tologin1(HttpServletRequest request){
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");
        System.out.println(phone+"---"+code);
        User user = new User();
        user=userServiceImp.findbyuserphone(phone);
        if(user==null){
            return "用户不存在,请先注册!";
        }else if(!code.equals(v_code)){
            return "验证码不正确!";
        }else {
            request.getSession().setAttribute("username",user.getU_name());
            return "true";
        }
    }

    //注册用户
    @RequestMapping("/registUser")
    @ResponseBody
    public String adduser(HttpServletRequest request){
        String name = request.getParameter("username");
        String passwd1 = request.getParameter("passwd1");
        String passwd2 = request.getParameter("passwd2");
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");
        System.out.println(name+"-"+code+"-"+passwd1+"-"+phone);
        List<User> list = userServiceImp.findalluser();
        Boolean flag=false;
        Boolean flagp=false;
        for(User u:list){
            if(u.getU_name().equals(name)){
                flag=true;
                break;
            }
            if(u.getU_phone().equals(phone)){
                flagp=true;
                break;
            }
        }
        if(flag){
            return "用户名已存在!";
        }else if(!passwd1.equals(passwd2)){
            return "两次密码不一致!";
        }else if(flagp){
            return "预留手机号码存在!";
        }else if(!code.equals(v_code)){
            return "验证码不正确!";
        }else {
            User user = new User();
            user.setU_name(name);
            user.setU_password(passwd1);
            user.setU_phone(phone);
            user.setU_type("U");
            user.setU_browse("");
            userServiceImp.adduser(user);
            return "true";
        }
    }

    //判断是否有用户登录
    @RequestMapping("/verifyTologin")
    @ResponseBody
    public String toVerify(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        if(name == null){
            return "false";
        }else {
            return "true";
        }
    }

    //添加浏览记录
    @RequestMapping("/userBrowse")
    @ResponseBody
    public String touserBrowse(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        User user = userServiceImp.findbyusername(name);
        String sid = request.getParameter("sid");
        String browse = user.getU_browse();
        String[] arry = null;
        Boolean falg=false;
        String x="";//存放重新排序的数组
        int index = -1;
        if(browse.equals("")){
            browse=sid+","+browse;
        }else {
            arry = browse.split(",");
            for (int i=0;i<arry.length;i++){
               if(sid.equals(arry[i])){
                   falg=true;
                   index=i;
                   break;
               }
            }
            if(falg){
                String b = arry[index];
                for(int i=0;i<arry.length;i++){
                    if(i<index){
                        String s = arry[i];
                        arry[i+1]= s;
                    }
                    if(i==index){
                        arry[0]= b;
                        break;
                    }

                }
                for (int i=0;i<arry.length;i++){
                    x+=arry[i]+",";
                }
                browse=x;
            }else {
                browse=sid+","+browse;
            }
        }
        System.out.println(browse);
        int i=userServiceImp.adduserbrowse(browse,user.getU_id());
        if(i==1){
            return "true";
        }else {
            return "false";
        }
    }

    //查看浏览记录
    @RequestMapping("/queryBrowse")
    @ResponseBody
    public String toqueryBrowse(HttpServletRequest request) throws JSONException {
        String name = (String) request.getSession().getAttribute("username");
        User user = userServiceImp.findbyusername(name);
        String browse = user.getU_browse();
        JSONArray array = new JSONArray();
        if(!browse.equals("")){
            String[] str = browse.split(",");
            for(int i=0;i<str.length;i++){
                JSONObject object = new JSONObject();
                String sid = str[i];
                Scenic scenic = scenicServiceImp.findbySid(Integer.parseInt(sid));
                object.put("s_id",scenic.getS_id());
                object.put("p_name",scenic.getS_name());
                object.put("p_address",scenic.getS_address());
                object.put("p_score",scenic.getS_score());
                object.put("p_price",scenic.getS_price());
                array.put(object);
            }
        }
        return array.toString();
    }

    //删除单个浏览记录
    @RequestMapping("/deletesingleBrowse")
    @ResponseBody
    public String todeletesingleBrowse(HttpServletRequest request) throws JSONException {
        String name = (String) request.getSession().getAttribute("username");
        User user = userServiceImp.findbyusername(name);
        String browse = user.getU_browse();
        String[] arry = browse.split(",");
        int index = 0;
        String sid = request.getParameter("sid");
        JSONArray array = new JSONArray();
        String x= "";
        for (int i=0;i<arry.length;i++){
            if(sid.equals(arry[i])){
                index = i;
                System.out.println("找到要删除的记录编号"+index);
                break;
            }
        }
        for (int i=index;i<arry.length-1;i++){
            arry[i]=arry[i+1];
        }
        for (int i=0;i<arry.length-1;i++){
            x+=arry[i]+",";
        }
        userServiceImp.adduserbrowse(x, user.getU_id());
        if(!x.equals("")){
            String[] str = x.split(",");
            for(int i=0;i<str.length;i++){
                JSONObject object = new JSONObject();
                String ssid = str[i];
                Scenic scenic = scenicServiceImp.findbySid(Integer.parseInt(ssid));
                object.put("s_id",scenic.getS_id());
                object.put("p_name",scenic.getS_name());
                object.put("p_address",scenic.getS_address());
                object.put("p_score",scenic.getS_score());
                object.put("p_price",scenic.getS_price());
                array.put(object);
            }
        }
        System.out.println("更新"+array);
        return array.toString();
    }

    //查看已登录信息
    @RequestMapping("/AlreadyLogin")
    @ResponseBody
    public String toAlreadyLogin(HttpServletRequest request) throws JSONException {
        String name = (String) request.getSession().getAttribute("username");
        User user = userServiceImp.findbyusername(name);
        JSONObject object = new JSONObject();
        object.put("username",user.getU_name());
        object.put("userphone",user.getU_phone());
        return object.toString();
    }

    //发送更改预留手机号验证码
    @RequestMapping("/sendmodfiyPhone")
    @ResponseBody
    public String tomodfiyPhone(HttpServletRequest request) throws Exception {
        String phone = request.getParameter("phone");
        String code = "";
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            code += r.nextInt(10);
        }
        v_code=code;
       // phoneService.sendPhone(phone,v_code);
        return v_code;
    }

    //验证手机号修改
    @RequestMapping("/modfiyPhone")
    @ResponseBody
    public String toMphoe(HttpServletRequest request) throws JSONException {
        String name = (String) request.getSession().getAttribute("username");
        String Newphone = request.getParameter("phone");
        String code = request.getParameter("code");
        User user = userServiceImp.findbyuserphone(Newphone);
        if(user!=null){
            return "手机号已存在!";
        }else if(!code.equals(v_code)){
            return "验证码不正确!";
        }else {
            User user1 = userServiceImp.findbyusername(name);
            System.out.println(user1);
            user1.setU_phone(Newphone);
            userServiceImp.updateuser(user1);
            JSONObject object = new JSONObject();
            object.put("username",user1.getU_name());
            object.put("userphone",user1.getU_phone());
            return object.toString();
        }
    }

    //修改密码
    @RequestMapping("/modifyPassword")
    @ResponseBody
    public String tomodifyPassword(HttpServletRequest request){
        String name = (String) request.getSession().getAttribute("username");
        User user = userServiceImp.findbyusername(name);
        String oldpwd = request.getParameter("oldpassword");
        String newpwd = request.getParameter("newpassword1");
        String newpwd2 = request.getParameter("newpassword2");

        if(!oldpwd.equals(user.getU_password())){
            return "旧密码错误!";
        }else if(newpwd.equals(oldpwd)){
            return "新密码不能与旧密码一致!";
        }else if(!newpwd.equals(newpwd2)){
            return "两次输入密码不一致!";
        }else {
            user.setU_password(newpwd);
            userServiceImp.updateuser(user);
            return "true";
        }
    }

    //登录后台系统
    @RequestMapping("/tologinmanagement")
    @ResponseBody
    public String tologinmanagement(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        System.out.println(username+"---"+password);
        user=userServiceImp.findbyusername(username);
        if(user==null){
            return "用户或密码不存在!";
        }else if(!user.getU_password().equals(password)){
            return "用户或密码不存在!";
        }else if(!user.getU_type().equals("A")){
            return "请使用管理员进行登录!";
        } else {
            request.getSession().setAttribute("admin",user);
            return "true";
        }
    }

    //导出用户
    @RequestMapping("/ExportUser")
    public void  toExportUser(HttpServletResponse response) throws IOException {
        //创建一个工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("用户表");
        //获取全部数据
        List<User> list = userServiceImp.findalluser();
        // 在sheet里创建第二行
        HSSFRow row1 = sheet.createRow(0);
        // 创建单元格并设置单元格内容
        row1.createCell(0).setCellValue("用户编码");
        row1.createCell(1).setCellValue("用户名");
        row1.createCell(2).setCellValue("预留手机号");
        row1.createCell(3).setCellValue("用户类型");
        row1.createCell(4).setCellValue("浏览记录");

        HSSFRow rows;
        int rownum = 1;

        for(User s:list){
            rows = sheet.createRow(rownum);
            rows.createCell(0).setCellValue(s.getU_id());
            rows.createCell(1).setCellValue(s.getU_name());
            rows.createCell(2).setCellValue(s.getU_phone());
            rows.createCell(3).setCellValue(s.getU_type());
            rows.createCell(4).setCellValue(s.getU_browse());
            rownum++;
        }

        // 输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=UserData.xls");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
        wb.close();
    }
}
