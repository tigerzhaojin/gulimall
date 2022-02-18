package com.tz.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.BrandService;
import com.tz.mall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.product.entity.CategoryBrandRelationEntity;
import com.tz.mall.product.service.CategoryBrandRelationService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 11:19:02
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;
//    获取分类关联的品牌/product/categorybrandrelation/brands/list
    @GetMapping(value ="/brands/list")
    public R brandsList(@RequestParam("catId") Long catId){

        List<CategoryBrandRelationEntity> entities = categoryBrandRelationService.getBrandsByCatid(catId);
        return R.ok().put("data",entities);

    }
//获取品牌关联的分类
    @GetMapping(value = "/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId){
        List<CategoryBrandRelationEntity> data= categoryBrandRelationService.list
                (new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
        return R.ok().put("data",data);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
//        String brandName =brandService.getById(categoryBrandRelation.getBrandId()).getName();
//
//        String cateName = categoryService.getById(categoryBrandRelation.getCatelogId()).getName();
//        categoryBrandRelation.setCatelogName(cateName);
//        categoryBrandRelation.setBrandName(brandName);
//        categoryBrandRelationService.save(categoryBrandRelation);
        categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
