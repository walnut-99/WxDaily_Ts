package com.walnut.wxts.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.walnut.wxts.config.CONFIG;
import com.walnut.wxts.entity.Model;
import com.walnut.wxts.utils.HttpRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WxService {

    /**
     * 主程序
     * @throws ParseException
     */
    public static void dailyCommonTS() throws ParseException {

        //微信公众号配置版块
        String appID = CONFIG.wxconfigs.get("appID");
        String appsecret = CONFIG.wxconfigs.get("appsecret");
        String dailyTS_id = CONFIG.wxconfigs.get("dailyTS_id");//每日推送模板id
        String getAccessToken_url = "https://api.weixin.qq.com/cgi-bin/token";
        String getAccessToken_params = "grant_type=client_credential&appid="+appID+"&secret="+appsecret;
        String AccessToken_str = HttpRequest.sendGet(getAccessToken_url, getAccessToken_params);
        JSONObject AccessToken_jsonobj = JSON.parseObject(AccessToken_str);
        String access_token = AccessToken_jsonobj.getString("access_token");


        //随机颜色设置
        List<String> colorslist = new ArrayList<>();
        colorslist.add("#09F7F7");
        colorslist.add("#3399ff");
        colorslist.add("#0066ff");
        colorslist.add("#33ff33");
        colorslist.add("#ff0033");
        colorslist.add("#cc33cc");
        colorslist.add("#0997F7");

        //日期版块
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date loveday = sdf.parse(CONFIG.dateconfigs.get("loveday")); //填写:在一起的时间
        Date birthday = sdf.parse(CONFIG.dateconfigs.get("birthday"));//填写:今年生日的时间（后期需做处理）
        Date nowday = new Date();
        String lovedays = String.valueOf(daysBetween(loveday,nowday));//相恋天数
        String birthday_dac = getBirthdayDate(birthday);//距离生日天数

        //天气版块
        String weatherCity = CONFIG.weatherconfigs.get("weatherCity"); //填写:天气查询城市
        String weatherUrl = "https://api.vvhan.com/api/weather";
        String weatherParams = "city="+weatherCity;
        JSONObject weatherJSON = getWeatherJSON(weatherUrl, weatherParams);
        String date = weatherJSON.getString("date");//今日日期
        String type = weatherJSON.getString("type");//天气情况
        String high = weatherJSON.getString("high");//最高温度
        String low = weatherJSON.getString("low");//最低温度
        String temp = low +" - "+ high; //气温
        String tip = weatherJSON.getString("tip");//提示
        String fengli = weatherJSON.getString("fengli");//风力
        String fengxiang = weatherJSON.getString("fengxiang");//风向

        String loveSen = getLoveSentence();//每日一句情话

        //发送请求推送
        for (String touserid : CONFIG.touserids) {
            toPostTs(access_token,date,lovedays,birthday_dac,type,temp,fengli,tip,dailyTS_id,touserid,weatherCity,loveSen,colorslist);
        }
    }

    /**
     * 获取今日天气情况json对象
     */
    public static JSONObject getWeatherJSON(String weatherUrl, String weatherParams){
        String weatherJson = HttpRequest.sendGet(weatherUrl, weatherParams);
        JSONObject jsonObject = JSON.parseObject(weatherJson);
        JSONObject todayWeather = jsonObject.getJSONObject("info");
        return todayWeather;
    }

    /**
     * 根据出生日期获取今年生日
     */
    public static String getBirthdayDate(Date clidate) throws ParseException {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cToday = Calendar.getInstance(); // 存今天
        Calendar cBirth = Calendar.getInstance(); // 存生日
        cBirth.setTime(clidate); // 设置生日
        cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
        int days;
        if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
            // 生日已经过了，要算明年的了
            days = cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
            days += cBirth.get(Calendar.DAY_OF_YEAR);
        } else {
            // 生日还没过
            days = cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR);
        }
        // 输出结果
        if (days == 0) {
            return "今天生日";
        } else {
            return String.valueOf(days);
        }

    }



    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 获取每日一句情话
     */
    public static String getLoveSentence(){
        String s = HttpRequest.sendGet("https://api.vvhan.com/api/love", "type=json");
        JSONObject jsonObject = JSON.parseObject(s);
        String love = jsonObject.getString("ishan");
        return love;
    }


    /**
     * 发送模板信息推送
     *
     */
    public static  void toPostTs(String accessToken_, String date_, String lovedays_, String birthday_dac_, String type_, String temp_, String fengli_,String tip_,String dailyTS_id_,String touserid_,String weatherCity_,String loveSen_,List colorslist){
        String ts_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessToken_;

        Random r = new Random();
        Model date = new Model();
        date.setValue(date_);
        date.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());


        Model lovedays = new Model();
        lovedays.setValue(lovedays_);
        lovedays.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model birthday_dac = new Model();
        birthday_dac.setValue(birthday_dac_);
        birthday_dac.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model type = new Model();
        type.setValue(type_);
        type.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model temp = new Model();
        temp.setValue(temp_);
        temp.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model fengli = new Model();
        fengli.setValue(fengli_);
        fengli.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model tip = new Model();
        tip.setValue(tip_);
        tip.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model place = new Model();
        place.setValue(weatherCity_);
        place.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());

        Model lovesen = new Model();
        lovesen.setValue(loveSen_);
        lovesen.setColor(colorslist.get(r.nextInt(colorslist.size()-1)).toString());


        JSONObject data = new JSONObject();
        data.put("date",date);
        data.put("lovedays",lovedays);
        data.put("birthday_dac",birthday_dac);
        data.put("type",type);
        data.put("temp",temp);
        data.put("fengli",fengli);
        data.put("tip",tip);
        data.put("place",place);
        data.put("lovesen",lovesen);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser",touserid_);
        jsonObject.put("template_id",dailyTS_id_);
        jsonObject.put("url","http://weixin.qq.com/download");
        jsonObject.put("topcolor","#FF0000");
        jsonObject.put("data",data);

        HttpRequest.sendPost(ts_url,jsonObject.toString());
    }


}
