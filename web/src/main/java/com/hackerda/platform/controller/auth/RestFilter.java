package com.hackerda.platform.controller.auth;

import com.hackerda.platform.controller.auth.JWTToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.HeaderFooterRecord;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chenjuanrong
 */
public class RestFilter extends BasicHttpAuthenticationFilter {

    public static final String NAME = "JWTFilter";

    /**
     * 检查是否登陆请求
     */
    private boolean isLoginAttempt(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String servletPath = req.getServletPath();
        return "/login".equals(servletPath);
    }

    /**
     * 这个方法是对处理没有获得授权的请求
     * 如果是登陆的请求的我们直接放行，也可以在配置文件中为登陆的url配置直接放行
     * 对于需要校验token的接口，用subject login来校验是否能获取系统的权限
     *
     * @param request  the incoming <code>ServletRequest</code>
     * @param response the outgoing <code>ServletResponse</code>
     * @return true if the request should be processed; false if the request should not continue to be processed
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        if(!isLoginAttempt(request)){

            try {
                getSubject(request, response).login(getToken(request));
            }catch (AuthenticationException e){
                sendChallenge(request, response);
            }
        }

        return true;

    }

    private AuthenticationToken getToken(ServletRequest request) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        if(StringUtils.isEmpty(token)) {
            token = httpServletRequest.getParameter("token");
        }
        String servletPath = httpServletRequest.getServletPath();

        if (servletPath.contains("community")) {
            return new UserJWTToken(token);
        }else {
            return new JWTToken(token);
        }
    }
}
