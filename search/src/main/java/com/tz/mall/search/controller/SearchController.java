package com.tz.mall.search.controller;

import com.tz.mall.search.service.MallSearchService;
import com.tz.mall.search.vo.SearchParam;
import com.tz.mall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    MallSearchService mallSearchService;
    @RequestMapping("/list.html")
    public String listPage (SearchParam param, Model model) {
        SearchResult result =mallSearchService.search(param);
        model.addAttribute("result",result);

        return "list";
    }
}
