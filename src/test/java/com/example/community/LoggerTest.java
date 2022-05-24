package com.example.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.slf4j.Logger;
/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-05-23 16:05
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggerTest {
    private static final Logger logger= LoggerFactory.getLogger(LoggerTest.class); //传入的是当前类的名字

    @Test
    public void testLogger(){
        System.out.println(logger.getName());

        logger.debug("debug Log"); //开发时候生效，上线删除
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");

        //然后进行配置  application.yml ?
    }

}