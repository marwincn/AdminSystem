package cn.marwin.adminsystem.service.platform;

import cn.marwin.adminsystem.entity.platform.Candidate;
import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.platform.Order;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.platform.CandidateDAO;
import cn.marwin.adminsystem.repository.platform.ItemDAO;
import cn.marwin.adminsystem.repository.platform.OrderDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    CandidateDAO candidateDAO;
    @Autowired
    ItemDAO itemDAO;
    @Autowired
    UserDAO userDAO;

    public Order add(Order order) throws Exception {
        if (orderDAO.existsByIid(order.getIid())) {
            throw new Exception("一个项目无法创建多个订单！");
        }
        // 设置竞选者成功选上的状态
        Candidate candidate = candidateDAO.findByIidAndUid(order.getIid(),order.getUid()).orElse(null);
        if (candidate == null) {
            throw new Exception("对不起，该竞选者已经退出竞选！");
        }
        // 设置候选人为已被选择状态
        candidate.setState(1);
        candidateDAO.save(candidate);
        // 设置项目已经竞选完成的状态
        Item item = itemDAO.findById(order.getIid()).orElse(null);
        item.setState(2);
        itemDAO.save(item);
        order.setState(0);
        order.setCreateTime(TimeUtil.getCurrentTime());
        return orderDAO.save(order);
    }

    public List<Order> findOnPage(Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<Order> orders = orderDAO.findOnPage(pageSize, offset);
        for (Order order: orders) {
            initOrder(order);
        }
        return orders;
    }

    public Long count() {
        return orderDAO.count();
    }

    public Order update(Order order) {
        order.setModifyTime(TimeUtil.getCurrentTime());
        return orderDAO.save(order);
    }

    public void delete(Integer id) {
        orderDAO.deleteById(id);
    }

    public Order findById(Integer id) {
        return initOrder(orderDAO.findById(id).orElse(null));
    }

    public List<Order> findNewOrders(Integer size) {
        if (size <= 0) {
            return null;
        }
        List<Order> orders = orderDAO.findNewOrders(size);
        for (Order order: orders) {
            initOrder(order);
        }
        return orders;
    }

    public List<Order> findByAid(Integer aid) {
        List<Order> orders = orderDAO.findByAid(aid);
        for (Order order: orders) {
            initOrder(order);
        }
        return orders;
    }

    public List<Order> findByUid(Integer uid) {
        List<Order> orders = orderDAO.findByUid(uid);
        for (Order order: orders) {
            initOrder(order);
        }
        return orders;
    }

    public Order findByIid(Integer iid) {
        return initOrder(orderDAO.findByIid(iid).orElse(null));
    }

    public Boolean existsByIid(Integer iid) {
        return orderDAO.existsByIid(iid);
    }

    private Order initOrder(Order order) {
        if (order == null) {
            return null;
        }
        User author = userDAO.findById(order.getAid()).orElse(null);
        if (author != null) {
            order.setAuthor(author.getNickname());
        } else {
            order.setAuthor("用户已注销");
        }
        User user = userDAO.findById(order.getUid()).orElse(null);
        if (user != null) {
            order.setUser(user.getNickname());
        } else {
            order.setUser("用户已注销");
        }
        Item item = itemDAO.findById(order.getIid()).orElse(null);
        if (item != null) {
            order.setItemTitle(item.getTitle());
        } else {
            order.setItemTitle("项目不存在");
        }
        return order;
    }
}
