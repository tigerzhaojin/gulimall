package com.tz.mall.search.vo;

import com.tz.common.to.es.SkuEsModel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SearchResult {
    private List<SkuEsModel> products;
    private Integer pageNum;
    private Long total;
    private Integer totalPages;

    private List<BrandVo> brands;  //当前查询到的结果 所涉及到的所有品牌
    private List<AttrsVo> attrs;  //当前查询到的结果 所涉及到的所有属性
    private List<CatelogVo> catelogs; // 当前查询到的结果 所涉及到的所有分类

    @Data
    @ToString
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    @ToString
    public static class AttrsVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }

    @Data
    @ToString
    public static class CatelogVo{
        private Long catalogId;
        private String catalogName;
    }
}
