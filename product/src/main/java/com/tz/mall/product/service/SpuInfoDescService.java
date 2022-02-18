package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:05
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);

}

