package com.tz.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tz.mall.ware.vo.MergeVo;
import com.tz.mall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.ware.entity.PurchaseEntity;
import com.tz.mall.ware.service.PurchaseService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 采购信息
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 17:10:29
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**完成采购单
     * /ware/purchase/done
     * */
    @PostMapping(value = "/done")
    public R done(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);
        return R.ok();
    }

    /**06、领取采购单
     * /ware/purchase/received
     * */
    @PostMapping(value = "/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }
    /**
     * 04、合并采购需求
     * /ware/purchase/merge
     */

    @PostMapping(value = "/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }
    /** 05、查询未领取的采购单
     * /ware/purchase/unreceive/list
     * */
    @RequestMapping(value ="/unreceive/list")
    public R unreceiveList(@RequestParam Map<String,Object> params){
        PageUtils page= purchaseService.getUnreceiveList(params);
        return R.ok().put("page",page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
