package cn.marwin.adminsystem.repository.community;

import cn.marwin.adminsystem.entity.community.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogDAO extends JpaRepository<Blog, Integer> {
    List<Blog> findByAid(Integer aid);
    @Query(value = "select * from blog order by publish_time desc limit :size offset :offset",nativeQuery = true)
    List<Blog> findOnPage(@Param("size") Integer size, @Param("offset") Integer offset);
}
