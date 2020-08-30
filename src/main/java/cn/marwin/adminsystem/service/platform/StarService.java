package cn.marwin.adminsystem.service.platform;

import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.platform.Star;
import cn.marwin.adminsystem.repository.platform.StarDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StarService {
    @Autowired
    StarDAO starDAO;
    @Autowired
    ItemService itemService;

    public void add(Star star) throws Exception{
        if (starDAO.existsByUidAndIid(star.getUid(), star.getIid())) {
            throw new Exception("您已收藏该项目");
        }
        star.setAddTime(TimeUtil.getCurrentTime());
        starDAO.save(star);
    }

    public List<Item> listByUid(Integer uid) {
        List<Item> items = new ArrayList<>();
        for (Star star: starDAO.findByUid(uid)) {
            items.add(itemService.findById(star.getIid()));
        }
        return items;
    }

    public boolean isStar(Integer uid, Integer iid) {
        return starDAO.existsByUidAndIid(uid, iid);
    }

    public Star findById(Integer id) {
        return starDAO.findById(id).orElse(null);
    }

    public void delete(int iid, int uid) {
        starDAO.deleteByIidAndUid(iid, uid);
    }

    public Integer countByIid(int iid) {
        return starDAO.countByIid(iid);
    }
}
