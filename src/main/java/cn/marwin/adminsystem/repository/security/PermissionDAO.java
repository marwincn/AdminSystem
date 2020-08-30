package cn.marwin.adminsystem.repository.security;

import cn.marwin.adminsystem.entity.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDAO extends JpaRepository<Permission, Integer> {
}
