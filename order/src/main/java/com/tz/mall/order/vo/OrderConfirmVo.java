package com.tz.mall.order.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class OrderConfirmVo {
//   收获地址，从member中取
    private List<MemberAddressVo> address;
    private List<OrderItemVo> items;
    private Integer itemCount;
//    积分信息
    private Integer integration;
    private BigDecimal total; //订单总额
    private BigDecimal payPrice; //应付总额
    private String orderToken;//防止重复提交令牌
    private Map<Long,Boolean> stocks;

}
