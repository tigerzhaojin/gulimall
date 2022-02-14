package com.tz.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.tz.mall.product.vo.AttrRespVo;
import com.tz.mall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping("/base/list/{catId}")
    public R baseList (@PathVariable("catId") long catId,
                       @RequestParam Map<String,Object> params){
        PageUtils page = attrService.queryBaseAttrPage(catId,params);
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
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

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
