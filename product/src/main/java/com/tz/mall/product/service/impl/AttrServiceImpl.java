package com.tz.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tz.mall.product.dao.AttrAttrgroupRelationDao;
import com.tz.mall.product.dao.AttrGroupDao;
import com.tz.mall.product.dao.CategoryDao;
import com.tz.mall.product.entity.AttrAttrgroupRelationEntity;
import com.tz.mall.product.entity.AttrGroupEntity;
import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.service.CategoryService;
import com.tz.mall.product.vo.AttrGroupRelationVo;
import com.tz.mall.product.vo.AttrRespVo;
import com.tz.mall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.AttrDao;
import com.tz.mall.product.entity.AttrEntity;
import com.tz.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Resource
    AttrGroupDao attrGroupDao;
    @Resource
    CategoryDao categoryDao;

    @Resource
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttrWithVo(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
//        对象属性拷贝
        BeanUtils.copyProperties(attrVo,attrEntity);
        // 保存基本操作
        this.save(attrEntity);
//        保存关联关系.

            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relationEntity);




    }

    @Override
    public PageUtils queryBaseAttrPage(long catId, Map<String, Object> params, String attrType) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type",attrType.equalsIgnoreCase("base")?1:0);
        if (catId!=0){
            queryWrapper.eq("catelog_id",catId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and((wapper->{
                wapper.eq("attr_id",key).or().like("attr_name",key);
            }));

        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> attrEntities=page.getRecords();
        List<AttrRespVo> attrRespVos = attrEntities.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne
                    (new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            if (relationEntity != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
//                销售属性没有组，所以不用设置
                if (attrGroupEntity!=null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        //        设置基本信息
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity,attrRespVo);
//        设置group信息
        AttrAttrgroupRelationEntity attrRealtionEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
        if (attrRealtionEntity!=null){
            attrRespVo.setAttrGroupId(attrRealtionEntity.getAttrGroupId());
        }

        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrRealtionEntity.getAttrGroupId());
        if (attrGroupEntity!=null){
            attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
        }

        //        设置catelogpath信息
        attrRespVo.setCatelogPath(categoryService.findCatlogPath(attrRespVo.getCatelogId()));
        CategoryEntity categoryEntity = categoryService.getById(attrRespVo.getCatelogId());
        attrRespVo.setCatelogName(categoryEntity.getName());
        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttrWithVo(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
//        对象属性拷贝
        BeanUtils.copyProperties(attrVo,attrEntity);
        // 保存基本操作
        this.updateById(attrEntity);
//        修改分组关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        UpdateWrapper<AttrAttrgroupRelationEntity> attr_id =
                new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", relationEntity.getAttrId());
        attrAttrgroupRelationDao.update(relationEntity,attr_id);
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrRelations = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrGroupId));
        List<Long> attrIds = attrRelations.stream().map(attrRelation -> {
            return attrRelation.getAttrId();
        }).collect(Collectors.toList());
        return this.listByIds(attrIds);
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos) {
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(attrGroupRelationVos).stream().map(item -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBtachRelation(entities);
    }

//    获取当前分组没有关联的所有的属性
    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
//       1.当前分组只能关联自己所属分类的属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

//        2.其他分组并未引用
//        2.1当前分类下的其他分组
        List<AttrGroupEntity> group = attrGroupDao.selectList(new
                QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId).ne("attr_group_id", attrgroupId));
        List<Long> ids = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
//       2.2这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId =
                attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", ids));
        List<Long> attrIds = groupId.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
//        2.3 从当前分类中移除这些属性
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).notIn("attr_id", attrIds);
        String key=(String)params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and(w->{
                w.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> entityIPage = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(entityIPage);
        return pageUtils;
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {

        return baseMapper.selectSearchAttrs(attrIds);
    }


}