package com.tz.mall.authserver.feign;


import com.tz.common.to.SocialUserVo;
import com.tz.common.utils.R;
import com.tz.mall.authserver.vo.UserLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping("/member/member/oauth/login")
    public R login(@RequestBody SocialUserVo.GitVo vo);

}
