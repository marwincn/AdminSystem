package cn.marwin.adminsystem.service.security;

import cn.marwin.adminsystem.entity.security.Role;
import cn.marwin.adminsystem.entity.security.RolePermission;
import cn.marwin.adminsystem.repository.security.RoleDAO;
import cn.marwin.adminsystem.repository.security.RolePermissionDAO;
import cn.marwin.adminsystem.repository.security.UserRoleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    UserRoleDAO userRoleDAO;
    @Autowired
    RolePermissionDAO rolePermissionDAO;

    public List<Role> findAll() {
        List<Role> roles = roleDAO.findAll();
        for (Role role: roles) {
            initRole(role);
        }
        return roles;
    }

    public Role findById(Integer id) {
        Role role = roleDAO.findById(id).orElse(null);
        if (role == null) {
            return null;
        }
        return initRole(role);
    }

    public Role add(Role role) {
        return roleDAO.save(role);
    }

    /*
     * role：被更新的对象，它的perms[]是旧的
     * perms：新的perms[]
     */
    public void updatePerms(Role role, List<Integer> perms) {
        if (role.getPerms() == null) {
            role.setPerms(new ArrayList<>());
        }
        if (perms == null) {
            perms = new ArrayList<>();
        }
        if (!perms.equals(role.getPerms())) {
            for (Integer pid: role.getPerms()) {
                if (!perms.contains(pid)) {
                    rolePermissionDAO.deleteByRidAndPid(role.getId(), pid);
                }
            }
            for (Integer pid: perms) {
                if (!role.getPerms().contains(pid)) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRid(role.getId());
                    rolePermission.setPid(pid);
                    rolePermissionDAO.save(rolePermission);
                }
            }
        }
    }

    public Role update(Role role) {
        return roleDAO.save(role);
    }

    public void delete(Integer id) throws Exception {
        if (userRoleDAO.existsByRid(id)) {
            throw new Exception("请确认无用户使用该角色，然后重试！");
        }
        roleDAO.deleteById(id);
    }

    private Role initRole(Role role) {
        List<Integer> perms = new ArrayList<>();
        for (RolePermission rolePerms: rolePermissionDAO.findByRid(role.getId())) {
            perms.add(rolePerms.getPid());
        }
        role.setPerms(perms);
        return role;
    }
}
