package com.tz.mall.product.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/search/list.html")
    public String test(){
        return "首页的List.html";
    }
}
