package cn.marwin.adminsystem.repository.security;

import cn.marwin.adminsystem.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Integer> {
    @Query(value = "select * from user order by id limit :size offset :offset",nativeQuery = true)
    List<User> findOnPage(@Param("size")Integer size, @Param("offset")Integer offset);
    List<User> findByUsernameLike(String keyword);
    Optional<User> findByUsername(String username);
    List<User> findByNicknameLike(String keyword);
    Optional<User> findByNickname(String nickname);
    Boolean existsByNickname(String nickname);
    Boolean existsByUsername(String username);
}
