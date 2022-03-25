package com.tz.mall.search.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SearchParam {
    private String keyword;
    private Long catalog3Id;
    private String sort;   //排序条件
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandIds;
    private List<String> attrs;
    private Integer pageNum=1;
}
