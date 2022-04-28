package com.tz.mall.cart.controller;


import com.tz.common.utils.R;
import com.tz.mall.cart.service.CartService;
import com.tz.mall.cart.vo.Cart;
import com.tz.mall.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * 浏览器有一个cookie，userkey，用于标识临时身份，一个月后过期
     * 如果第一次使用购物车功能，都会给一个临时身份
     * 浏览器以后保存，每次访问都会带上userkey
     * <p>
     * 登陆了，就用session里的数据
     * 如果未登陆，则使用cookie里的userkey
     * 第一次如果没有临时用户，创建临时用户
     */

    @GetMapping("/getUserCartItems")
    @ResponseBody
    public List<CartItem> getUserCartItems(){
        List<CartItem> cartItems=cartService.getUserCartItems();

        return cartItems;
    }

//    删除商品
    @RequestMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

//    修改商品数量
    @RequestMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num){
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
//    勾选商品
    @RequestMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("check") Integer check){
        cartService.checkItem(skuId,check);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
//    获取整个购物车的信息
    @RequestMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
//        不需要传入用户信息，因为已经存在整个threadloca里了
        Cart cart=cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     * ~~~~~~~~~~`添加购物车，第一个请求将购物车添加到数据库，然后重新定向到第二个请求，到数据库里取数据
     */
    @RequestMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes ra) throws ExecutionException, InterruptedException {
//        因为在这个请求里带有addCart的逻辑，因此如果刷新页面会造成购物车内容累加，因此  重新定向的success页面
        cartService.addToCart(skuId, num); //数据库里添加购物车商品
//      model.addAttribute("item",cartItem);
//      return "success";
        ra.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }
//  跳转到成功页，每次都是刷新操作，而非添加
    @RequestMapping("/addToCartSuccess.html")
    public String addToCartSuccess(@RequestParam("skuId") Long skuId,Model model) {
//        拿到添加成功的数据后，重新到数据库里去取一下最新数据即可
        CartItem cartItem=cartService.getCartItem(skuId);
        model.addAttribute("item",cartItem);
        return "success";
    }
/**~~~~~~~~~~`添加购物车，完毕*/
}
