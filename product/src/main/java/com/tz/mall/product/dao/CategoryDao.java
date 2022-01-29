package com.tz.mall.product.dao;

import com.tz.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:05
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
