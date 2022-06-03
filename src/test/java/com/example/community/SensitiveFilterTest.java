package com.example.community;

import com.example.community.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: community
 * @description:
 * @author: zjx
 * @create: 2022-06-03 10:56
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class SensitiveFilterTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void test(){
        String text="在这里，你不能吸毒，你不能嫖娼";
        String res = sensitiveFilter.filter(text);
        System.out.println(res);
    }
}
