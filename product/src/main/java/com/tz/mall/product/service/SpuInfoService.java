package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.SpuInfoDescEntity;
import com.tz.mall.product.entity.SpuInfoEntity;
import com.tz.mall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:05
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);



}

