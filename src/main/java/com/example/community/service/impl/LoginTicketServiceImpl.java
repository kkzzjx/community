package com.example.community.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.community.model.LoginTicket;
import com.example.community.service.LoginTicketService;
import com.example.community.mapper.LoginTicketMapper;
import org.springframework.stereotype.Service;

/**
* @author 86133
* @description 针对表【login_ticket】的数据库操作Service实现
* @createDate 2022-05-30 17:05:12
*/
@Service
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket>
    implements LoginTicketService{

}




