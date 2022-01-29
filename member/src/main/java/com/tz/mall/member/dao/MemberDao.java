package com.tz.mall.member.dao;

import com.tz.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:35:46
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
