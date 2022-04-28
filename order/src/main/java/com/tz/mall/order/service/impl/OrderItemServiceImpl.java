package com.tz.mall.order.service.impl;

import com.rabbitmq.client.Channel;
import com.tz.mall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.order.dao.OrderItemDao;
import com.tz.mall.order.entity.OrderItemEntity;
import com.tz.mall.order.service.OrderItemService;

@RabbitListener(queues = {"hello-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }
    /**
     * queues= 声明需要监听的所有队列，是一个数组
     * queues,允许多个客户端监听消息，但只有一个客户端会收到消息。消息收到后，队列删除消息
     * //  消费端确认--保证每个消息被正确接收，才能在broker端删除该消息
     * // 默认是自动确认，只要收到消息就会删除所有。要修改为手动确认（处理一条，删除一条）
     * */
    @RabbitHandler
    public void recevieMsg(Message message,
                           OrderReturnReasonEntity reasonEntity,
                           Channel channel) throws InterruptedException, IOException {

        byte[] body = message.getBody();
        MessageProperties messageProperties = message.getMessageProperties();
//        System.out.println("接收到的消息："+message);
//        System.out.println("接收到的消息体："+body);
        System.out.println("接收到的消息对象："+reasonEntity);
//        获取消息tag
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        挨个确认收到消息，在服务器端删除
        channel.basicAck(deliveryTag,false);
        System.out.println("签收："+deliveryTag);
//        if(deliveryTag%2==0){
//            channel.basicAck(deliveryTag,false);
//            System.out.println("签收："+deliveryTag);
//        } else {
//            System.out.println("没有签收："+deliveryTag);
//        }

//        Thread.sleep(3000);
    }

    @RabbitHandler
    public void recevieMsg1(Message message,

                           Channel channel) throws InterruptedException {

        byte[] body = message.getBody();
        MessageProperties messageProperties = message.getMessageProperties();
//        System.out.println("接收到的消息："+message);
//        System.out.println("接收到的消息体："+body);
        System.out.println("接收到的消息对象："+message);
//        Thread.sleep(3000);
    }

}