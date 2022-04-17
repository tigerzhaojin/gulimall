package com.tz.mall.order;

import com.tz.mall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void createExchange() {
        DirectExchange directExchange =
                new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("exchange：[{}]创建成功","hello-java-exchange");
    }

    @Test
    public void createQueue(){
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("queue：[{}]创建成功","hello-java-exchange");
    }
    @Test
    public void createBinding(){
        Binding binding = new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello-java-exchange",
                "hello.java",null);
        amqpAdmin.declareBinding(binding);
        log.info("binding：[{}]创建成功","hello.java");
    }
    @Test
    public void sendMsg(){
//        OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
//        reasonEntity.setId(2L);
//        reasonEntity.setCreateTime(new Date());
//        reasonEntity.setName("chris");
        for (int i = 0; i < 10; i++) {
            OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
            reasonEntity.setId(2L);
            reasonEntity.setCreateTime(new Date());
            reasonEntity.setName("chris"+i);
            rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",reasonEntity);
        }


    }

}
