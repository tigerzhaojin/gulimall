package com.tz.mall.order.feign;

import com.tz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {
    @RequestMapping("/product/spuinfo/skuId/{skuId}")
    R getSpuInfobySkuId(@PathVariable("skuId") Long skuId);
}
