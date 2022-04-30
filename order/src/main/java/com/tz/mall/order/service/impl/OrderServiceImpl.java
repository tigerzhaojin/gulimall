package com.tz.mall.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.common.constant.OrderConstant;
import com.tz.common.exception.NoStockException;
import com.tz.common.to.SkuHasStockTo;
import com.tz.common.to.mq.OrderTo;
import com.tz.common.utils.R;
import com.tz.common.vo.MemberRespVo;
import com.tz.mall.order.dao.OrderItemDao;
import com.tz.mall.order.entity.OrderItemEntity;
import com.tz.mall.order.enume.OrderStatusEnume;
import com.tz.mall.order.feign.CartFeignService;
import com.tz.mall.order.feign.MemberFeignService;
import com.tz.mall.order.feign.ProductFeignService;
import com.tz.mall.order.feign.WmsFeignService;
import com.tz.mall.order.interceptor.UserLoginInterceptor;
import com.tz.mall.order.service.OrderItemService;
import com.tz.mall.order.to.OrderCreateTo;
import com.tz.mall.order.vo.*;
//import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tz.common.utils.PageUtils;
import com.tz.common.utils.Query;

import com.tz.mall.order.dao.OrderDao;
import com.tz.mall.order.entity.OrderEntity;
import com.tz.mall.order.service.OrderService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    private ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    MemberFeignService memberFeignService;
    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    WmsFeignService wmsFeignService;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignService productFeignService;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }
//    to trade
    @Override
    public OrderConfirmVo orderConfirm() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRespVo memberRespVo = UserLoginInterceptor.loginUser.get();
//        先获取原先主线程里的数据
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//      收获地址信息
        CompletableFuture<Void> addrFuture = CompletableFuture.runAsync(() -> {
//            在每一个子线程里设置一下主线程里获取的数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            orderConfirmVo.setAddress(address);
        }, executor);

//      商品信息
        CompletableFuture<Void> itemFuture = CompletableFuture.runAsync(() -> {
//            在每一个子线程里设置一下主线程里获取的数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> cartItems = cartFeignService.getUserCartItems();
            orderConfirmVo.setItems(cartItems);
//        用户积分信息，已经保存在拦截器里了
            orderConfirmVo.setIntegration(memberRespVo.getIntegration());
//       获取总价,总数量
            BigDecimal sum = new BigDecimal("0");
            Integer itemCount = 0;
            for (OrderItemVo cartItem : cartItems) {
                BigDecimal totoal = cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount()));
                sum = sum.add(totoal);
                itemCount += cartItem.getCount();
            }
            orderConfirmVo.setTotal(sum);
            orderConfirmVo.setPayPrice(sum);
            orderConfirmVo.setItemCount(itemCount);
        }, executor).thenRunAsync(() -> {
//            远程查询库存信息
            List<OrderItemVo> items = orderConfirmVo.getItems();
            List<Long> skdIds = items.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            List<SkuHasStockTo> skuHasStock = wmsFeignService.getSkuHasStock(skdIds);
            if (skuHasStock != null) {
                Map<Long, Boolean> stockMap
                        = skuHasStock.stream().collect(Collectors.toMap(SkuHasStockTo::getSkuId, SkuHasStockTo::getHasStock));
                orderConfirmVo.setStocks(stockMap);
            }
        }, executor);
        CompletableFuture.allOf(addrFuture, itemFuture).get();
        //      防重复令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue()
                .set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId().toString()
                        , token, 30, TimeUnit.MINUTES);
        orderConfirmVo.setOrderToken(token);
        return orderConfirmVo;
    }

//    @GlobalTransactional 效果不好，用mq代替
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo) {
        confirmVoThreadLocal.set(submitVo);
        SubmitOrderResponseVo orderResponseVo = new SubmitOrderResponseVo();
        orderResponseVo.setCode(0);
        MemberRespVo memberRespVo = UserLoginInterceptor.loginUser.get();
        String redisKey = OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId();
        redisTemplate.opsForValue().get(redisKey);
        String orderToken = submitVo.getOrderToken();
//        0 token failure , 1 token success
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        Long result = redisTemplate.execute
                (new DefaultRedisScript<Long>(script, Long.class),
                        Arrays.asList(redisKey), orderToken);

        if (result == 0L) {
//            failue
            orderResponseVo.setCode(1);
            return orderResponseVo;
        } else {
//  success
            OrderCreateTo order = createOrder();
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrcie = submitVo.getPayPrcie();
            if (Math.abs(payAmount.subtract(payPrcie).doubleValue())<0.01){
                saveOrder(order);
//          lock the inventory for order
                WareSkuLockVo lockVo = new WareSkuLockVo();
                lockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> itemVos = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                lockVo.setLocks(itemVos);
                R r = wmsFeignService.lockWare(lockVo);
//                order rollback, stock dosen't roll back
//                Integer i=10/0;
                if (r.getCode()==0){
//                    lock success
                    orderResponseVo.setOrder(order.getOrder());
//                    库存锁定成功，可以认为整体订单已经创建完成
                    rabbitTemplate.convertAndSend("order-event-exchange",
                            "order.create.order",order.getOrder());
                    return orderResponseVo;
                } else {
//                    lock failue
                    String msg = r.getMsg();
                    throw new NoStockException(msg);
//                    orderResponseVo.setCode(3);
//
//                    return orderResponseVo;
                }
            } else {
                orderResponseVo.setCode(2);
                return orderResponseVo;
            }
        }

    }

    @Override
    public OrderEntity getOrderBySn(String orderSn) {
        OrderDao dao = this.baseMapper;
        OrderEntity order =
                dao.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return order;
    }

    @Override
    public void closeOrder(OrderEntity orderEntity) {
//        先查询当前订单的状态
        OrderEntity entity = this.getById(orderEntity.getId());
        if (entity.getStatus()==OrderStatusEnume.CREATE_NEW.getCode()){
            entity.setStatus(OrderStatusEnume.CANCLED.getCode());
            this.updateById(entity);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(entity,orderTo);
            rabbitTemplate.convertAndSend("order-event-exchange",
                    "order.release.other",orderTo);
        }
    }

    @Override
    public PayVo getOrderPay(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity order = this.getOrderBySn(orderSn);
        BigDecimal bigDecimal = order.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(bigDecimal.toString());
        payVo.setOut_trade_no(orderSn);
        List<OrderItemEntity> order_sn = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        OrderItemEntity itemEntity = order_sn.get(0);
        payVo.setSubject(itemEntity.getSkuName());
        payVo.setBody(itemEntity.getSkuAttrsVals());
        return payVo;
    }

    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        List<OrderItemEntity> orderItems = order.getOrderItems();
        OrderDao orderDao = this.baseMapper;
        orderDao.insert(orderEntity);
        orderItemService.saveBatch(orderItems);

    }

    private OrderCreateTo createOrder() {
        MemberRespVo memberRespVo = UserLoginInterceptor.loginUser.get();
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        OrderSubmitVo orderSubmitVo = confirmVoThreadLocal.get();


//     1.generate orderNumber, userId
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberId(memberRespVo.getId());
//      2. set consignee information and delivery fee
        R fare = wmsFeignService.getFare(orderSubmitVo.getAddrId());
        Object obj = fare.get("fare");
        FareVo fareResponse = new ObjectMapper().convertValue(obj, FareVo.class);
        orderEntity.setFreightAmount(fareResponse.getFare());
        orderEntity.setReceiverCity(fareResponse.getMemberAddressVo().getCity());
        orderEntity.setReceiverDetailAddress(fareResponse.getMemberAddressVo().getDetailAddress());
        orderEntity.setReceiverName(fareResponse.getMemberAddressVo().getName());
        orderEntity.setReceiverPhone(fareResponse.getMemberAddressVo().getPhone());
        orderEntity.setReceiverPostCode(fareResponse.getMemberAddressVo().getPostCode());
        orderEntity.setReceiverProvince(fareResponse.getMemberAddressVo().getProvince());
        orderEntity.setReceiverRegion(fareResponse.getMemberAddressVo().getRegion());
//        3.set order items
        List<OrderItemEntity> itemEntities = builderOrderItems(orderSn);
//        4. verify price
        computePrice(orderEntity, itemEntities);
        orderCreateTo.setOrder(orderEntity);
        orderCreateTo.setOrderItems(itemEntities);
//        createOrder().setFare(fare);
        return orderCreateTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> itemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0");
        BigDecimal integration = new BigDecimal("0");
        BigDecimal promotion = new BigDecimal("0");
        Integer giftIntegration =0;
        Integer giftGrowth=0;
//        totalAmout of oder
        for (OrderItemEntity itemEntity : itemEntities) {
            total=total.add(itemEntity.getRealAmount());
            coupon=coupon.add(itemEntity.getCouponAmount());
            integration = integration.add(itemEntity.getIntegrationAmount());
            promotion =promotion.add(itemEntity.getPromotionAmount());
            giftIntegration += itemEntity.getGiftIntegration();
            giftGrowth += itemEntity.getGiftGrowth();
        }
        orderEntity.setTotalAmount(total);
        orderEntity.setCouponAmount(coupon);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setAutoConfirmDay(7);
        orderEntity.setIntegration(giftIntegration);
        orderEntity.setGrowth(giftGrowth);

        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
//        set order related status
        orderEntity.setStatus(OrderStatusEnume.CREATE_NEW.getCode());
        orderEntity.setDeleteStatus(0);

    }


    //        3.set order items
    private List<OrderItemEntity> builderOrderItems(String orderSn) {
        List<OrderItemVo> userCartItems = cartFeignService.getUserCartItems();
        if (userCartItems != null && userCartItems.size() > 0) {
            List<OrderItemEntity> itemEntities = userCartItems.stream().map(cartItem -> {
                OrderItemEntity itemEntity = builderOrderItem(cartItem);
                itemEntity.setOrderSn(orderSn);
                return itemEntity;
            }).collect(Collectors.toList());
            return itemEntities;
        } else {
            return null;
        }

    }

    private OrderItemEntity builderOrderItem(OrderItemVo cartItem) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        Long skuId = cartItem.getSkuId();
//        1.spu Information
        R r = productFeignService.getSpuInfobySkuId(skuId);
        Object o = r.get("spuInfo");
        SpuInfoVo spuInfoVo =objectMapper.convertValue(o, SpuInfoVo.class);
        itemEntity.setSpuId(spuInfoVo.getId());
        itemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
        itemEntity.setSpuName(spuInfoVo.getSpuName());
        itemEntity.setCategoryId(spuInfoVo.getCatalogId());
//        2.sku Information
        itemEntity.setSkuId(cartItem.getSkuId());
        itemEntity.setSkuName(cartItem.getTitle());
        itemEntity.setSkuPic(cartItem.getImage());
        itemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttrs = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        itemEntity.setSkuAttrsVals(skuAttrs);
        itemEntity.setSkuQuantity(cartItem.getCount());
//        3.coupon Information
        itemEntity.setGiftIntegration(cartItem.getPrice().intValue());
        itemEntity.setGiftGrowth(cartItem.getPrice().intValue());
        itemEntity.setPromotionAmount(new BigDecimal("0"));
        itemEntity.setCouponAmount(new BigDecimal("0"));
        itemEntity.setIntegrationAmount(new BigDecimal("0"));
        BigDecimal orign =
                itemEntity.getSkuPrice().multiply(new BigDecimal(itemEntity.getSkuQuantity().toString()));
        BigDecimal amount = orign.subtract(itemEntity.getPromotionAmount())
                .subtract(itemEntity.getCouponAmount())
                .subtract(itemEntity.getIntegrationAmount());
        itemEntity.setRealAmount(amount);
        return itemEntity;
    }


//    代理对象示例
    @Transactional
    public void a (){
        OrderServiceImpl orderService = (OrderServiceImpl) AopContext.currentProxy();
        orderService.b();
        orderService.c();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void b (){

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW,timeout = 20)
    public void c (){

    }
}