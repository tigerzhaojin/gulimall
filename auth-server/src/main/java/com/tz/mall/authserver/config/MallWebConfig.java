package com.tz.mall.authserver.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebConfig implements WebMvcConfigurer {

//    视图映射
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
//        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
