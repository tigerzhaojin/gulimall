package com.tz.mall.product.feign;


import com.tz.common.to.SkuHasStockTo;
import com.tz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstrock")
    List<SkuHasStockTo>  getSkuHasStock(@RequestBody List<Long> skuIds);
}
