package com.tz.mall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.common.utils.R;
import com.tz.mall.cart.feign.ProductFeignService;
import com.tz.mall.cart.interceptor.CartInterceptor;
import com.tz.mall.cart.service.CartService;
import com.tz.mall.cart.vo.Cart;
import com.tz.mall.cart.vo.CartItem;
import com.tz.mall.cart.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.tz.mall.cart.to.UserInfoTo;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    ThreadPoolExecutor executor;
    private final String CART_PREFIX="gulimall:cart:";
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

//      先去redis查一下，有无该商品
        String res = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(res)){
//            没有此商品，需要新增
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> skuInfoTask = CompletableFuture.runAsync(() -> {
                //     1. 远程查询商品的详细信息
                R info = productFeignService.info(skuId);
                Object data = info.get("skuInfo");
                ObjectMapper objectMapper = new ObjectMapper();
                SkuInfoVo skuInfo = objectMapper.convertValue(data, SkuInfoVo.class);

//     2. 商品添加到购物车
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(skuInfo.getSkuDefaultImg());
                cartItem.setTitle(skuInfo.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(skuInfo.getPrice());
            }, executor);
//    远程查询商品的销售舒心
            CompletableFuture<Void> skuSaleAttrs = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);
//        阻塞等所有任务都完成
            CompletableFuture.allOf(skuInfoTask,skuSaleAttrs).get();
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(),s);
            return cartItem;
        } else{
//            已经有此商品，增加数量即可
            CartItem cartItem = new CartItem();
            cartItem=JSON.parseObject(res,CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(),s);
            return cartItem;

        }



    }
//获取购物车里某个Item的信息
    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String o = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(o, CartItem.class);
        return cartItem;
    }


    //获取整个购物车
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();
        if (userInfoTo.getUserId()!=null){
//            1.已登陆状态
            String cartKey = CART_PREFIX+userInfoTo.getUserId();
//            先判断临时购物车内有无数据
            String tempCartKey= CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems!=null){
                for (CartItem item : tempCartItems) {
//                    临时购物车里有数据，需要合并进去
                    addToCart(item.getSkuId(), item.getCount());
                }
                clearCart(tempCartKey);
            }
//            再重新获取一下登陆后购物车里的数据
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);

        } else {
//            2.未登陆状态
            String cartKey = CART_PREFIX+userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);

        }
        return cart;
    }


    private BoundHashOperations<String, Object, Object> getCartOps() {
        //        在同一次请求里，都可以得到线程里保存的信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey="";
        if (userInfoTo.getUserId()!=null){
//          登陆用户，例如： "gulimall:cart:1"
            cartKey=CART_PREFIX+userInfoTo.getUserId();
        } else {
//          临时用户，例如： "gulimall:cart:effdfds45lldf"
            cartKey=CART_PREFIX+userInfoTo.getUserKey();
        }
//        绑定某个值，以后都针对这个值来操作
        BoundHashOperations<String, Object, Object> oprations = redisTemplate.boundHashOps(cartKey);
        return oprations;
    }

//    获取购物车里的所有数据
    private List<CartItem> getCartItems(String cartKey){
        //            将数据绑定用户的购物车
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
//            购物车里的所有item
        List<Object> values = hashOps.values();
        if (values!=null && values.size()>0){
            List<CartItem> cartItems = values.stream().map((value) -> {
                String str = (String) value;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return cartItems;
        } else {
            return null;
        }

    }
//    删除购物车
    @Override
    public void clearCart(String cartKey){
        redisTemplate.delete(cartKey);
    }

//    修改商品check属性
    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1?true:false);
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),s);
    }
 //  改变商品数量
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),s);
    }

//    删除商品
    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId()==null){
            return null;
        } else {
            String cartKey = CART_PREFIX+userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            List<CartItem> collect =
                    cartItems.stream()
                            .filter(item -> item.getCheck())
                            .map(item->{
//                               获取最新价格
                                BigDecimal price = productFeignService.getPrice(item.getSkuId());
                                item.setPrice(price);
                                return item;
                            })
                            .collect(Collectors.toList());
            return collect;
        }

    }
}
