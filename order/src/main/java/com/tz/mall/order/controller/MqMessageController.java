package com.tz.mall.order.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.tz.mall.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tz.mall.order.entity.MqMessageEntity;
import com.tz.mall.order.service.MqMessageService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:53:27
 */
@RestController
@RequestMapping("order/mqmessage")
public class MqMessageController {
    @Autowired
    private MqMessageService mqMessageService;

    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = mqMessageService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{messageId}")
    public R info(@PathVariable("messageId") String messageId){
		MqMessageEntity mqMessage = mqMessageService.getById(messageId);

        return R.ok().put("mqMessage", mqMessage);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MqMessageEntity mqMessage){
		mqMessageService.save(mqMessage);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MqMessageEntity mqMessage){
		mqMessageService.updateById(mqMessage);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] messageIds){
		mqMessageService.removeByIds(Arrays.asList(messageIds));

        return R.ok();
    }

    @RequestMapping("/sendMsg")
    public String sendMsg(){
        for (int i = 0; i < 4; i++) {
            OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
            reasonEntity.setId(2L);
            reasonEntity.setCreateTime(new Date());
            reasonEntity.setName("chris"+i);
            rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",reasonEntity);
        }
        return "ok";
    }

    @RequestMapping("/sendErrMsg")
    public String sendErrMsg(){
        for (int i = 0; i < 2; i++) {
            OrderReturnReasonEntity reasonEntity = new OrderReturnReasonEntity();
            reasonEntity.setId(2L);
            reasonEntity.setCreateTime(new Date());
            reasonEntity.setName("chris"+i);
            rabbitTemplate.convertAndSend("hello-java-exchange","hello.java1",reasonEntity);
        }
        return "ok";
    }

}
