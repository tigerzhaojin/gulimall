package com.tz.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.order.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 16:53:26
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

