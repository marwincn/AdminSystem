package cn.marwin.adminsystem.util;

import cn.marwin.adminsystem.entity.security.User;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class UserUtil {
    private static final String CONTEXT_ATTRIBUTE_OF_USER = "CONTEXT_ATTRIBUTE_OF_USER";

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static User getLoginUser() {
        return (User) RequestContextHolder.currentRequestAttributes()
                .getAttribute(CONTEXT_ATTRIBUTE_OF_USER, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * Spring Boot内部使用线程池响应请求，所以一个请求完要及时销毁ThreadLocal里的数据
     * 这里直接使用Spring的RequestContextHolder规避了这个问题
     *
     * @param user 用户信息
     */
    public static void setUser(User user) {
        RequestContextHolder.currentRequestAttributes()
                .setAttribute(CONTEXT_ATTRIBUTE_OF_USER, user, RequestAttributes.SCOPE_REQUEST);
    }
}
