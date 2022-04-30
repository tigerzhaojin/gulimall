package com.tz.mall.order.web;

import com.alipay.api.AlipayApiException;
import com.tz.mall.order.config.AlipayTemplate;
import com.tz.mall.order.service.OrderService;
import com.tz.mall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayWebController {
    @Autowired
    AlipayTemplate alipayTemplate;
    @Autowired
    OrderService orderService;

    @GetMapping(value="/payOrder",produces = "text/html")
    @ResponseBody
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
//        PayVo payVo = new PayVo();
//        payVo.setBody();
//        payVo.setOut_trade_no(payOrder);
//        payVo.setTotal_amount();
//        payVo.setSubject();
        PayVo payVo =orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        System.out.println(pay);
        return pay;
    }
}
