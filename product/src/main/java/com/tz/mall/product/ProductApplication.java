package com.tz.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 页面修改实时更新
 * 1)引入dev-tools*/
@EnableFeignClients(basePackages ="com.tz.mall.product.feign" )
@EnableDiscoveryClient
@MapperScan("com.tz.mall.product.dao")
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
