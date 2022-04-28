package com.tz.common.to;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuHasStockTo  {
    private Long skuId;
    private Boolean hasStock;
}
