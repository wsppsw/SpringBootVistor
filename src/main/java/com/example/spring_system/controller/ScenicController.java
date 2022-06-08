package com.example.spring_system.controller;

import com.example.spring_system.domain.Order;
import com.example.spring_system.domain.Scenic;
import com.example.spring_system.domain.Station;
import com.example.spring_system.domain.User;
import com.example.spring_system.service.Imp.OrderServiceImp;
import com.example.spring_system.service.Imp.ScenicServiceImp;
import com.example.spring_system.service.Imp.StationServiceImp;
import com.example.spring_system.service.Imp.UserServiceImp;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

@Controller
public class ScenicController {

    @Autowired
    private ScenicServiceImp scenicServiceImp;

    @Autowired
    private StationServiceImp stationServiceImp;

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private OrderServiceImp orderServiceImp;

    private  String sid="";

    private String imgsrc="";

    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping("/all")
    @ResponseBody
    public PageInfo<Scenic> findall(){
        PageInfo<Scenic> scenicPageInfo = null;
        scenicPageInfo=scenicServiceImp.findallPage(1, 10);
        return  scenicPageInfo;
    }

    //主界面
    @RequestMapping("/index")
    public String tomain(){
        return "main/vue_list";
    }

    //景点推荐
    @RequestMapping("/main/travel")
    public String tolist(){
        return "main/vue_main_list";
    }

    //具体的景区
    @RequestMapping("/main/scenic")
    public String toscenic() {
        return "main/vue_find";
    }

    //热门景点
    @RequestMapping("/main/hot")
    public String toHot(){
        return "main/vue_hot";
    }

    //浏览记录
    @RequestMapping("/user/browse")
    public String tobrowse(){
        return "main/vue_browse";
    }

    //订单页面
    @RequestMapping("/user/order")
    public String toorder(){
        return "main/vue_order";
    }

    //管理后台主界面
    @RequestMapping("/manage/management")
    public String toManagement(){
        return "main/vue_management_main";
    }

    //管理后台景点添加
    @RequestMapping("/manage/add")
    public String tomanageAdd(){
        return "main/vue_man_add";
    }

    //管理后台景点查询
    @RequestMapping("/manage/query")
    public String tomanageQuery(){
        return "main/vue_man_query";
    }

    //管理后台查票退票
    @RequestMapping("/manage/order")
    public String tomanageshoworder(){
        return "main/vue_man_order";
    }

    //编号查询
    @RequestMapping("/findsid")
    @ResponseBody
    public Scenic toSenic(HttpServletRequest request){
        String id = request.getParameter("id");
       // System.out.println("获取编号"+id);
        sid=id;
        if(id.equals("")){
            id="1";
        }
        Integer sid= Integer.parseInt(id);
        Scenic scenic = scenicServiceImp.findbySid(sid);
        System.out.println(scenic);
        return scenic;
    }

    //获取站名
    @RequestMapping("/tostation")
    @ResponseBody
    public List<Station> tostation(){
        List<Station> list = stationServiceImp.findallStation();
        return list;
    }

    //城市地位
    @RequestMapping("/city")
    @ResponseBody
    public String toCity() throws JSONException {
       /* String urls="http://ip.ws.126.net/ipquery?ie=utf-8";
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(urls);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GBK"));
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
        String city=result.toString().substring(18,21);
        return city;*/
        //容错处理
        String urls="http://ip.ws.126.net/ipquery?ie=utf-8";
        String code ="";
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(urls);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
            httpUrlConnection.setConnectTimeout(300000);
            httpUrlConnection.setReadTimeout(300000);
            httpUrlConnection.connect();
            code = new Integer(httpUrlConnection.getResponseCode()).toString();
            BufferedReader in = null;
            if(code.equals("200")){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GBk"));
            }else {
                urls = "http://api.map.baidu.com/location/ip?ak=vE5EkHqq2Q6wMRIdX8FSGChnEKj982mw";
                URL url1 = new URL(urls);
                URLConnection conns = url1.openConnection();
                in = new BufferedReader(new InputStreamReader(conns.getInputStream(),"utf-8"));
            }
            //String message = httpUrlConnection.getResponseMessage();
            // System.out.println("getResponseCode code ="+ code);
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
        JSONObject jsonObject = null;
        String city = "";
        if(code.equals("200")){
            String[] data = result.toString().split("=");
            jsonObject = new JSONObject(data[3]);
            city = jsonObject.get("city").toString();
        }else {
            jsonObject = new JSONObject(result.toString());
            city = jsonObject.getJSONObject("content").getJSONObject("address_detail").getString("city");
        }
        return city;
    }

    //今日天气预报
    @RequestMapping("/weather")
    @ResponseBody
    public String toWeather(HttpServletRequest request) throws JSONException {
        String city = null;
        city =request.getParameter("city");
        if(city.equals("")){
            city="张家口";
        }
        System.out.println("城市定位："+city);
        String urls = "http://wthrcdn.etouch.cn/weather_mini?city="+city;
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
        JSONObject object = new JSONObject(result.toString());
        String[] arr = new String[3];
        JSONObject object1 =object.getJSONObject("data");
        JSONObject yes = object1.getJSONObject("yesterday");
        JSONArray array = object1.getJSONArray("forecast");
        arr[0]=object1.getString("city");
        arr[1]=object1.getString("ganmao");
        arr[2]=object1.getString("wendu")+"℃";
        JSONObject jsonObject2 = array.getJSONObject(0);
        String type=jsonObject2.getString("type").toString();
        System.out.println(type+"--"+arr[2]);
        JSONObject obj = new JSONObject();
        obj.put("wendu",arr[2]);
        obj.put("type",type);
        return obj.toString();
    }

    //未来五天天气预报
    @RequestMapping("/weatherFive")
    @ResponseBody
    public String toweatherFive(HttpServletRequest request) throws JSONException {
        String city = null;
        city =request.getParameter("scity");
        //System.out.println("初次景点城市定位："+city);
        if(city.equals("undefined")){
            Scenic scenic = scenicServiceImp.findbySid(Integer.parseInt(sid));
            city=scenic.getS_city();
        }
        System.out.println("景点城市定位："+city);
        String urls = "http://wthrcdn.etouch.cn/weather_mini?city="+city;
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
        JSONObject object = new JSONObject(result.toString());
        JSONObject object1 = object.getJSONObject("data");
        JSONArray array = object1.getJSONArray("forecast");
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<array.length();i++){
            JSONObject jsonObject2 = array.getJSONObject(i);
            JSONObject jsonObject = new JSONObject();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, i);
            String date1 = dateFormat.format(c.getTime());
            String type = jsonObject2.getString("type");
            String high = jsonObject2.getString("high").substring(3);
            String low = jsonObject2.getString("low").substring(3);
            jsonObject.put("date",date1);
            jsonObject.put("type",type);
            jsonObject.put("high",high);
            jsonObject.put("low",low);
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    //推荐的景点(前5个轮播景点)
    @RequestMapping("/recommend")
    @ResponseBody
    public List<Scenic> torecommend(){
        Integer num=5;
        List<Scenic> list=scenicServiceImp.findallLimit(num);
        List<Scenic> listlimit = new ArrayList<>();
        for(int i=0;i<num;i++){
            Scenic scenic = new Scenic();
            scenic=list.get(i);
            String[] img = list.get(i).getS_imgsrc().split(",");
            scenic.setS_imgsrc("../imgs/"+img[0]);
            listlimit.add(scenic);
        }
        System.out.println(listlimit);
        return listlimit;
    }

    //推荐的景点(前10个景点)
    @RequestMapping("/recommendTen")
    @ResponseBody
    public List<Scenic> torecommendTen(){
        Integer num=10;
        List<Scenic> list=scenicServiceImp.findallLimit(num);
        List<Scenic> listlimit = new ArrayList<>();
        for(int i=0;i<num;i++){
            Scenic scenic = new Scenic();
            scenic=list.get(i);
            String[] img = list.get(i).getS_imgsrc().split(",");
            scenic.setS_imgsrc("../imgs/"+img[1]);
            listlimit.add(scenic);
        }
        System.out.println(listlimit);
        return listlimit;
    }

    //热门景点分页
    @RequestMapping("/hotPage")
    @ResponseBody
    public PageInfo<Scenic> tohotpage(HttpServletRequest request){
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        return scenicServiceImp.findallPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
    }

    //搜查景点
    @RequestMapping("/findPage")
    @ResponseBody
    public PageInfo<Scenic> tofindpage(HttpServletRequest request) throws JSONException {
        String key = request.getParameter("key");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        Integer Num = Integer.parseInt(pageNum);
        Integer Size = Integer.parseInt(pageSize);
        PageInfo<Scenic> pageInfo = null;

        int err=0;
        if(scenicServiceImp.findbyNamePage(key,Num,Size).getTotal()!=0){
            pageInfo = scenicServiceImp.findbyNamePage(key,Num,Size);
            System.out.println("名字");
        }else if(scenicServiceImp.findbyAddressPage(key,Num,Size).getTotal()!=0){
            pageInfo = scenicServiceImp.findbyAddressPage(key,Num,Size);
            System.out.println("地址");
        }else if(scenicServiceImp.findbyScorePage(key,Num,Size).getTotal()!=0){
            pageInfo = scenicServiceImp.findbyScorePage(key,Num,Size);
            System.out.println("评分");
        }else if(key.matches("^[0-9]*$")){
            pageInfo = scenicServiceImp.findbyPricePage(Double.parseDouble(key),Num,Size);
            System.out.println("价格");
        }else {
            pageInfo = scenicServiceImp.findallPage(Num,Size);
            System.out.println("失败！");
            err=1;
        }
        pageInfo.setPrePage(err);
        return  pageInfo;
    }

    //图片上传与下载
    @RequestMapping("/fileup")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request) throws IOException {
        // replaceAll 用来替换windows中的\\ 为 /
        System.out.println("名称："+multipartFile.getOriginalFilename()+"-"+multipartFile.getContentType());
        String[] filename =multipartFile.getOriginalFilename().split("\\.");
        System.out.println("截取类型"+filename[0]+"."+filename[1]);
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        System.out.println("更名后"+uuid);
        imgsrc+=uuid+"."+filename[1]+",";
        System.out.println(imgsrc);
        // 文件存储位置，文件的目录要存在才行，可以先创建文件目录，然后进行存储
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\imgs\\" +uuid+"."+filename[1];
        String filePath1 = System.getProperty("user.dir")+"\\target\\classes\\static\\imgs\\" +uuid+"."+filename[1];
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 文件存储
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File result = new File(filePath1.replaceAll("\\\\", "/"));//需要复制到的路径，以及图片的新命名+格式
        FileInputStream input = new FileInputStream(filePath.replaceAll("\\\\", "/"));//需要复制的原图的路径+图片名+ .png(这是该图片的格式)
        FileOutputStream out = new FileOutputStream(result);
        byte[] buffer = new byte[100];//一个容量，相当于打水的桶，可以自定义大小
        int hasRead = 0;
        while ((hasRead = input.read(buffer)) > 0) {
            out.write(buffer, 0, hasRead);//0：表示每次从0开始
        }
        // System.out.println(result.getAbsolutePath());
        input.close();//关闭
        out.close();

        return file.getAbsolutePath().replaceAll("\\\\", "/");
        //return FileUploadUtil.upload(multipartFile).replaceAll("\\\\", "/");
    }

    //添加景点
    @RequestMapping( "/addScenic" )
    @ResponseBody
    public String toaddScenic(HttpServletRequest request){
        Map<String,String[]> map = request.getParameterMap();
        String name = map.get("s_name")[0];
        String address = map.get("s_address")[0];
        String price = map.get("s_price")[0];
        String score = map.get("s_score")[0];
        String city = map.get("s_city")[0];
        String coordinate = map.get("s_coordinate")[0];
        String introduce = map.get("s_introduce")[0];
        String start = map.get("startselect")[0];
        String end = map.get("endselect")[0];
        String time = start+"~"+end;

        Scenic scenic = new Scenic();
        scenic.setS_name(name);
        scenic.setS_address(address);
        scenic.setS_city(city);
        scenic.setS_coordinate(coordinate);
        scenic.setS_price(Double.parseDouble(price));
        scenic.setS_score(score);
        scenic.setS_introduce(introduce);
        scenic.setS_time(time);
        scenic.setS_imgsrc(imgsrc);
        System.out.println(scenic);
        int i=scenicServiceImp.addScenic(scenic);

        if(i==1){
            imgsrc="";
            return "true";
        }else {
            return "false";
        }
    }

    //下架景点
    @RequestMapping("/delScenic")
    @ResponseBody
    public String todelScenic(HttpServletRequest request){
        String sid = request.getParameter("sid");

        List<Order> listo = orderServiceImp.findorderbysid(Integer.parseInt(sid));

        if(listo!=null){
            return "false";
        }else {
            int j = scenicServiceImp.deleteScenic(Integer.parseInt(sid));
            if (j == 1) {
                List<User> list = userServiceImp.findalluser();
                for (User user : list) {
                    Boolean flag = false;//判断是否存在浏览记录中
                    boolean flag1 = false;//判断是否存在订单
                    String browse = user.getU_browse();
                    String[] arry = browse.split(",");
                    int index = 0;
                    String x = "";
                    for (int i = 0; i < arry.length; i++) {
                        if (sid.equals(arry[i])) {
                            index = i;
                            flag = true;
                            System.out.println("找到要删除的记录编号" + index);
                            break;
                        }
                    }

                    if (flag) {
                        for (int i = index; i < arry.length - 1; i++) {
                            arry[i] = arry[i + 1];
                        }
                        for (int i = 0; i < arry.length - 1; i++) {
                            x += arry[i] + ",";
                        }
                        userServiceImp.adduserbrowse(x, user.getU_id());
                    }

                }
                return "true";
            } else {
                return "false";
            }
        }
    }

    //修改景点
    @RequestMapping("/modifyScenic")
    @ResponseBody
    public String toModfiysc(HttpServletRequest request){
        Map<String,String[]> map = request.getParameterMap();
        String id = map.get("s_id")[0];
        String name = map.get("s_name")[0];
        String address = map.get("s_address")[0];
        String price = map.get("s_price")[0];
        String score = map.get("s_score")[0];
        String city = map.get("s_city")[0];
        String coordinate = map.get("s_coordinate")[0];
        String introduce = map.get("s_introduce")[0];
        String time = map.get("s_time")[0];
        String img = map.get("s_imgsrc")[0];

        Scenic scenic = new Scenic();
        scenic.setS_id(Integer.parseInt(id));
        scenic.setS_name(name);
        scenic.setS_address(address);
        scenic.setS_city(city);
        scenic.setS_coordinate(coordinate);
        scenic.setS_price(Double.parseDouble(price));
        scenic.setS_score(score);
        scenic.setS_introduce(introduce);
        scenic.setS_time(time);
        scenic.setS_imgsrc(img);
        int i = scenicServiceImp.updateScenic(scenic);
        if(i==1){
            return "true";
        }else {
            return "false";
        }
    }

    //导出景点
    @RequestMapping("/ExportScenic")
    @ResponseBody
    public void toexportScenic(HttpServletResponse response) throws IOException {
        //创建一个工作簿对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("景点表");
        //获取全部数据
       // List<Student> list = serviceStudentImp.findall();
        List<Scenic> list = scenicServiceImp.findall();
        // 在sheet里创建第二行
        HSSFRow row1 = sheet.createRow(0);
        // 创建单元格并设置单元格内容
        row1.createCell(0).setCellValue("编号");
        row1.createCell(1).setCellValue("景点名称");
        row1.createCell(2).setCellValue("景点地址");
        row1.createCell(3).setCellValue("所在市县");
        row1.createCell(4).setCellValue("景区评分");
        row1.createCell(5).setCellValue("景区票价");
        row1.createCell(6).setCellValue("开放时间");
        row1.createCell(7).setCellValue("景点坐标");
        row1.createCell(8).setCellValue("景点简介");

        HSSFRow rows;
        int rownum = 1;

        for(Scenic s:list){
            rows = sheet.createRow(rownum);
            rows.createCell(0).setCellValue(s.getS_id());
            rows.createCell(1).setCellValue(s.getS_name());
            rows.createCell(2).setCellValue(s.getS_address());
            rows.createCell(3).setCellValue(s.getS_city());
            rows.createCell(4).setCellValue(s.getS_score());
            rows.createCell(5).setCellValue(s.getS_price());
            rows.createCell(6).setCellValue(s.getS_time());
            rows.createCell(7).setCellValue(s.getS_coordinate());
            rows.createCell(8).setCellValue(s.getS_introduce());
            rownum++;
        }

        // 输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=ScenicData.xls");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
        wb.close();
    }


}
