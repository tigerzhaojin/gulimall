package com.tz.mall.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tz.common.utils.HttpUtils;
import com.tz.common.to.SocialUserVo;
import com.tz.common.utils.R;
import com.tz.mall.authserver.feign.MemberFeignService;
import com.tz.common.vo.MemberRespVo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OAuth2Controller {
    @Autowired
    MemberFeignService memberFeignService;
    @RequestMapping("/oauth2.0/git/success")
    public String gitAuth(@RequestParam("code") String code,
                          HttpSession httpSession,
                          HttpServletResponse servletResponse) throws Exception {

//        1.拿到git返回的code码
//        2.按照code获取token
        Map<String,String > queryMap= new HashMap<>();
        queryMap.put("client_id","a10953a98ae68ae40d34");
        queryMap.put("client_secret","d3dd53a2eaec859c8fac2f8ae420bdebab92e23c");
        queryMap.put("code",code);
        Map<String,String> headMap=new HashMap<>();
//        github要加上head，否则默认不返回json
        headMap.put("Content-Type","application/json");
        headMap.put("Accept","application/json");
        Map<String,String > bodyMap= new HashMap<>();


        HttpResponse response = HttpUtils.doPost("https://github.com",
                "/login/oauth/access_token",
                "post",
                headMap,
                queryMap,
                bodyMap);
        if (response.getStatusLine().getStatusCode()==200){
            String json = EntityUtils.toString(response.getEntity());
//            封装token信息
            SocialUserVo.GitVo gitVo = JSON.parseObject(json, SocialUserVo.GitVo.class);
 //           根据token信息,查出用户信息,
            //   之前使用过的headMap和queryMap可以清空后，封装新的参数
            headMap.clear();
            headMap.put("Authorization","Bearer "+gitVo.getAccess_token());
            queryMap.clear();
            HttpResponse userResponse = HttpUtils.doGet("https://api.github.com/user", "", "get",
                    headMap, null);
            HttpEntity entity = userResponse.getEntity();
            String jsonUserInfo = EntityUtils.toString(entity);
            JSONObject jsonObject = JSON.parseObject(jsonUserInfo);
//           接入member系统，将查出的social账户与系统内用户关联。如果有用户则返回，如果没有新增用户
            String id = jsonObject.getString("id");
            String login = jsonObject.getString("login");
            gitVo.setUid(id);
            gitVo.setUname(login);
            R authLogin = memberFeignService.login(gitVo);
            Object data = authLogin.get("data");
            ObjectMapper objectMapper = new ObjectMapper();

            MemberRespVo respVo = objectMapper.convertValue(data, MemberRespVo.class);
            System.out.println("登陆成功，用户信息："+data);
//           子域之间,  gulimall.com, auth.gulimall.com, search.gulimall.com ....
//           发卡的时候指定域名，即使是子域名签发的，也能让父域直接使用，就解决了session共享的问题
            httpSession.setAttribute("loginUser",respVo);
//            servletResponse.addCookie();
            return "redirect:http://gulimall.com/";

        }  else {
            return "redirect:http://auth.gulimall.com/login.html";
        }

//        System.out.println(post);


    }
}
