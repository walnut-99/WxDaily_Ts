package com.walnut.wxts.controller;

import com.walnut.wxts.service.WxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@RestController("/")
public class Controller {

//    @GetMapping("yz")
    public String baotatest(@RequestParam("echostr") String echostr){
        return echostr;
    }

//    @GetMapping("test")
    public void test() throws ParseException {
        WxService.dailyCommonTS();
    }
    @GetMapping()
    public String success(){
        return "success";
    }

}
