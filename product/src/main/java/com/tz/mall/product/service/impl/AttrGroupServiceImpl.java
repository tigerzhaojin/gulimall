package com.tz.mall.product.service.impl;


import com.tz.mall.product.entity.AttrEntity;
import com.tz.mall.product.service.AttrService;
import com.tz.mall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.product.dao.AttrGroupDao;
import com.tz.mall.product.entity.AttrGroupEntity;
import com.tz.mall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if (catelogId==0){
            IPage<AttrGroupEntity> page=this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        } else {
            String key=(String)params.get("key");
            QueryWrapper<AttrGroupEntity> queryWrapper=new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
            if (!StringUtils.isEmpty(key)){
                queryWrapper.and((obj)->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            IPage<AttrGroupEntity> page=this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper);
            return new PageUtils(page);
        }



    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrgroupWithattr(long catelogId) {
        List<AttrGroupEntity> attrGroupEntities =
                this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_Id", catelogId));
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(item -> {
//        查询所有分组基本信息，插入到attrsVo
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item,attrsVo);
//        每个分组的attrs
            List<AttrEntity> attrs = attrService.getRelationAttr(item.getAttrGroupId());
            attrsVo.setAttrs(attrs);
            return attrsVo;
        }).collect(Collectors.toList());
        return collect;
    }

}