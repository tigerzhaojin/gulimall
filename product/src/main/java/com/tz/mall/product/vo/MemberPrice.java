/**
  * Copyright 2022 bejson.com 
  */
package com.tz.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Auto-generated: 2022-02-17 15:51:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;


}