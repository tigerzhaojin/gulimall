package com.tz.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.BrandService;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest
class ProductApplicationTests {
    @Resource
    BrandService brandService;
    @Test
    void contextLoads() {
        BrandEntity brandEntity=new BrandEntity();
//        brandEntity.setDescript("大家好");
//        brandEntity.setName("Iphone");
//        brandEntity.setBrandId(1L);
//        brandService.updateById(brandEntity);
        List<BrandEntity> list= brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item)->{
            System.out.println(item);
        });
        System.out.println("测试成功");
    }

}
