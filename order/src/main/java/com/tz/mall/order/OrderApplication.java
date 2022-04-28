package com.tz.mall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 本地事物设置失效问题
 * 同一个对象内事物方法互调，默认被调用的事物的单独设置失效，原因：事物绕过了代理对象
 * 解决方案：
 * 使用代理对象来调用事物方法
 * 1.引入aop， spring-boot-starter-aop
 * 2.开启动态代理  @EnableAspectJAutoProxy,以后所有的动态代理，都是由AspectJ创建，而非原始的jdk
 * 即使没有接口，也能创建动态代理
 * 3.设置exposeProxy = true，对外暴露代理对象
 * 4.用代理对象实现本类方法互调
 *      @Transactional
 *     public void a (){
 *         OrderServiceImpl orderService = (OrderServiceImpl) AopContext.currentProxy();
 *         orderService.b();
 *         orderService.c();
 *     }
 *  Seata控制微服务事物
 *  1. 每个微服务都创建一个undo_log表
 *  2.安装Seata服务器
 *  3.配置代理数据源 mySeataconfig
 *  4.每个微服务都必须导入file.conf和registry.conf，修改file.conf文件里的配置内容
 *      vgroup_mapping.gulimall-order-fescar-service-group = "default"
 *  5.分布式大事物大入口处标注：@GlobalTransactional，每一个被远程调用的小事务用@Transactional即可
 * */

@EnableAspectJAutoProxy (exposeProxy = true)
@EnableFeignClients
@EnableRedisHttpSession
@EnableDiscoveryClient
@EnableRabbit
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
