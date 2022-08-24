package com.walnut.wxts.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CONFIG {

    //推送人员id
    List<String> touserids = new ArrayList<String>(3){{
        add("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        add("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }};

    //微信公众号配置版块
    Map<Object,String> wxconfigs = new HashMap<Object, String>(){{
        put("appID","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        put("appsecret","xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        put("dailyTS_id","xxxxxxxxxxxxxxxxxxxxxxx");  //所用发送模板id
    }};

    //日期版块
    Map<Object,String> dateconfigs = new HashMap<Object, String>(){{
        put("loveday","2020-10-24");//在一起的日子
        put("birthday","2000-09-11");//出生日期
    }};

    //天气版块
    Map<Object,String> weatherconfigs = new HashMap<Object, String>(){{
        put("weatherCity","上海");//查询天气地区 市级或县级都可以（但不止一个地区有相同名称的不行例如高新区）
    }};
}
