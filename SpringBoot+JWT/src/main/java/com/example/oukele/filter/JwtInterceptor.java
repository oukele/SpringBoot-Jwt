package com.example.oukele.filter;

import com.example.oukele.annotation.JwtIgnore;
import com.example.oukele.jwt.Audience;
import com.example.oukele.jwt.JwtTokenUtil;
import com.sun.deploy.net.HttpResponse;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * token 验证拦截器
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 忽略带 JwtIgnore 注解的请求，不做后续 token 认证验证
        if( handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
            if( jwtIgnore != null ){
                return true;
            }
        }
        // 预检请求不进行 认证验证
        if(HttpMethod.OPTIONS.equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头信息authorization信息
        String authHeader = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            System.out.println("### 用户未登录，请先登录 ###");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("{\"code\":5000,\"msg\":\"请先登陆\",\"flag\":false}");
            return false;
        }
        // 获取 token
        String token = authHeader.substring(7);
        if(audience == null){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            audience = (Audience) factory.getBean("audience");
        }
        // 验证token是否有效--
        Claims parseJWT = JwtTokenUtil.parseJWT(token, audience.getBase64Secret());
        if( parseJWT == null ){
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println("{\"code\":5000,\"msg\":\"无效的token,请重新登录\",\"flag\":false}");
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
