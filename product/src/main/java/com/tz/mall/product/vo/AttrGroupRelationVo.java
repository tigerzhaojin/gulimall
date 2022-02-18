package com.tz.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttrGroupRelationVo {
    private Long attrId;
    private Long attrGroupId;

}
