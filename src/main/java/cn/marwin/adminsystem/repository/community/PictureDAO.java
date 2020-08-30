package cn.marwin.adminsystem.repository.community;

import cn.marwin.adminsystem.entity.community.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PictureDAO extends JpaRepository<Picture, Integer> {
    List<Picture> findByBid(Integer bid);
    @Query(value = "select * from picture order by id limit :size offset :offset",nativeQuery = true)
    List<Picture> findOnPage(@Param("size") Integer size, @Param("offset") Integer offset);
    @Transactional
    void deleteByBid(Integer bid);
}
