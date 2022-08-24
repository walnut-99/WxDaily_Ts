# WxDaily_Ts
## (Java版本微信测试号每日推送,支持多账户推送)

修改config/CONFIG中内容  

挂载至宝塔或者服务器中即可使用  

网上此类教程极多 不多讲解  
  
```
{{date.DATA}} 
今天是xx和xx相恋的第{{lovedays.DATA}}天 
距离大宝贝的生日还有{{birthday_dac.DATA}}天 
今日{{place.DATA}}天气 {{type.DATA}} 
今日{{place.DATA}}温度 {{temp.DATA}}
风力 {{fengli.DATA}} 
{{tip.DATA}}
{{lovesen.DATA}}  
```
### 原理讲解(其实很简单就是发两次请求)：
1. 第一次请求根据appID和appsecret获取access_token
2. 配置请求相关body参数,参数要求请看官方文档https://mp.weixin.qq.com/debug/cgi-bin/readtmpl?t=tmplmsg/faq_tmpl
3. 第二次请求wx服务器发送模板消息需携带access_token及body参数(见第二条)
  
测试图：  
<img src="https://s1.ax1x.com/2022/08/24/vgk07F.jpg" width=400 height=800 />
