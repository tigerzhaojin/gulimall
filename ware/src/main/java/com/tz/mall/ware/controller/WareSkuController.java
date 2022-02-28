package com.tz.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tz.common.to.SkuHasStockTo;
import com.tz.mall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.ware.entity.WareSkuEntity;
import com.tz.mall.ware.service.WareSkuService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 商品库存
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 17:10:29
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;
//    查询sku是否有库存
    @PostMapping("/hasstrock")
    public List<SkuHasStockTo> getSkuHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockTo> tos= wareSkuService.getSkuHasStock(skuIds);
        return tos;
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
