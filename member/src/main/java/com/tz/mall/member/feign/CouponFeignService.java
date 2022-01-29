package com.tz.mall.member.feign;

import com.tz.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-coupon")
public interface CouponFeignService {
//    远程路径写完整,将整个远程服务的签名拷贝过来即可
    @RequestMapping("coupon/coupon/member/list")
    public R membercoupon();
}
