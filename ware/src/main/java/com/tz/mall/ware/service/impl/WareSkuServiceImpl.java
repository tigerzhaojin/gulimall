package com.tz.mall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tz.common.to.SkuHasStockTo;
import com.tz.common.to.mq.OrderTo;
import com.tz.common.to.mq.StockDetailTo;
import com.tz.common.to.mq.StockLockedTo;
import com.tz.common.utils.R;

import com.tz.mall.ware.entity.WareOrderTaskDetailEntity;
import com.tz.mall.ware.entity.WareOrderTaskEntity;
import com.tz.mall.ware.feign.OrderFeignService;
import com.tz.mall.ware.feign.ProductFeignService;
import com.tz.mall.ware.service.WareOrderTaskDetailService;
import com.tz.mall.ware.service.WareOrderTaskService;
import com.tz.mall.ware.vo.OrderItemVo;
import com.tz.mall.ware.vo.OrderVo;
import com.tz.mall.ware.vo.WareSkuLockVo;
import com.tz.mall.ware.exception.NoStockException;
import lombok.Data;
import lombok.ToString;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.ware.dao.WareSkuDao;
import com.tz.mall.ware.entity.WareSkuEntity;
import com.tz.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    OrderFeignService orderFeignService;
    @Autowired
    WareOrderTaskService wareOrderTaskService;
    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    WareSkuDao wareSkuDao;
    @Autowired
    ProductFeignService feignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
//        判断如果没有库存记录则新增，若有，则更新
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", skuId)
                .eq("ware_id", wareId));
        if (entities == null || entities.size() == 0) {
//            创建新的库存记录
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setWareId(wareId);
            skuEntity.setStock(skuNum);
            R r = feignService.info(skuId);
            Map<String, Object> skuInfo = new HashMap<>();
            skuInfo = (Map<String, Object>) r.get("skuInfo");
            skuEntity.setSkuName((String) skuInfo.get("skuName"));
//             r.get("spuInfo");
//            skuEntity.setSkuName();
            wareSkuDao.insert(skuEntity);
        } else {
            //            更新库存
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }

    }

    @Override
    public List<SkuHasStockTo> getSkuHasStock(List<Long> skuIds) {

        List<SkuHasStockTo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockTo vo = new SkuHasStockTo();
//            查询当前sku的总库存量
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

//    lock the inventory for order

    /**
     * 库存解锁场景：
     * 1. 下订单成功，订单过期未支付被自动取消或者用户手动取消订单，解锁库存
     * 2. 下订单成功，库存锁定成功，后续执行异常，导致订单回滚
     * Distributed service inventory unlocking scenarios:
     * 1. the order is placed successfully,
     * the order will be automatically cancelled if the order expires and the payment is not made,
     * or the user manually cancels the order to unlock the inventory.
     * 2. The order is placed successfully, the inventory is locked successfully,
     * but the subsequent execution is abnormal, causing the order to be rolled back
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo wareSkuLockVo) {
//        保存库存工作单  Save Inventory Task
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(wareSkuLockVo.getOrderSn());
        wareOrderTaskService.save(taskEntity);
//        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        List<OrderItemVo> locks = wareSkuLockVo.getLocks();
//        find which ware has stock
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            List<Long> wareIds = wareSkuDao.listWareIdHasStock(skuId);
            stock.setWareIds(wareIds);
            return stock;
        }).collect(Collectors.toList());
//        lock stock
        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareIds();
//            no ware stock for this skuid
            if (wareIds == null || wareIds.size() == 0) {
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
//                success: return 1, else return 0
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (count == 1) {
                    skuStocked = true;
//                  库存锁定成功，通知MQ消息
//                  Inventory locked successfully, notify MQ message
//                  1. 保存库存工作单详情   Save Inventory Task Detail
                    WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity(null, skuId, "",
                            hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    wareOrderTaskDetailService.save(taskDetailEntity);
//                  2. 通知mq notify MQ
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(taskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(taskDetailEntity, stockDetailTo);
                    stockLockedTo.setDetailTo(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTo);
                    break;
                } else {
//                    current wareid locked failue, try next
                }
            }
            if (skuStocked == false) {
                throw new NoStockException(skuId);
            }

        }
        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) {

        StockDetailTo detail = to.getDetailTo();
        Long detailId = detail.getId();
//        锁定库存的detail信息
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        if (byId != null) {
//            已有库存被锁定，解锁库存.
//            1.先检查订单是否存在
            Long id = to.getId();
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();
            R r = orderFeignService.getOrderStatus(orderSn);
            if (r.getCode() == 0) {
                Object data = r.get("data");
                ObjectMapper mapper = new ObjectMapper();
                OrderVo orderVo = r.getData(new TypeReference<OrderVo>(){});

//                System.out.println("abc");
//            如果订单不存在，或者订单被取消，解锁库存
                if (orderVo == null || orderVo.getStatus() == 4) {
//                  能查询到订单，且订单状态为已取消,且库存状态为已锁定,解锁库存
                    if (byId.getLockStatus() == 1) {
                        unLockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }


                }
            } else {
//                  订单不存在，消息被拒绝后，重新返回队列
                throw new RuntimeException("远程服务失败");
            }


        } else {
//            库存自身抛出异常,并没有真正锁定，无需解锁

        }
    }
//  订单关闭后自动执行， 解锁库存调用方法，把锁定库存的数量加回去
    @Transactional
    @Override
    public void unlockStock(OrderTo orderTo) {
        String orderSn = orderTo.getOrderSn();
        WareOrderTaskEntity task= wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = task.getId();
        List<WareOrderTaskDetailEntity> entities = wareOrderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>()
                .eq("task_id", id)
                .eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity : entities) {
            unLockStock(entity.getSkuId(),entity.getWareId(),
                    entity.getSkuNum(),entity.getId());
        }

    }

    //库存到期后自动执行,  解锁库存调用方法，把锁定库存的数量加回去
    private void unLockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        wareSkuDao.unLockStock(skuId, wareId, num);
        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
        entity.setId(taskDetailId);
        entity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(entity);
    }

    @Data
    @ToString
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

}



