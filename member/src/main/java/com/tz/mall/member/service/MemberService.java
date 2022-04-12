package com.tz.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.to.SocialUserVo;
import com.tz.common.utils.PageUtils;
import com.tz.mall.member.entity.MemberEntity;
import com.tz.mall.member.vo.MemberVo;

import java.util.Map;

/**
 * 会员
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:35:46
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MemberEntity login(MemberVo vo);

    MemberEntity login(SocialUserVo.GitVo vo);
}

