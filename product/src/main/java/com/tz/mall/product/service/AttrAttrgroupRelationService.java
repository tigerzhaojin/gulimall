package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.AttrAttrgroupRelationEntity;
import com.tz.mall.product.vo.AttrGroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:06
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);



}

