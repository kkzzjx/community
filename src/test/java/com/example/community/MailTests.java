package com.example.community;

import com.example.community.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.transform.Templates;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-23 20:41
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTests {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine  templateEngine;



    @Test
    public void test(){
        mailClient.sendMail("zhuxuanyouxiang@163.com","test","test");
    }

    @Test
    public void testHtmlMail(){
        //注入模版
        //向模版传参
        Context context=new Context();
        context.setVariable("username","sunday"); //传参

        String con = templateEngine.process("/mail/demo", context); //模版引擎 的模版名称，context对象
        System.out.println(con);
        mailClient.sendMail("zhuxuanyouxiang@163.com","html",con); //发送的是con中的html页面


    }

}
