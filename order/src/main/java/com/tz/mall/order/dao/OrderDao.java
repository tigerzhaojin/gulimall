package com.tz.mall.order.dao;

import com.tz.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:53:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
