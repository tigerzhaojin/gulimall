package com.tz.mall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/*远程调用别的服务
* 1.引入open-feign
* 2.编写一个接口(interface)，告诉springboot，这个接口需要调用远程服务
* 3.在接口里声明每一个方法都是调用哪个远程服务的请求
* 4.开启远程调用功能*/
@EnableFeignClients(basePackages = "com.tz.mall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
public class MemberApplication {
//启动主程序
    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}
