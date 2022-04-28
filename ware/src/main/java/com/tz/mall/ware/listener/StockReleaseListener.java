package com.tz.mall.ware.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tz.common.to.mq.OrderTo;
import com.tz.common.to.mq.StockDetailTo;
import com.tz.common.to.mq.StockLockedTo;
import com.tz.common.utils.R;
import com.tz.mall.ware.dao.WareSkuDao;
import com.tz.mall.ware.entity.WareOrderTaskDetailEntity;
import com.tz.mall.ware.entity.WareOrderTaskEntity;
import com.tz.mall.ware.feign.OrderFeignService;
import com.tz.mall.ware.service.WareOrderTaskDetailService;
import com.tz.mall.ware.service.WareOrderTaskService;
import com.tz.mall.ware.service.WareSkuService;
import com.tz.mall.ware.vo.OrderVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {
    @Autowired
    WareSkuService wareSkuService;


    @RabbitHandler
    public void handleStockLockRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        System.out.println("正在解锁库存...");
        try {
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
    @RabbitHandler
    public void handleStockCloseRelease(OrderTo orderTo,Message message,Channel channel) throws IOException {
        System.out.println("订单关闭，准备解锁库存...");

        try {
            wareSkuService.unlockStock(orderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
