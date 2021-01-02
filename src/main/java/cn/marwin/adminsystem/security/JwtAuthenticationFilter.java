package cn.marwin.adminsystem.security;

import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.security.UserService;
import cn.marwin.adminsystem.util.JwtUtil;
import cn.marwin.adminsystem.util.UserUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * todo：后续认证和鉴权跨域划分成两个filter
 */
@WebFilter(urlPatterns = "/*")
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        // 判断请求的接口是否需要认证
        if (!needAuthorization(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 获取token
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "user-token".equals(cookie.getName()))
                .findAny()
                .map(Cookie::getValue)
                .orElse("");
        // 未认证的情况
        if (StringUtils.isEmpty(token)) {
            setResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 鉴权
        String username = JwtUtil.verify(token);
        User user = userService.findByUsername(username);
        if (!hasPermission(user, url)) {
            setResponse(request, response, HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        UserUtil.setUser(user);
        filterChain.doFilter(request, response);
    }

    private boolean needAuthorization(String url) {
        if (url.equals("/user/login") || url.equals("/user/register")) {
            return false;
        }
        return true;
    }

    private boolean hasPermission(User user, String url) {
        return true;
    }

    /**
     * 由于错误处理没有走doFilter，所以没有走到corsFilter要手动设置跨域头
     *
     * @param request 获取origin，设置allow origin为*前端会报错
     * @param response 设置跨域头和http status
     * @param status 要响应的http status
     */
    private void setResponse(HttpServletRequest request, HttpServletResponse response, int status) {
        response.setStatus(status);
        // 跨域设置
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "*");
    }

}
