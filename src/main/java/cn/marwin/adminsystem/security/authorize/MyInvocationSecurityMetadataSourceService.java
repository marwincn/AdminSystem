package cn.marwin.adminsystem.security.authorize;

import cn.marwin.adminsystem.entity.security.Permission;
import cn.marwin.adminsystem.repository.security.PermissionDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    @Autowired
    PermissionDAO permissionDAO;

    private Map<String, Collection<ConfigAttribute>> map = null;

    // 该方法返回某个url需要的permission的name，或null
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (map == null) {
            loadResourceDefine();
        }
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        // 打印日志，方便调试
        System.out.println("RequestURI：" + request.getRequestURI() + "  Method：" + request.getMethod() + "  Time：" + TimeUtil.getCurrentTime());

        // 和请求的url比较，如果存在则返回需要的permission的name
        for (Map.Entry<String, Collection<ConfigAttribute>> entry: map.entrySet()) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(entry.getKey());
            if (matcher.matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    // 从数据库获取所有存在的权限
    public void loadResourceDefine(){
        map = new HashMap<>();
        List<Permission> permissions = permissionDAO.findAll();
        for(Permission permission : permissions) {
            Collection<ConfigAttribute> array = new ArrayList<>();
            // 放入的是permission的name
            ConfigAttribute cfg = new SecurityConfig(permission.getName());
            array.add(cfg);
            // 这里是一对一的关系，一个url只需要的一个permission
            map.put(permission.getUrl(), array);
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override public boolean supports(Class<?> clazz) {
        return true;
    }
}
