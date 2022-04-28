package com.tz.mall.ware.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class FareVo {
    private MemberAddressVo memberAddressVo;
    private BigDecimal fare;
}
