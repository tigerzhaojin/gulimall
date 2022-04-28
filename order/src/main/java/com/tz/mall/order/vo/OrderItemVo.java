package com.tz.mall.order.vo;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ToString
@Data
public class OrderItemVo  {
    private long skuId;
    private Boolean check;
    private String title;
    private String image;
    //    商品套餐信息
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    private Boolean hasStock;
    private BigDecimal weight;



}


