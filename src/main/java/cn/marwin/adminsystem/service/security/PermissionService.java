package cn.marwin.adminsystem.service.security;

import cn.marwin.adminsystem.entity.security.Permission;
import cn.marwin.adminsystem.repository.security.PermissionDAO;
import cn.marwin.adminsystem.repository.security.RolePermissionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    PermissionDAO permissionDAO;
    @Autowired
    RolePermissionDAO rolePermissionDAO;

    public Permission add(Permission permission) {
        return permissionDAO.save(permission);
    }

    public List<Permission> findAll() {
        return permissionDAO.findAll();
    }

    public Permission findById(Integer id) {
        return permissionDAO.findById(id).orElse(null);
    }

    public void delete(Integer id) throws Exception {
        if (rolePermissionDAO.existsByPid(id)) {
            throw new Exception("请确认无角色使用该权限时，再重试！");
        }
        permissionDAO.deleteById(id);
    }

    public void update(Permission perm) {
        permissionDAO.save(perm);
    }
}
