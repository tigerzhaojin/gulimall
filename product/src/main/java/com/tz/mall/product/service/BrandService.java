package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:06
 */
public interface BrandService extends IService<BrandEntity> {


    PageUtils queryPage(Map<String, Object> params);

    public void updateDetail(BrandEntity brand);

}

