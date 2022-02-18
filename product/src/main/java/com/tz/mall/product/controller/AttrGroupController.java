package com.tz.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tz.mall.product.entity.AttrAttrgroupRelationEntity;
import com.tz.mall.product.entity.AttrEntity;
import com.tz.mall.product.service.AttrAttrgroupRelationService;
import com.tz.mall.product.service.AttrService;
import com.tz.mall.product.service.CategoryService;
import com.tz.mall.product.vo.AttrGroupRelationVo;
import com.tz.mall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.product.entity.AttrGroupEntity;
import com.tz.mall.product.service.AttrGroupService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 属性分组
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 11:19:02
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

/**
 * 获取分类下所有分组&关联属性
 * /product/attrgroup/{catelogId}/withattr
 * */

    @GetMapping(value = "/{catelogId}/withattr")
    public R getAttrgroupWithattr(@PathVariable("catelogId") long catelogId){
        List<AttrGroupWithAttrsVo> vos=attrGroupService.getAttrgroupWithattr(catelogId);
        return R.ok().put("data",vos);
    }
//   添加属性与分组关联关系 /product/attrgroup/attr/relation
    @PostMapping("attr/relation")
    public R addRealtion(@RequestBody List<AttrAttrgroupRelationEntity> relationVos){

        relationService.saveBatch(relationVos);
        return R.ok();
    }

//    /product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping(value = "{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> attrEntities= attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data",attrEntities);
    }

//   获取属性分组没有关联的其他属性 /product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping(value ="/{attrgroupId}/noattr/relation")
    public R noattrRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(attrgroupId,params);
        return R.ok().put("data",page);
    }

//    /product/attrgroup/attr/relation/delete删除属性关联
    @PostMapping(value ="/attr/relation/delete" )
    public R attrRelationDele(@RequestBody AttrGroupRelationVo[] attrGroupRelationVos){
        attrService.deleteRelation(attrGroupRelationVos);
        return R.ok();

    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
//        原先方法里参数只有page，现在要加上catelogId。
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] cateLogPath= categoryService.findCatlogPath(catelogId);
        attrGroup.setCatelogPath(cateLogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
