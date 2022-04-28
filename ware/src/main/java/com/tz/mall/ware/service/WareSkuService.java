package com.tz.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.to.SkuHasStockTo;
import com.tz.common.to.mq.OrderTo;
import com.tz.common.to.mq.StockLockedTo;
import com.tz.common.utils.PageUtils;
import com.tz.common.vo.LockStockResult;
import com.tz.mall.ware.entity.WareSkuEntity;
import com.tz.mall.ware.vo.SkuHasStockVo;
import com.tz.mall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 17:10:29
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo wareSkuLockVo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo orderTo);

}

