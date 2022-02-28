package com.tz.mall.product.feign;

import com.tz.common.to.es.SkuEsModel;
import com.tz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    public R productUp(@RequestBody List<SkuEsModel> skuEsModels);
}
