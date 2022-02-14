package com.tz.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.tz.mall.product.dao.AttrAttrgroupRelationDao;
import com.tz.mall.product.dao.AttrGroupDao;
import com.tz.mall.product.dao.CategoryDao;
import com.tz.mall.product.entity.AttrAttrgroupRelationEntity;
import com.tz.mall.product.entity.AttrGroupEntity;
import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.service.AttrAttrgroupRelationService;
import com.tz.mall.product.service.CategoryService;
import com.tz.mall.product.vo.AttrRespVo;
import com.tz.mall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
//        保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationDao.insert(relationEntity);


    }

    @Override
    public PageUtils queryBaseAttrPage(long catId, Map<String, Object> params) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
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
        List<AttrRespVo> attrRespVos = attrEntities.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            Long attr_groupid = attrAttrgroupRelationDao.selectOne
                    (new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId())).getAttrGroupId();
            if (attr_groupid != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_groupid);
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
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

}