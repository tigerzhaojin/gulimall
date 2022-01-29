package com.tz.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.coupon.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:17:40
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

