package com.tz.mall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tz.common.utils.R;
import com.tz.mall.product.entity.BrandEntity;
import com.tz.mall.product.service.AttrGroupService;
import com.tz.mall.product.service.BrandService;
import com.tz.mall.product.service.CategoryService;
import com.tz.mall.product.service.SkuSaleAttrValueService;
import com.tz.mall.product.vo.SkuItemSaleAttrVo;
import com.tz.mall.product.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductApplicationTests {
    @Resource
    BrandService brandService;

    @Resource
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    SkuSaleAttrValueService saleAttrValueService;

//    测试redis
    @Test
    public void redisTest(){
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set("hello","Chris_"+ UUID.randomUUID().toString());
        String hello = opsForValue.get("hello");
        log.info("之前保存的数据：    "+hello);
    }
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

    @Test
    public void testR(){
        R r = R.ok();
        System.out.println(r);
    }

    @Test
    public void getAttrgroupWithattrByGroupId(){
        List<SpuItemAttrGroupVo> attrGroupVos =
                attrGroupService.getAttrgroupWithattrByGroupId(9L, 225L);
        System.out.println(attrGroupVos.toString());
    }

    @Test
    public void saleAttrValueService(){
        List<SkuItemSaleAttrVo> saleAttrVos = saleAttrValueService.getSaleAttrsBySpuId(9L);
        System.out.println(saleAttrVos.toString());
    }
}
