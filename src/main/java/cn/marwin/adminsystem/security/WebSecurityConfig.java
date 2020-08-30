package cn.marwin.adminsystem.security;


import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.security.authenticate.MyUserDetailsService;
import cn.marwin.adminsystem.security.authorize.MyFilterSecurityInterceptor;
import cn.marwin.adminsystem.facade.UserFacade;
import cn.marwin.adminsystem.facade.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import net.sf.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    MyFilterSecurityInterceptor myFilterSecurityInterceptor;
    @Autowired
    UserDAO userDAO;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new PasswordEncoder() {
            // 设置密码加密
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            // 登陆验证逻辑
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果有允许匿名的url，填在下面
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginProcessingUrl("/user/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        String username = httpServletRequest.getParameter("username");
                        User user = userDAO.findByUsername(username).orElse(null);
                        JSONObject result = JSONObject.fromObject(new Result(Result.SUCCESS, "登录成功！", new UserFacade(user)));
                        out.write(result.toString());
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        JSONObject result = JSONObject.fromObject(new Result(Result.ERROR, "登录失败，账号或密码错误！"));
                        out.write(result.toString());
                        out.flush();
                        out.close();
                    }
                })
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .permitAll();
        // 关闭CSRF跨域
        http.cors().and().csrf().disable();
        // 设置自定义Filter，访问url前验证权限
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }

    @Override
    public void configure(WebSecurity web) {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**", "/lib/**", "/upload/**");
    }
}
