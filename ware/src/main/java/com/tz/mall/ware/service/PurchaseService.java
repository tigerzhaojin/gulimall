package com.tz.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tz.common.utils.PageUtils;
import com.tz.mall.ware.entity.PurchaseEntity;
import com.tz.mall.ware.vo.MergeVo;
import com.tz.mall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author tiger
 * @email tiger_jo@live.com
 * @date 2022-01-23 17:10:29
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getUnreceiveList(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);



    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);

}

