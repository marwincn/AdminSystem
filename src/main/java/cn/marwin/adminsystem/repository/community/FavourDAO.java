package cn.marwin.adminsystem.repository.community;

import cn.marwin.adminsystem.entity.community.Favour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FavourDAO extends JpaRepository<Favour, Integer> {
    Integer countByBid(Integer bid);
    Boolean existsByBidAndUid(Integer bid, Integer uid);
    @Transactional
    void deleteByBid(Integer bid);
    //void deleteByBidAndUid(Integer bid, Integer uid);
}
