package com.tz.mall.order.to;

import com.tz.mall.order.entity.OrderEntity;
import com.tz.mall.order.entity.OrderItemEntity;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class OrderCreateTo {
    private OrderEntity order;
    private List<OrderItemEntity> orderItems;
    private BigDecimal fare;
    private BigDecimal payPrice;
}
