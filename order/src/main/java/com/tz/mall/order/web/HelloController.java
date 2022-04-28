package com.tz.mall.order.web;

import com.tz.mall.order.entity.OrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

@Controller
public class HelloController {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @RequestMapping("{page}.html")
    public String gotoPage(@PathVariable("page") String page){
        return page;
    }

    @RequestMapping("/ordertest")
    @ResponseBody
    public String orderTest(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setModifyTime(new Date());
        rabbitTemplate.convertAndSend("order-event-exchange",
                "order.create.order",orderEntity);
        return "ok";
    }
}
