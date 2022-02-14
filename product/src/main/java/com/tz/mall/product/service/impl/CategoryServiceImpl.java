package com.tz.mall.product.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.tz.mall.product.entity.CategoryBrandRelationEntity;
import com.tz.mall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.CategoryDao;
import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
//        查出所有的分类信息
        List<CategoryEntity> entities = baseMapper.selectList(null);
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                //标识为0
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;


        /* ～～～～～～～～～～～～～～～～旧写法～～～～～～～～～～～～～～～～～～～*/
////        查出所有的根节点集合
//        List<CategoryEntity> rootEntities=new ArrayList<>();
//        for (CategoryEntity entity : entities){
//            if (entity.getParentCid()==0L) {
//                rootEntities.add(entity);
//            }
//        }
//        /* 根据Menu类的order排序 */
//        //Collections.sort(rootMenu, order());
//        //为根菜单设置子菜单，getClild是递归调用的
//        for (CategoryEntity entity : rootEntities){
//            List<CategoryEntity> childEitities=getChild(String.valueOf(entity.getCatId()) ,entities);
//            entity.setChildren(childEitities);
//        }
//        return rootEntities;
//        /* ～～～～～～～～～～～～～～～～旧写法结束～～～～～～～～～～～～～～～～～～～*/
    }
//

    @Override
    public void removeMenuByIds(List<Long> asList) {
//        TODO
        System.out.println("正在进行逻辑删除");
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatlogPath(Long catelogId) {
        List<Long> path =new ArrayList<>();
        List<Long> parentPath = findParentId(catelogId, path);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }
//更新category同时，更新关联表pms_category_brand_relation里的数据
    @Transactional
    @Override
    public void updateByIdwithCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCatetory(category.getCatId(),category.getName());


    }

    private List<Long> findParentId(Long catelogId,List<Long> path){
        path.add(catelogId);
        CategoryEntity categoryEntity=this.getById(catelogId);
        if (categoryEntity.getParentCid()!=0){
            findParentId(categoryEntity.getParentCid(),path);
        }
        return  path;
    }


    /**
     * 获取子节点
     *
     * @param id      父节点id
     * @param allMenu 所有菜单列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<CategoryEntity> getChild(String id, List<CategoryEntity> allMenu) {
        //子菜单
        List<CategoryEntity> childList = new ArrayList<>();
        for (CategoryEntity entity : allMenu) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if (String.valueOf(entity.getParentCid()).equals(id)) {
                childList.add(entity);
            }
        }
        //递归
        for (CategoryEntity entity : childList) {
            entity.setChildren(getChild(String.valueOf(entity.getCatId()), allMenu));
        }
        //Collections.sort(childList,order()); //排序
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity ->
             root.getCatId().equals(categoryEntity.getParentCid())
//            return root.getCatId()==categoryEntity.getCatId();
        ).map(categoryEntity -> {
            //找到子菜单
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //菜单排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }


}