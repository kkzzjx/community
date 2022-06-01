package com.example.community.controller;

import com.example.community.model.User;
import com.example.community.service.UserService;
import com.example.community.utils.CommunityUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.example.community.utils.CommunityConstant.*;

/**
 * @program: community
 * @description: 登录注册
 * @author: zjx
 * @create: 2022-05-23 22:52
 **/
@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    Producer producer;

    @Value("${community.path.contextPath}")
    private String contentPath;

    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @GetMapping("/forget")
    public String getForgetPasswordPage(){
        return "site/forget";
    }

    @PostMapping("/register")
    public String register(User user, Model model){
        Map<String, Object> map = userService.register(user);
        if(map==null|| map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封邮件，请尽快激活！");
            model.addAttribute("target","index");
            return "/site/operate-result";
        }
        else{
           model.addAttribute("usernameMsg",map.get("usernameMsg"));
           model.addAttribute("passwordMsg",map.get("passwordMsg"));
           model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }

    }
    // http://localhost:8080/community/activation/101/code
    @GetMapping("/activation/{userId}/{code}")  //查询请求，用get
    public String activation(Model model, @PathVariable("userId")int userId,@PathVariable("code")String code){
        int activation = userService.activation(userId, code);
        if(activation==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号已经可以正常使用了");
            model.addAttribute("target","/login");
        }
        else if(activation==ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活");
            model.addAttribute("target","/login");
        }
        else{
            model.addAttribute("msg","激活失败,您提供的激活码不正确");
            model.addAttribute("target","/index");
        }

        return "/site/operate-result";
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码文字
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        //将验证码存入session
        //服务端需要暂存验证码，这里使用session实现
        session.setAttribute("kaptcha",text);
        //将图片输出到浏览器
        response.setContentType("image/png");
        try{
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 访问登录页面：
     *     点击顶部区域内的连接，打开登录页面
     * 登录：
     *      验证账号、密码、验证码
     *      成功时，生成登录凭证，发给客户端  LoginTicket
     *      失败时，跳转回登录页
     * 退出：
     *      将登录凭证修改为失效状态
     *      跳转到王炸你首页
     */


    @PostMapping("/login")
    public String login(String username,String password,String code,boolean rememberme,Model model,
                        HttpSession session,HttpServletResponse response){
        //验证码现在放在Session中，所以要从Session中取出来
        //登录成功以后，需要将ticket以cookie的形式发放给客户端进行保存，所以需要Response
        //而这几个参数是放在request中的（前端可拿）
//        if(StringUtils.isAnyBlank(username,password,code)){
//            model.addAttribute("errorMsg","信息不完整！")
//        }

        //检查验证码
        String kaptcha = ((String) session.getAttribute("kaptcha"));
        if(StringUtils.isAnyBlank(kaptcha,code)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确！");
            return "/site/login";
        }

        //检查账号、密码
        int expiredSeconds=rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> loginResult = userService.Login(username, password,expiredSeconds);
        if(loginResult.containsKey("ticket")){
            Cookie cookie=new Cookie("ticket",(String) loginResult.get("ticket"));
            cookie.setPath(contentPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{ //登录失败
            model.addAttribute("usernameMsg",loginResult.get("usernameMsg"));
            model.addAttribute("passwordMsg",loginResult.get("passwordMsg"));
            return "/site/login";
        }

    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/login"; //重定向用于第二次请求；服务器将这次请求处理完了，给浏览器一个相应，并建议浏览器访问另外的组件；否则还是这个请求（path）确返回了另一个页面，会引起疑惑
    }

    @GetMapping("/forget/getCode")
    public String getCode(Model model,String email,HttpSession session){
        String code=new Random(System.currentTimeMillis()).toString().substring(0,4);
        session.setAttribute("code_"+email,code);
        Map<String, Object> map = userService.getCode(email, code);

        if(!map.isEmpty()){
            model.addAttribute("emailMsg",map.get("emailMsg"));
        }
        return "/site/forget";

    }

    @PostMapping("/forget/resetPassword")
    public String resetPassword(Model model,String email,String code,String password,HttpSession session){
        Map<String, Object> map =new HashMap<>();
        if(StringUtils.isBlank(code)){
            model.addAttribute("codeMsg","验证码为空！");
            return "/site/forget";
        }
        String  rightCode=(String) session.getAttribute("code_"+email);

        if(!code.equalsIgnoreCase(rightCode)){
            model.addAttribute("codeMsg","验证码错误！");
            return "/site/forget";
        }

        map=userService.resetPassword(email, password);

        if(!map.isEmpty()){
            model.addAttribute("emailMsg",map.get("emailMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/forget";
        }else{
            return "redirect:/site/login";
        }






    }



}
