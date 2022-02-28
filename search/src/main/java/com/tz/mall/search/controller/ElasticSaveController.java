package com.tz.mall.search.controller;

import com.tz.common.exception.BizCodeEnume;
import com.tz.common.to.es.SkuEsModel;
import com.tz.common.utils.R;
import com.tz.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSaveController {
    @Autowired
    ProductSaveService productSaveService;
//    上架商品
    @PostMapping("/product")
    public R productUp(@RequestBody  List<SkuEsModel> skuEsModels){
        boolean b =false;
        try {
            b=productSaveService.productUp(skuEsModels);

        }catch (Exception e){
            log.error("商品上架错误，{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),
                    BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        return R.ok();

    }
}
