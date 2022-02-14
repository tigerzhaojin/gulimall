package com.tz.mall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tz.common.valid.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.BrandService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-20 11:19:02
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * 添加@Valid注解后，spring会检查是bean中约定的校验字段。如果不标注解，则不起作用
     * 给被校验的bean后面紧跟一个BindingResult,就可以获取校验的结果
     * 注：可以建立一个@ControllerAdvice的类，就可以集中处理异常，不用在每个Controller里面处理
     */
    @RequestMapping("/save")
//    以下为指定新增校验组的方式
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
        brandService.save(brand);
        return R.ok();
    }

//    以下为不指定校验组的方式
//    public R save(@Valid @RequestBody BrandEntity brand /*BindingResult bindingResult*/){
///**      以下为在Controller里单独处理的写法。
//        if (bindingResult.hasErrors()){
//            Map<String,String> errMsg=new HashMap<>();
//            bindingResult.getFieldErrors().forEach((item)->{
//                String field = item.getField();
//                String defaultMessage = item.getDefaultMessage();
//                errMsg.put(field,defaultMessage);
//            });
//            return R.error(400,"提交数据不合法").put("data",errMsg);
//        } else {
//            brandService.save(brand);
//
//            return R.ok();
// */
//
//        /**以下为集中处理的写法，集中处理逻辑在ExceptionController.在这里无需编写*/
//        return R.ok();
//    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
