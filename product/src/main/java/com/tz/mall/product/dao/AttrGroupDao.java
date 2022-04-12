package com.tz.mall.product.dao;

import com.tz.mall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tz.mall.product.vo.SkuItemVo;
import com.tz.mall.product.vo.SpuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:06
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {


    List<SpuItemAttrGroupVo> getAttrgroupWithattrByGroupId
            (@Param("skuId") Long skuId, @Param("catalogId") Long catalogId);
}
