package cn.marwin.adminsystem.repository.platform;

import cn.marwin.adminsystem.entity.platform.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDAO extends JpaRepository<Order, Integer> {
    List<Order> findByAid(Integer aid);
    List<Order> findByUid(Integer uid);
    Optional<Order> findByIid(Integer iid);
    Boolean existsByIid(Integer iid);
    @Query(value = "select * from order_ order by create_time desc limit :size",nativeQuery = true)
    List<Order> findNewOrders(@Param("size") Integer size);
    @Query(value = "select * from order_ order by create_time desc limit :size offset :offset",nativeQuery = true)
    List<Order> findOnPage(@Param("size") Integer size, @Param("offset") Integer offset);
}
