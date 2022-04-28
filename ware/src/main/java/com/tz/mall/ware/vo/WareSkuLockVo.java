package com.tz.mall.ware.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class WareSkuLockVo {
    private String orderSn;
    private List<OrderItemVo> locks;
}
