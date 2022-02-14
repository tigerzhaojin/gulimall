package com.tz.mall.product.service.impl;

import com.tz.mall.product.dao.BrandDao;
import com.tz.mall.product.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.CategoryBrandRelationDao;
import com.tz.mall.product.entity.CategoryBrandRelationEntity;
import com.tz.mall.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Resource
    BrandDao brandDao;
    @Resource
    CategoryDao categoryDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        String cateName = categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName();
        String brandName = brandDao.selectById(categoryBrandRelation.getBrandId()).getName();
        categoryBrandRelation.setBrandName(brandName);
        categoryBrandRelation.setCatelogName(cateName);
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateCatetory(Long catId, String name) {
        this.baseMapper.updateCatetory(catId,name);
    }

}