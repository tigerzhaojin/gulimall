package com.tz.mall.product.vo;

import com.tz.mall.product.entity.SkuImagesEntity;
import com.tz.mall.product.entity.SkuInfoEntity;
import com.tz.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SkuItemVo {
//   1. sku基本信息
    private SkuInfoEntity info;
//   2. sku图片信息
    private List<SkuImagesEntity> images;
//   3. spu销售组合信息
    private List<SkuItemSaleAttrVo> saleAttr;
//   4. spu介绍,描述信息
    private SpuInfoDescEntity desp;
//   5. spu规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;






}
