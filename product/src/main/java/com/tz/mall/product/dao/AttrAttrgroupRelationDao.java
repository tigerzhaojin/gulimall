package com.tz.mall.product.dao;

import com.tz.mall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:06
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBtachRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);

}
