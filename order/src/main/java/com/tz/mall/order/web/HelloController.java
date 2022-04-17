package com.tz.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {
    @RequestMapping("{page}.html")
    public String gotoPage(@PathVariable("page") String page){
        return page;
    }
}
