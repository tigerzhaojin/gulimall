package com.tz.mall.authserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.common.constant.AuthServerConstant;
import com.tz.common.utils.R;
import com.tz.common.vo.MemberRespVo;
import com.tz.mall.authserver.feign.MemberFeignService;
import com.tz.mall.authserver.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    MemberFeignService memberFeignService;
    /**
     * 1.发送一个请求，可以不通过controller跳转到一个页面
     * 2.通过springMvc，将请求映射到页面
     * */

    @GetMapping({"login.html"})
    public String loginPage(HttpSession httpSession){
        Object attribute = httpSession.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute==null){
            return "login";
        } else {
            return "redirect:http://gulimall.com";
        }

    }

    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo,
                        RedirectAttributes attributes,
                        HttpSession session){
        System.out.println(userLoginVo.toString());
        R login = memberFeignService.login(userLoginVo);
        if (login.getCode()==0){
            Object data = login.get("data");
//            登陆成功，放到session中
            ObjectMapper objectMapper = new ObjectMapper();
            MemberRespVo respVo = objectMapper.convertValue(data, MemberRespVo.class);
            session.setAttribute(AuthServerConstant.LOGIN_USER,respVo);
            return "redirect:http://gulimall.com";
        } else {
            Map<String,String > errMap=new HashMap<>();

            errMap.put("msg",login.getMsg());
            attributes.addFlashAttribute("errors",errMap);

            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
    @RequestMapping("/success")
    public String success(){
        return "success";
    }
}
