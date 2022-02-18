package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:05
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);


    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    public void updateCatetory(Long catId, String name);

    List<CategoryBrandRelationEntity> getBrandsByCatid(Long catId);

}

