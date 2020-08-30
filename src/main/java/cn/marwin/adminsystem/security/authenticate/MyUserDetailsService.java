package cn.marwin.adminsystem.security.authenticate;

import cn.marwin.adminsystem.repository.security.PermissionDAO;
import cn.marwin.adminsystem.repository.security.RoleDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.repository.security.RolePermissionDAO;
import cn.marwin.adminsystem.repository.security.UserRoleDAO;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.entity.security.Permission;
import cn.marwin.adminsystem.entity.security.Role;
import cn.marwin.adminsystem.entity.security.RolePermission;
import cn.marwin.adminsystem.entity.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 认证
@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    UserRoleDAO userRoleDAO;
    @Autowired
    RolePermissionDAO rolePermissionDAO;
    @Autowired
    PermissionDAO permissionDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 获取用户
        User user = userDAO.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        // 获取该用户的所有角色
        List<UserRole> userRoles = userRoleDAO.findByUid(user.getId());
        for (UserRole userRole: userRoles) {
            Role role = roleDAO.findById(userRole.getRid()).orElse(null);
            if (role != null) {
                // 获取该角色的所有权限
                List<RolePermission> rolePermissions = rolePermissionDAO.findByRid(role.getId());
                for (RolePermission rolePermission: rolePermissions) {
                    Permission permission = permissionDAO.findById(rolePermission.getPid()).orElse(null);
                    // 将权限名加入authorities
                    if (permission != null && permission.getName() != null) {
                        // 这里的权限是permission的name，而不是url
                        authorities.add(new SimpleGrantedAuthority(permission.getName()));
                    }
                }
            }
        }
        // 返回userdetails和authorities
        return new MyUserDetails(user, authorities);
    }
}
