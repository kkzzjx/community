package com.example.community.utils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.standard.expression.MessageExpression;

import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-23 20:03
 **/
@Component
public class MailClient {
    //private static final Logger logger= LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    //把配置文件中的 属性注入
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to,String subject,String content){

        try{
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true); //支持html
            mailSender.send(helper.getMimeMessage());
        }
        catch (MessagingException e){
            e.printStackTrace();
        }

    }






}
