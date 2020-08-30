package cn.marwin.adminsystem.repository.security;

import cn.marwin.adminsystem.entity.security.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolePermissionDAO extends JpaRepository<RolePermission, Integer> {
    List<RolePermission> findByRid(Integer rid);
    @Transactional
    void deleteByRidAndPid(Integer rid, Integer pid);
    boolean existsByPid(Integer pid);
}
