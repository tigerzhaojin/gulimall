package com.tz.mall.member.interceptor;

import com.tz.common.constant.AuthServerConstant;
import com.tz.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    public static ThreadLocal<MemberRespVo> loginUser=new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri=request.getRequestURI();
//      放行login的请求
        Boolean mathch=new AntPathMatcher().match("/member/**",uri);
        if (mathch) {
            return true;
        }

        MemberRespVo attribute =
                (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute!=null){
            loginUser.set(attribute);
            return true;
        } else {
            request.getSession().setAttribute("msg","请先登陆!");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }


    }
}
