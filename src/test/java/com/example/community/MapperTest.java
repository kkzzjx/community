package com.example.community;

import com.example.community.mapper.LoginTicketMapper;
import com.example.community.model.LoginTicket;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-30 17:18
 **/

@SpringBootTest
public class MapperTest {
    @Resource
    private LoginTicketMapper loginTicketMapper;

    @Test
    void testLoginTicket(){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(1);
        loginTicket.setStatus(1);
        loginTicket.setTicket("abc");
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));

        loginTicketMapper.insertLoginTicket(loginTicket);

        LoginTicket loginTicket1 = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket1);

        int res = loginTicketMapper.updateStatus("abc", 2);
        loginTicket1 = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket1);
    }
}
