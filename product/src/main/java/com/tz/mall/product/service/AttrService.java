package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.AttrEntity;
import com.tz.mall.product.vo.AttrGroupRelationVo;
import com.tz.mall.product.vo.AttrRespVo;
import com.tz.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:05
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrWithVo(AttrVo attrVo);


    PageUtils queryBaseAttrPage(long catId, Map<String, Object> params, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttrWithVo(AttrVo attrVo);

//根据分组id，找到相关的基本属性
    List<AttrEntity> getRelationAttr(Long attrGroupId);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos);

    PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params);

}

