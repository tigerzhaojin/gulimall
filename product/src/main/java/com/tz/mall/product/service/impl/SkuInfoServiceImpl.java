package com.tz.mall.product.service.impl;

import com.tz.mall.product.entity.SkuImagesEntity;
import com.tz.mall.product.entity.SpuInfoDescEntity;
import com.tz.mall.product.service.*;
import com.tz.mall.product.vo.SkuItemSaleAttrVo;
import com.tz.mall.product.vo.SkuItemVo;
import com.tz.mall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.SkuInfoDao;
import com.tz.mall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    SkuImagesService imagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    ThreadPoolExecutor thread;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        /**
        * page: 1
            limit: 10
            key:
            catelogId: 0
            brandId: 0
            min: 0
            max: 0*/
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(key)){
            wrapper.and(w->{
                w.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)){

            wrapper.eq("brand_id",brandId);
        }
        if (!StringUtils.isEmpty(min)){
            wrapper.ge("price",min);
        }
        if (!StringUtils.isEmpty(max) ){
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0"))==1){
                    wrapper.le("price",max);
                }
            } catch (Exception e){

            };

        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        List <SkuInfoEntity> list=
                this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
        return list;
    }

/**~~~~~~~~~~~~~~~~~~~~~~~~查询商品详情~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        //   1. sku基本信息
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, thread);
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //   3. spu销售组合信息
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, thread);
        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            //   4. spu介绍,描述信息
            SpuInfoDescEntity infoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesp(infoDescEntity);
        }, thread);

        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync(res -> {
            //   5. spu规格参数信息
            List<SpuItemAttrGroupVo> attrGroupVos =
                    attrGroupService.getAttrgroupWithattrByGroupId(res.getSkuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(attrGroupVos);
        }, thread);

//        Long spuId = skuInfoEntity.getSpuId();
//        Long catalogId = skuInfoEntity.getCatalogId();
//   2. sku图片信息
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> images = imagesService.getImagsBySkuId(skuId);
            skuItemVo.setImages(images);
        }, thread);

//        等待所有执行完毕
        CompletableFuture.allOf
                (infoFuture,saleAttrFuture,descFuture,baseAttrFuture,imageFuture).get();
        return skuItemVo;
    }


}