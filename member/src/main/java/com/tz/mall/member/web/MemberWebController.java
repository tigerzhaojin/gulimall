package com.tz.mall.member.web;


import com.alibaba.fastjson.JSON;
import com.tz.common.utils.R;
import com.tz.mall.member.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberWebController {
    @Autowired
    OrderFeignService orderFeignService;
//    @ResponseBody
    @RequestMapping("/memberOrder.html")
    public String memberOrder(@RequestParam(value = "pageNum" ,defaultValue = "1") Integer pageNum,
                              Model model){
        Map<String,Object> page =new HashMap<>();
        page.put("page",pageNum.toString());
        R r = orderFeignService.listWithItem(page);
        System.out.println(JSON.toJSONString(r));
        model.addAttribute("orders",r);
        return "orderList";
    }
}
