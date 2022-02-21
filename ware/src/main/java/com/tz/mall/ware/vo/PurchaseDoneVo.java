package com.tz.mall.ware.vo;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PurchaseDoneVo {
    @NotNull
    private Long id;//采购单Id
    List<PurchaseItemDoneVo> items;

}
