package com.tz.mall.coupon.dao;

import com.tz.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:17:42
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
