package com.tz.mall.order.vo;

import com.tz.mall.order.entity.OrderEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SubmitOrderResponseVo {
    private OrderEntity order;
    private Integer code; //状态码 0 成功 其他是各种错误
}
