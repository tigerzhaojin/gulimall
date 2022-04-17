package com.tz.mall.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


@Configuration
public class MyRabbitConfig {
    @Autowired
    RabbitTemplate rabbitTemplate;

//    配置完成后，将把对象转换为json
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

//    定制rabbitTemplate
    @PostConstruct //MyRabbitConfig对象创建完成后，调用这个方法
    public void initRabbitTemplate(){
//      1.  设置确认回调.只需要消息抵达broker，ack就为true
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                String str="CorrelationData["+correlationData+"]==>ack["
                        +ack+"]==>cause["+"]";
                System.out.println(str);
            }
        });
//      2.  设置消息抵达队列,确认回调. 消息没有投递到队列，再回调，类似于失败回调.否则不回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText,
                                        String exchange, String routingKey) {
                System.out.println("failMesage: Message ["+message+"]=>replyCode["+replyCode
                        +"]=> replyText["+replyText+"]=> exchange["+exchange+"]=>routingKey["+routingKey+"]");
            }
        });


    }

}
