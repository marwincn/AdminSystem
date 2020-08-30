package cn.marwin.adminsystem.repository.security;

import cn.marwin.adminsystem.entity.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleDAO extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUid(Integer uid);
    @Transactional
    void deleteByUidAndRid(Integer uid, Integer rid);
    boolean existsByRid(Integer rid);
    //void deleteByUid(Integer uid);
}
