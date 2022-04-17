package com.tz.mall.cart.interceptor;

import com.tz.common.constant.AuthServerConstant;
import com.tz.common.constant.CartConstant;
import com.tz.common.vo.MemberRespVo;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.tz.mall.cart.to.UserInfoTo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;


//@Component
public class CartInterceptor implements HandlerInterceptor {

//   创建本地线程，可以在controller，service，dao 进行同一个线程数据共享
    public static ThreadLocal<UserInfoTo> threadLocal=new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request,
                              HttpServletResponse response,
                             Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberRespVo member =
                (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (member!=null){
//            用户已经登陆了
            userInfoTo.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_SUER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }
//        如果没有临时用户，分配一个临时用户。不管是否登陆
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }

//        目标方法执行之前，将信息放入到thread
        threadLocal.set(userInfoTo);
        return true;
    }

//    业务执行之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        UserInfoTo userInfoTo = threadLocal.get();
//        如果没有cookie信息，再添加进去
        if (!userInfoTo.isTempUser()){
            Cookie cookie = new Cookie(CartConstant.TEMP_SUER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_SUER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }

    }
}
