package com.tz.mall.product.service.impl;

import com.alibaba.druid.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.CategoryDao;
import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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
//        查出所有的根节点集合
        List<CategoryEntity> rootEntities=new ArrayList<>();
        for (CategoryEntity entity : entities){
            if (entity.getParentCid()==0L) {
                rootEntities.add(entity);
            }
        }
        /* 根据Menu类的order排序 */
        //Collections.sort(rootMenu, order());
        //为根菜单设置子菜单，getClild是递归调用的
        for (CategoryEntity entity : rootEntities){
            List<CategoryEntity> childEitities=getChild(String.valueOf(entity.getCatId()) ,entities);
            entity.setChildren(childEitities);
        }

        return rootEntities;
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

    public List<CategoryEntity> getChildren (CategoryEntity root,List<CategoryEntity> allMenu){

        return null;
    }



}