package cn.marwin.adminsystem.service.community;

import cn.marwin.adminsystem.entity.community.Favour;
import cn.marwin.adminsystem.repository.community.FavourDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavourService {
    @Autowired
    FavourDAO favourDAO;

    public Favour add(Favour favour) throws Exception {
        if (existsByBidAndUid(favour.getBid(), favour.getUid())){
            throw new Exception("您已经点赞过该动态了！");
        }
        return favourDAO.save(favour);
    }

    public void delete(Integer id) {
        favourDAO.deleteById(id);
    }

    public Boolean existsByBidAndUid(Integer bid, Integer uid) {
        return favourDAO.existsByBidAndUid(bid, uid);
    }

    public Favour findById(Integer id) {
        return favourDAO.findById(id).orElse(null);
    }
}
