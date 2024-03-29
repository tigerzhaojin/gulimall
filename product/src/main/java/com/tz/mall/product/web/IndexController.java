package com.tz.mall.product.web;

import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.service.CategoryService;
import com.tz.mall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","index.html"})
    public String indexPage(Model model){
//        查出一级分类
        List<CategoryEntity> categoryEntities= categoryService.getTopLevelCats();
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

//    $.getJSON("index/catalog.json",function (data)
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String,List<Catelog2Vo>> getCatelogJson(){
        Map<String,List<Catelog2Vo>> resMap= categoryService.getCatelogJson();
        return resMap;
    }
}
