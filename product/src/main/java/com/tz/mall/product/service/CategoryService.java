package com.tz.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 10:28:05
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);
//找到catelogId对应的完整路径
    public Long[] findCatlogPath(Long catelogId);

    public void updateByIdwithCascade(CategoryEntity category);

    List<CategoryEntity> getTopLevelCats();

    Map<String, List<Catelog2Vo>> getCatelogJson();

}


