package com.example.community.mapper;

import com.example.community.model.LoginTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
* @author 86133
* @description 针对表【login_ticket】的数据库操作Mapper
* @createDate 2022-05-30 17:05:12
* @Entity com.example.community.model.LoginTicket
*/
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {
    @Insert({"insert into login_ticket(user_id,ticket,status,expired) ",
                "values(#{userId},#{ticket},#{status},#{expired})"

    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket ",
            "where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "<script>",
            "update login_ticket ",
            "set status=#{status}",
            "where ticket=#{ticket}",
            "<if test=\"ticket!=null\">",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket,int status);

}




