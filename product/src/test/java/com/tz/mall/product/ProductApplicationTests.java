package com.tz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.BrandService;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductApplicationTests {
    @Resource
    BrandService brandService;

    @Test
    public void contextLoads() {
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
