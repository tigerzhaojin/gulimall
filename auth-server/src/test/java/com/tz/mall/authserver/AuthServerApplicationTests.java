package com.tz.mall.authserver;

import com.tz.mall.authserver.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServerApplicationTests {
    @Autowired
    @Qualifier("userIvy")
    User userIvy;

    @Autowired
    @Qualifier("userTiger")
    User userTiger;


//    public final User userCons;
//    @Autowired
//    @Qualifier("userCons")
//    public void userCons(){
//        this.userCons.setAge(5);
//        this.userCons.setId(3);
//        this.userCons.setName("chris");
//    }
    @Test
    public void contextLoads() {
    }
    @Test
    public void userBeanTest(){
        System.out.println(userIvy.toString());
        System.out.println(userTiger.toString());
//        System.out.println(userCons.toString());
    }

}
