package cn.marwin.adminsystem.repository.platform;

import cn.marwin.adminsystem.entity.platform.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CatalogDAO extends JpaRepository<Catalog, Integer> {
    @Query(value = "select * from catalog order by id limit :size offset :offset",nativeQuery = true)
    List<Catalog> findOnPage(@Param("size") Integer size, @Param("offset") Integer offset);
}
