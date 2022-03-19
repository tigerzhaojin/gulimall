package com.tz.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tz.mall.product.entity.ProductAttrValueEntity;
import com.tz.mall.product.service.ProductAttrValueService;
import com.tz.mall.product.vo.AttrRespVo;
import com.tz.mall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.product.entity.AttrEntity;
import com.tz.mall.product.service.AttrService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 商品属性
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 11:19:02
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    ProductAttrValueService attrValueService;

//    /product/attr/base/listforspu/{spuId}
    @RequestMapping(value ="base/listforspu/{spuId}" )
    public R listforSpu(@PathVariable ("spuId") Long spuId){
       List<ProductAttrValueEntity> entities= attrValueService.listforSpu(spuId);
       return R.ok().put("data",entities);
    }


    @RequestMapping("/{attrType}/list/{catId}")
    public R baseList (@PathVariable("catId") long catId,
                       @RequestParam Map<String,Object> params,
                       @PathVariable("attrType") String attrType){

        PageUtils page = attrService.queryBaseAttrPage(catId,params,attrType);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);


        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attrRespVo=attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo){
//		attrService.save(attr);
        attrService.saveAttrWithVo(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateAttrWithVo(attrVo);

        return R.ok();
    }

//    /product/attr/update/{spuId}
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){
        attrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }
    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
