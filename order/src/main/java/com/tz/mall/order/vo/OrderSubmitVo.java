package com.tz.mall.order.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单信息无需在订单确认页里获取，可以重新到购物车里取获取一下
 * 用户相关信息都在session里
 * */
@Data
@ToString
public class OrderSubmitVo {
    private Long addrId;
    private Integer payType;
    private String note;
    private String orderToken; //防重复令牌
    private BigDecimal payPrcie; //用户验证价格
}
