package com.example.community.controller;

import com.example.community.annotation.LoginRequired;
import com.example.community.mapper.UserMapper;
import com.example.community.model.User;
import com.example.community.service.UserService;
import com.example.community.utils.CommunityUtil;
import com.example.community.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;

/**
 * @program: community
 * @description: 用户个人设置
 * @author: zjx
 * @create: 2022-05-31 20:15
 **/
@Controller
@RequestMapping("/user")
public class UserController {
    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "site/setting";
    }

    /**
     * 上传头像
     * SpringMVC提供了专有类型MultipartFile来接收上传文件
     * @return
     */
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile file, Model model){
        if(file==null){
            model.addAttribute("error","还没有选择图片");
            return "site/setting";
        }
        //给文件生成一个随机名字， 后缀不能变
        String fileName=file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","格式错误");
            return "site/setting";
        }

        //生成随机文件名
        fileName= CommunityUtil.getUUID()+"."+suffix;
        File dest=new File(uploadPath+"/"+fileName);

        //将上传文件的内容写入这个File
        try {
            //存储文件
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败，服务器发生异常！",e);
        }

        //如果存成功了，更新当前用户头像的路径（提供web访问路径，而不是本地）
        //http://localohost:8080/community/user/header/xxx.png
        //当前用户是谁
        User user = hostHolder.getUser();
        String headerUrl=domain+contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }

    //获取头像  向浏览器反映的是二进制的图片数据 要向浏览器通过流写出
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //服务器存放名字
        fileName=uploadPath+"/"+fileName;
        //读取后缀  用于contentPath
        String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
        //响应图片
        response.setContentType("image/"+suffix);

        try {
            OutputStream os=response.getOutputStream();
            FileInputStream fis=new FileInputStream(fileName);
            byte[] buffer=new byte[1024];
            int b=0;
            while((b=fis.read(buffer))!=-1){
                os.write(buffer,0,b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 修改密码功能
     * - 账号设置页面，填写原密码和新密码，点击保存时将数据提交给服务器
     * - 服务器检查原密码是否正确，若正确则将密码修改为新密码，并重定向到退出功能，强制用户重新登录；若错误则返回带账号设置页面，给予响应提示
     清空session后到登录？？
     */
    @LoginRequired
    @PostMapping("/updatePassword")
    String updatePassword(String originPassword, String newPassword, String againPassword, HttpSession session, Model model) {
        if (StringUtils.isBlank(originPassword)) {
            model.addAttribute("olderror", "原密码不能为空");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newerror", "新密码不能为空");
            return "site/setting";
        }
        if (StringUtils.isBlank(againPassword)) {
            model.addAttribute("againerrpr", "确认密码不能为空");
            return "site/setting";
        }
        if (!newPassword.equals(againPassword)) {
            model.addAttribute("againerror", "密码不一致");
            return "site/setting";
        }

        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user, originPassword, newPassword);

        if(!map.isEmpty()){
            model.addAttribute("olderror", "原密码输入错误");
            return "site/setting";
        }
        return "redirect:/logout";


    }
}
