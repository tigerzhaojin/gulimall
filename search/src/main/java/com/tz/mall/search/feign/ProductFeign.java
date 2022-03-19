package com.tz.mall.search.feign;

import com.tz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeign {
    @RequestMapping("/product/category/info/{catId}")
    public R catelogInfo(@PathVariable("catId") Long catId);


    @RequestMapping("/product/brand/info/{brandId}")
    public R brandInfo(@PathVariable("brandId") Long brandId);

    @RequestMapping("/product/attr/info/{attrId}")
    public R attrInfo(@PathVariable("attrId") Long attrId);
}
