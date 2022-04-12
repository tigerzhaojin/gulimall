package com.tz.mall.authserver.config;

import com.tz.mall.authserver.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
    @Bean("userTiger")
    public User user(){
        return new User(1,"tiger",45);
    }

    @Bean("userIvy")
    public User usera(){
        return new User(2,"ivy",44);
    }
    @Bean("userCons")
    public User userCons(){
        return new User();
    }
}
