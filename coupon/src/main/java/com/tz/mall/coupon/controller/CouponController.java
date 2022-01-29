package com.tz.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tz.mall.coupon.entity.CouponEntity;
import com.tz.mall.coupon.service.CouponService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 优惠券信息
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:17:42
 */
@RefreshScope
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

//  ~~~~~~~~~~~测试类方法，非原有功能~~~~~~~~~~~~~~~~~~~~~~~~~~
//    从配置文件中获取数据
    @Value("${c.user.name}")
    private String userName;
    @Value("${c.user.age}")
    private String userAge;

//    编写测试nacos config
    @RequestMapping(value = "/test")
    public R test(){
      return R.ok().put("name",userName).put("age",userAge);
    }
//    ~~~~~~~~~~~~测试完成~~~~~~~~~~~~~~~~~~~~


    @RequestMapping("/member/list")
    public R membercoupon(){
        CouponEntity couponEntity=new CouponEntity();
        couponEntity.setCouponName("满一百减十");
        return R.ok().put("coupons",Arrays.asList(couponEntity));
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
