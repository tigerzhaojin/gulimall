package com.tz.mall.member.service.impl;

import com.tz.common.to.SocialUserVo;
import com.tz.mall.member.vo.MemberVo;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.member.dao.MemberDao;
import com.tz.mall.member.entity.MemberEntity;
import com.tz.mall.member.service.MemberService;
import org.springframework.util.StringUtils;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public MemberEntity login(MemberVo vo) {
        MemberDao dao = this.baseMapper;
        MemberEntity memberEntity = dao.selectOne(new QueryWrapper<MemberEntity>().eq("username", vo.getLoginAcct())
                .or().eq("mobile", vo.getLoginAcct()));
        if (memberEntity==null){
            return null;
        } else {
            if (memberEntity.getPassword().equals(vo.getPassWord())){
                return memberEntity;
            } else{
                return null;
            }
        }
    }


//    社交用户登陆
    @Override
    public MemberEntity login(SocialUserVo.GitVo vo) {
        MemberDao dao = this.baseMapper;
        String uid = vo.getUid();
        MemberEntity memberEntity = dao.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
//        该用户已经存在社交账户，直接返回用户信息即可
        if (memberEntity!=null){

            return memberEntity;
//       该用户尚未拥有社交账户，那就创建用户
        } else {
            MemberEntity registy=new MemberEntity();
            registy.setSocialUid(vo.getUid());
            registy.setUsername(vo.getUname());
            registy.setNickname(vo.getUname());
            registy.setLevelId(1L);
            registy.setCreateTime(new Date());
            return registy;
        }
    }

}