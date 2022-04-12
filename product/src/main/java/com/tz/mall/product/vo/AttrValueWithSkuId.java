package com.tz.mall.product.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AttrValueWithSkuId {
    private String attrValue;
    private String skuIds;
}
