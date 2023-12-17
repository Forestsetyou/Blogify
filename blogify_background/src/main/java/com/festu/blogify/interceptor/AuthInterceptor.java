package com.festu.blogify.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse, Object o) throws Exception{
        Logger logger = LoggerFactory.getLogger(getClass());
        if(HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())){
            // 放行 Option 以解决 CORS 问题
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        if(httpServletRequest.getRequestURI().equals("/api/v1/user") && httpServletRequest.getMethod().equals("POST")){
            // 放行注册
            return true;
        }

        Subject subject = SecurityUtils.getSubject();
        // 检查 shiro 认证信息
        if(!subject.isAuthenticated()){
            logger.warn(" " + httpServletRequest.getMethod() + " => fail");
            return false;
        }
        return true;
    }
}
