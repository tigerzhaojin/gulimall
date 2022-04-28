package com.tz.common.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LockStockResult {
    private Long skuId;
    private Integer num;
    private Boolean locked;
}
