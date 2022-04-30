package com.tz.mall.order.feign;

import com.tz.common.utils.R;
import com.tz.mall.order.vo.MemberAddressVo;
import com.tz.mall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("gulimall-member")
public interface MemberFeignService {
//    member/memberreceiveaddress/{memeberID}/addresses
    @RequestMapping("/member/memberreceiveaddress/{memeberID}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memeberID") Long id);


}
