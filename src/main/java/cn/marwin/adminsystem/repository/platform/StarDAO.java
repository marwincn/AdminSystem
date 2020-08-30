package cn.marwin.adminsystem.repository.platform;

import cn.marwin.adminsystem.entity.platform.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StarDAO extends JpaRepository<Star,Integer> {
    Boolean existsByUidAndIid(Integer uid, Integer iid);
    Boolean existsByUid(Integer uid);
    Integer countByIid(Integer iid);
    List<Star> findByUid(Integer uid);
    @Transactional
    void deleteByIidAndUid(Integer iid, Integer uid);
}
