package com.tz.mall.ware.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseItemDoneVo {
//    items: [{itemId:1,status:4,reason:""}]//完成/失败的需求详情\
    private Long itemId;
    private Integer status;
    private String reason;
}
