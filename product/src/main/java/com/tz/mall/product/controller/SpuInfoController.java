package com.tz.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.tz.mall.product.entity.SkuInfoEntity;
import com.tz.mall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.product.entity.SpuInfoEntity;
import com.tz.mall.product.service.SpuInfoService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * spu信息
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 11:19:01
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;
/**
 * 商品上架
 * /product/spuinfo/{spuId}/up
 * */
    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId){
        spuInfoService.spuUp(spuId);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
//		spuInfoService.save(spuInfo);
        spuInfoService.saveSpuInfo(spuSaveVo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/skuId/{skuId}")
    public R getSpuInfobySkuId(@PathVariable("skuId") Long skuId){
        SpuInfoEntity infoEntity= spuInfoService.getSpuInfobySkuId(skuId);
        return R.ok().put("spuInfo",infoEntity);
    }

}
