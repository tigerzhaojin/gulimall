package com.tz.mall.ware.service.impl;

import com.tz.common.to.SkuHasStockTo;
import com.tz.common.utils.R;
import com.tz.mall.ware.feign.ProductFeignService;
import com.tz.mall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.ware.dao.WareSkuDao;
import com.tz.mall.ware.entity.WareSkuEntity;
import com.tz.mall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    WareSkuDao wareSkuDao;
    @Autowired
    ProductFeignService feignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        if (!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
//        判断如果没有库存记录则新增，若有，则更新
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", skuId)
                .eq("ware_id", wareId));
        if (entities==null || entities.size()==0){
//            创建新的库存记录
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setWareId(wareId);
            skuEntity.setStock(skuNum);
            R r = feignService.info(skuId);
            Map<String,Object> skuInfo =new HashMap<>();
            skuInfo=(Map<String,Object>)r.get("skuInfo");
            skuEntity.setSkuName((String) skuInfo.get("skuName"));
//             r.get("spuInfo");
//            skuEntity.setSkuName();
            wareSkuDao.insert(skuEntity);
        } else {
            //            更新库存
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }

    }

    @Override
    public List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds) {

        List<SkuHasStockTo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockTo vo = new SkuHasStockTo();
//            查询当前sku的总库存量
            Long count= baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count==null?false:count>0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

}