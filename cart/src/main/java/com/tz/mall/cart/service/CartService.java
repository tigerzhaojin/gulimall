package com.tz.mall.cart.service;

import com.tz.mall.cart.vo.Cart;
import com.tz.mall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {
    //将商品添加到购物车
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    //获取购物车里某个Item的信息
    CartItem getCartItem(Long skuId);

    //获取整个购物车
    Cart getCart() throws ExecutionException, InterruptedException;

//    清空购物车
    void clearCart(String cartKey);

//    勾选购物车
    void checkItem(Long skuId, Integer check);
//    改变商品数量
    void changeItemCount(Long skuId, Integer num);

//    删除商品
    void deleteItem(Long skuId);
}
