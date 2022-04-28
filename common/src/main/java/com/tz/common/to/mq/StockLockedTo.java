package com.tz.common.to.mq;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class StockLockedTo {
    private Long id; //库存工作单ID；
    private StockDetailTo detailTo; //库存详情单ID
}
