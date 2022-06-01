package com.example.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.community.mapper.LoginTicketMapper;
import com.example.community.model.LoginTicket;
import com.example.community.model.User;
import com.example.community.service.UserService;
import com.example.community.mapper.UserMapper;
import com.example.community.utils.CommunityConstant;
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
    implements UserService, CommunityConstant {

    @Resource
    UserMapper userMapper;
    @Resource
    LoginTicketMapper loginTicketMapper;

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
    public Map<String,Object> register(User user) {
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
            return map;
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
        // http://localhost:8080/community/activation/101/code
        context.setVariable("url",domain+contextPath+"activation/"+user.getId().toString()+"/"+user.getActivationCode().toString());

        String con=templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"Welcome to nowcoder!",con);

        return map;
    }

    /**
     * 激活 判断这个用户是否需要激活，然后操作
     * @param userId 用户id
     * @param code 激活状态码
     * @return
     */

    @Override
    public int activation(int userId,String code){
        User user=userMapper.selectById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            user.setStatus(1);
            userMapper.updateById(user);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAIL;
        }


    }


    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param expiredSeconds 超时时间
     * @return 返回的结果用map记录
     */
    @Override
    public Map<String,Object> Login(String username,String password,int expiredSeconds){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user=userMapper.selectOne(queryWrapper);
        if(user==null){
            map.put("usernameMsg","用户名不存在！");
            return map;
        }
        String protectedPassword = CommunityUtil.getMd5(password + user.getSalt());
        if(!protectedPassword.equals(user.getPassword())){
            map.put("passwordMsg","密码错误！");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.getUUID());
        //浏览器传来这个ticket，去库里找，如果发现存在，并且时间对，状态对（0）,那么就能知道这个用户是谁。
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }



    @Override
    public Map<String,Object> getCode(String email,String code){
        Map<String,Object> map=new HashMap<>();
        if(email==null){
            map.put("emailMsg","邮箱未填写！");
        }
        QueryWrapper queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            map.put("emailMsg","该邮箱未被注册！");
            return map;
        }

        //发送html邮件 forget.html
        Context context=new Context();
        context.setVariable("email",user.getEmail());
        context.setVariable("code",code);
        String con=templateEngine.process("/mail/forget",context);
        mailClient.sendMail(user.getEmail(),"Update your password!",con);

        return map;

    }

    @Override
    public Map<String,Object> resetPassword(String email,String password){
        Map<String,Object> map=new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(email)){
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        //验证邮箱
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("email",email);
        User user = userMapper.selectOne(queryWrapper);
        if(user==null){
            map.put("emailMsg","邮箱未注册！");
            return map;
        }

        //重置密码
        user.setPassword(CommunityUtil.getMd5(password+user.getSalt()));
        //持久化
        userMapper.updateById(user);
     //   map.put("user",user);
        return map;
    }


    public LoginTicket findLoginTicket(String ticket){
        if(ticket==null) return null;
        LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);
        return loginTicket;
    }

    public int updateHeader(int userId, String headUrl){
        User user = userMapper.selectById(userId);
        user.setHeaderUrl(headUrl);
        return userMapper.updateById(user);
    }

    @Override
    public Map<String,Object> updatePassword(User user, String originPassword, String newPassword) {
        Map<String,Object> map=new HashMap<>();
        String originPass=CommunityUtil.getMd5(originPassword+user.getSalt());
        if(!originPass.equals(user.getPassword())){
            map.put("olderror","原密码输入错误！");
        }
        String newPass=CommunityUtil.getMd5(newPassword+user.getSalt());
        user.setPassword(newPass);
        userMapper.updateById(user);
        return map;
    }


}




