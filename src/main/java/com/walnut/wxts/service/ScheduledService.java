package com.walnut.wxts.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
public class ScheduledService {

    //每日早上八点触发,需要修改请百度cron表达式
    @Scheduled(cron = "0 0 8 * * ?")
    public void wxts() throws ParseException {
        WxService.dailyCommonTS();
    }

}
