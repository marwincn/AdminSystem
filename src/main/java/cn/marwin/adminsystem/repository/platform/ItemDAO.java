package cn.marwin.adminsystem.repository.platform;

import cn.marwin.adminsystem.entity.platform.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDAO extends JpaRepository<Item, Integer> {
    // 过滤掉state=0的项目
    @Query(value = "select * from item where state <> 0 order by create_time desc limit :size offset :offset",nativeQuery = true)
    List<Item> findOnPage(@Param("size") Integer size, @Param("offset") Integer offset);
    // 不过滤
    @Query(value = "select * from item order by create_time desc limit :size offset :offset",nativeQuery = true)
    List<Item> findOnPageC(@Param("size") Integer size, @Param("offset") Integer offset);

    // 过滤掉state=0的项目
    @Query(value = "select * from item where cid=:cid and state <> 0 order by create_time desc limit :size offset :offset",nativeQuery = true)
    List<Item> findOnPageByCid(@Param("cid") Integer cid, @Param("size") Integer size, @Param("offset") Integer offset);

    // 过滤掉state=0的项目
    @Query(value = "select * from item where state <> 0 and id in (select iid from (select iid from itemview group by iid order by count(*) desc LIMIT :size) as hot_items);",nativeQuery = true)
    List<Item> findHotItem(@Param("size") Integer size);

    // 过滤掉state=0的项目
    List<Item> findByAidAndStateNot(Integer aid, Integer state);
    // 不过滤
    List<Item> findByAid(Integer aid);

    // 过滤掉state=0的项目
    List<Item> findByTitleLikeAndStateNot(String keyword, Integer state);
    // 不过滤
    List<Item> findByTitleLike(String keyword);

    // 过滤掉state=0的项目
    Long countByCidAndStateNot(Integer cid, Integer state);
    // 不过滤
    Long countByCid(Integer cid);

    // 过滤掉state=0的项目
    Long countByStateNot(Integer state);

    boolean existsByCid(Integer cid);

}
