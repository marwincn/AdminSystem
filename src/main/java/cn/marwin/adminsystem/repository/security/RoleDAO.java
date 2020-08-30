package cn.marwin.adminsystem.repository.security;

import cn.marwin.adminsystem.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer> {
}
