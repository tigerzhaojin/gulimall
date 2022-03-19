package com.tz.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tz.mall.product.entity.CategoryBrandRelationEntity;
import com.tz.mall.product.service.CategoryBrandRelationService;
import com.tz.mall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate redisTemplate;

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

    @Override
    public List<CategoryEntity> getTopLevelCats() {

        return this.list(new QueryWrapper<CategoryEntity>().eq("cat_level",1L));

    }

//    利用redis缓存
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
//        1.到redis里查询是否有缓存
        String catelogJSON = redisTemplate.opsForValue().get("catelogJSON");
        if (StringUtils.isEmpty(catelogJSON)){
//            如果没有缓存，则到数据库里取数据，然后在redis里写入缓存
            Map<String, List<Catelog2Vo>> catelogJsonFromDb = getCatelogJsonFromDb();
            String jsonString = JSON.toJSONString(catelogJsonFromDb);
            redisTemplate.opsForValue().set("catelogJSON",jsonString);
            return catelogJsonFromDb;
        } else {
//            如果有，则将json数据凡序列话为对象，然后返回
            Map<String, List<Catelog2Vo>> result=
                    JSON.parseObject(catelogJSON,new TypeReference<Map<String, List<Catelog2Vo>>>(){});
            return result;
        }

    }

    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDb() {
//        查出所有category
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> topLevelCats = getParent_cid(selectList,0L);
        Map<String, List<Catelog2Vo>> parent_id = topLevelCats.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
//            查出每一个二级分类
            List<CategoryEntity> categoryEntities =
                    getParent_cid(selectList,v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(),null,l2.getCatId().toString(),l2.getName());
//                  找出当前二级分类的三级分类
                    List<CategoryEntity> level3Catelogs = getParent_cid(selectList,l2.getCatId());
                    if (level3Catelogs!=null){
                        List<Catelog2Vo.Catelog3Vo> collect = level3Catelogs.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo =
                                    new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_id;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList,Long parent_cid) {
//        return baseMapper.selectList(new QueryWrapper<CategoryEntity>()
//                .eq("parent_cid", v.getCatId()));
        List<CategoryEntity> collect = selectList.stream().filter(item -> {
            return item.getParentCid() == parent_cid;
        }).collect(Collectors.toList());
        return collect;
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
        //recursion
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