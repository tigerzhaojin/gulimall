package com.tz.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tz.mall.product.dao.CategoryBrandRelationDao;
import com.tz.mall.product.entity.CategoryBrandRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.BrandDao;
import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Resource
    CategoryBrandRelationDao categoryBrandRelationDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key=(String)params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
            queryWrapper.eq("brand_id",key).or().like("name",key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        CategoryBrandRelationEntity categoryBrandRelationEntity =new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandName(brand.getName());
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper
                = new UpdateWrapper<>();
        updateWrapper.eq("brand_id",brand.getBrandId());

        categoryBrandRelationDao.update(categoryBrandRelationEntity,updateWrapper);
        this.updateById(brand);
    }

}