package com.tz.mall.product.web;

import com.tz.mall.product.entity.CategoryEntity;
import com.tz.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
        return "/index";
    }
}
