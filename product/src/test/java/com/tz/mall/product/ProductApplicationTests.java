package com.tz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.BrandService;
import com.tz.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductApplicationTests {
    @Resource
    BrandService brandService;

    @Resource
    CategoryService categoryService;
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

    @Test
    public  void findPanrentList(){
        Long[] catlogPath = categoryService.findCatlogPath(999L);
        log.info("完整路径：{}", Arrays.asList(catlogPath));
    }

}
