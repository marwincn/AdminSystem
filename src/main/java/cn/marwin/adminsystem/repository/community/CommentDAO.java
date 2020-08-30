package cn.marwin.adminsystem.repository.community;

import cn.marwin.adminsystem.entity.community.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentDAO extends JpaRepository<Comment, Integer> {
    Integer countByBid(Integer bid);
    List<Comment> findByBid(Integer bid);
    List<Comment> findByAid(Integer aid);
    @Query(value = "select * from comment where bid = :bid order by id limit :size offset :offset",nativeQuery = true)
    List<Comment> findOnPage(@Param("bid") Integer bid, @Param("size") Integer size, @Param("offset") Integer offset);
    @Transactional
    void deleteByBid(Integer bid);
}
