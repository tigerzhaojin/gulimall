package com.tz.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.tz.common.exception.BizCodeEnume;
import com.tz.common.to.SocialUserVo;
import com.tz.mall.member.feign.CouponFeignService;
import com.tz.mall.member.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tz.mall.member.entity.MemberEntity;
import com.tz.mall.member.service.MemberService;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.R;



/**
 * 会员
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:35:46
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
//    注入接口
    @Autowired
    CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R couponmember(){
        MemberEntity memberEntity=new MemberEntity();
        memberEntity.setNickname("tiger");
        R membercoupons=couponFeignService.membercoupon();
        return R.ok().put("member",memberEntity).put("coupons",membercoupons.get("coupons"));
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("data", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberVo vo){
        MemberEntity memberEntity= memberService.login(vo);
        if (memberEntity!=null){
            return R.ok().put("data",memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getCode(),
                    BizCodeEnume.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getMsg());
        }

    }

    @PostMapping("/oauth/login")
    public R login(@RequestBody SocialUserVo.GitVo vo){

        MemberEntity memberEntity= memberService.login(vo);
        if (memberEntity!=null){
            return R.ok().put("data",memberEntity);
        } else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getCode(),
                    BizCodeEnume.LOGINACCT_PASSWORD_INVALID_EXCEPTION.getMsg());
        }

    }
}
