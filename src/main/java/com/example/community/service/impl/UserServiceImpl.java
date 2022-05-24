package com.example.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.community.model.User;
import com.example.community.service.UserService;
import com.example.community.mapper.UserMapper;
import com.example.community.utils.CommunityUtil;
import com.example.community.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.*;

/**
* @author 86133
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-05-22 18:37:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.contextPath}")
    private String contextPath;

    @Override
    public User findUserById(int userId) {

        return userMapper.selectById(userId);
    }

    @Override
    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        // 各个属性都要存在
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }

        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //账号、邮箱不能重复
        QueryWrapper<User> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("username",user.getUsername());

        if(!userMapper.selectList(queryWrapper1).isEmpty()){
            map.put("usernameMsg","用户名重复");
            return map;
        }
        QueryWrapper<User> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.eq("email",user.getEmail());
        if(!userMapper.selectList(queryWrapper2).isEmpty()){
            map.put("emailMsg","邮箱重复");
        }



        //注册用户 密码需要加盐md5（先设置盐值）
        user.setSalt(CommunityUtil.getUUID().substring(0,5));
        user.setPassword(CommunityUtil.getMd5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.getUUID());
        Random random=new Random();
        int i=random.nextInt(1000);
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",i));
        user.setCreateTime(new Date());
        userMapper.insert(user);
        //调用insert以后 就有id了（配置）

        //发送html邮件 activation.html
        Context context=new Context();
        context.setVariable("email",user.getEmail());
        // http://localhost:8080/activation/101/code
        context.setVariable("url",domain+contextPath+"activation/"+user.getId().toString()+"/"+user.getActivationCode().toString());

        String con=templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"Welcome to nowcoder!",con);

        return map;
    }


}




