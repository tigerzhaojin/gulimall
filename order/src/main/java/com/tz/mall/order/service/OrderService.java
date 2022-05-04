package com.tz.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.order.entity.OrderEntity;
import com.tz.mall.order.vo.OrderConfirmVo;
import com.tz.mall.order.vo.OrderSubmitVo;
import com.tz.mall.order.vo.PayVo;
import com.tz.mall.order.vo.SubmitOrderResponseVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:53:27
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo orderConfirm() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo);

    OrderEntity getOrderBySn(String orderSn);

    void closeOrder(OrderEntity orderEntity);

    PayVo getOrderPay(String orderSn);

    PageUtils queryPageWithItem(Map<String, Object> params);

}

