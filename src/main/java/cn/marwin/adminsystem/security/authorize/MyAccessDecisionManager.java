package cn.marwin.adminsystem.security.authorize;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * decide 方法是判定是否拥有权限的决策方法，
     * Authentication含有请求url的用户的权限信息
     * Object含有发起请求的request信息，HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，包含数据库里所有的permission
     * 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     *
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        // 如果权限表为空则不需要判定，直接放行
        if(null== collection || collection.size() ==0) {
            return;
        }
        // 因为url和permission是一对一关系，所有这里的collection只有一个
        for (ConfigAttribute attribute: collection) {
            // 取出需要的权限的permission
            String need = attribute.getAttribute();
            for (GrantedAuthority authority: authentication.getAuthorities()) {
                // 拥有权限
                if (need.trim().equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        // 无权限
        throw new AccessDeniedException("NO RIGHT!");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }
}
