package com.tz.mall.order.web;

import com.tz.common.exception.NoStockException;
import com.tz.mall.order.service.OrderService;
import com.tz.mall.order.vo.OrderConfirmVo;
import com.tz.mall.order.vo.OrderSubmitVo;
import com.tz.mall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {
    @Autowired
    OrderService orderService;
    @RequestMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo=orderService.orderConfirm();
        model.addAttribute("orderConfirmData",confirmVo);
        return "confirm";
    }

    @PostMapping("/orderSubmit")
    private String submitOrder(OrderSubmitVo submitVo, Model model, RedirectAttributes attributes){
        try {
            SubmitOrderResponseVo vo=orderService.submitOrder(submitVo);
            if (vo.getCode()==0){
//            success, go to pay
                model.addAttribute("submitOrderResp",vo);
                return "pay";
            } else {
//            error ,go to cart
                String msg="Order Failue : ";
                switch (vo.getCode()){
                    case 1 : msg+="Order expired";break;
                    case 2 : msg+="Order price has changed";break;
                    case 3 : msg+="No enough inventory";
                }
                attributes.addFlashAttribute("msg",msg);
                return "redirect:http://order.gulimall.com/toTrade";
            }
        } catch(Exception e) {
            if (e instanceof NoStockException) {
                String msg = ((NoStockException) e).getMessage();
                attributes.addFlashAttribute("msg", msg);

            }
            return "redirect:http://order.gulimall.com/toTrade";
        }

    }
}
