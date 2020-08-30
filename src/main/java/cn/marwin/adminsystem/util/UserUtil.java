package cn.marwin.adminsystem.util;

import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.security.authenticate.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static User getLoginUser() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (obj.getClass().getSimpleName().equals("MyUserDetails")) {
            return ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } else {
            return null;
        }
    }
}
