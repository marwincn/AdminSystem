package cn.marwin.adminsystem.service.platform;

import cn.marwin.adminsystem.entity.platform.Catalog;
import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.platform.CandidateDAO;
import cn.marwin.adminsystem.repository.platform.CatalogDAO;
import cn.marwin.adminsystem.repository.platform.ItemDAO;
import cn.marwin.adminsystem.repository.platform.StarDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import cn.marwin.adminsystem.util.ViewUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemDAO itemDAO;
    @Autowired
    CatalogDAO catalogDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    CandidateDAO candidateDAO;
    @Autowired
    StarDAO starDAO;

    public Item add(Item item) {
        // 添加验证
        item.setState(1);
        item.setCreateTime(TimeUtil.getCurrentTime());
        return itemDAO.save(item);
    }

    public void delete(Integer id) {
        itemDAO.deleteById(id);
    }

    public Item update(Item item) {
        item.setModifyTime(TimeUtil.getCurrentTime());
        return itemDAO.save(item);
    }

    public Item findById(Integer id) {
        Item item = itemDAO.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        if (item.getState() == 0) {
            Item i = new Item();
            i.setTitle("项目已下架");
            return i;
        }
        return initItem(item);
    }

    public Item findByIdC(Integer id) {
        Item item = itemDAO.findById(id).orElse(null);
        if (item == null) {
            return null;
        }
        return initItem(item);
    }

    public List<Item> findOnPage(Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<Item> items = itemDAO.findOnPage(pageSize, offset);
        for (Item item: items) {
            initItem(item);
        }
        return items;
    }

    public List<Item> findOnPageC(Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<Item> items = itemDAO.findOnPageC(pageSize, offset);
        for (Item item: items) {
            initItem(item);
        }
        return items;
    }

    public Long count() {
        return itemDAO.countByStateNot(0);
    }

    public Long countC() {
        return itemDAO.count();
    }

    public List<Item> findByAid(Integer aid) {
        List<Item> items = itemDAO.findByAidAndStateNot(aid, 0);
        for (Item item: items) {
            initItem(item);
        }
        return items;
    }

    public Long countByCid(Integer cid) {
        return itemDAO.countByCidAndStateNot(cid, 0);
    }

    public List<Item> findByCid(Integer cid, Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<Item> items = itemDAO.findOnPageByCid(cid, pageSize, offset);
        for (Item item: items) {
            initItem(item);
        }
        return items;
    }

    public List<Item> searchByTitle(String keyword) {
        List<Item> items = itemDAO.findByTitleLikeAndStateNot("%" + keyword + "%", 0);
        for (Item item: items) {
            initItem(item);
        }
        return items;
    }

    public List<Item> findHotItems(Integer size) {
        if (size <= 0) {
            return null;
        }
        return itemDAO.findHotItem(size);
    }

    private Item initItem(Item item) {
        if (item == null) {
            return null;
        }
        User user = userDAO.findById(item.getAid()).orElse(null);
        if (user != null) {
            item.setAuthor(user.getNickname());
            item.setAvatar(user.getAvatar());
        } else {
            item.setAuthor("用户已注销");
        }
        Catalog catalog = catalogDAO.findById(item.getCid()).orElse(null);
        if (catalog != null) {
            item.setCatalog(catalog.getName());
        } else {
            item.setCatalog("无分类");
        }
        item.setStarCount(starDAO.countByIid(item.getId()));
        item.setCandCount(candidateDAO.countByIid(item.getId()));
        item.setViewCount(ViewUtil.getViewCount("item", item.getId()));
        return item;
    }
}
