package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.platform.Candidate;
import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.platform.Order;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.platform.ItemService;
import cn.marwin.adminsystem.service.platform.OrderService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    ItemService itemService;

    @ApiOperation(value = "生成订单(uid,iid必须传入）")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResult create(Candidate candidate) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        Item item = itemService.findById(candidate.getIid());
        if (item == null) {
            return new HttpResult(HttpResult.ERROR, "该项目不存在！");
        }
        if (item.getState() == 0) {
            return new HttpResult(HttpResult.ERROR, "下架项目无法生产订单！");
        }
        if (item.getAid() != user.getId()) {
            return new HttpResult(HttpResult.ERROR, "您没有权限！");
        }
        Order order = new Order();
        order.setIid(item.getId());
        order.setAid(item.getAid());
        order.setUid(candidate.getUid());
        order.setPrice(item.getPrice());
        Order newOrder = null;
        try {
            newOrder = orderService.add(order);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "创建订单成功！", newOrder.getId());
    }

    @ApiOperation(value = "列出自己接的订单")
    @RequestMapping(value = "listMyJoined", method = RequestMethod.GET)
    public HttpResult listMyJoined() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        return new HttpResult(HttpResult.SUCCESS, "获取个人接的订单成功", orderService.findByUid(user.getId()));
    }

    @ApiOperation(value = "列出自己创建的订单")
    @RequestMapping(value = "listMyCreated", method = RequestMethod.GET)
    public HttpResult listMyCreated() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        return new HttpResult(HttpResult.SUCCESS, "获取个人创建订单成功", orderService.findByAid(user.getId()));
    }

    @ApiOperation(value = "获取最近生成的订单")
    @RequestMapping(value = "getNewOrders", method = RequestMethod.GET)
    public HttpResult getNewOrders(Integer size) {
        List<Order> orders = new ArrayList<>();
        for (Order order: orderService.findNewOrders(size)) {
            Order simpleOrder = new Order();
            simpleOrder.setAuthor(order.getAuthor());
            simpleOrder.setUser(order.getUser());
            simpleOrder.setPrice(order.getPrice());
            simpleOrder.setItemTitle(order.getItemTitle());
            simpleOrder.setCreateTime(order.getCreateTime());
            orders.add(simpleOrder);
        }
        return new HttpResult(HttpResult.SUCCESS, "获取最近生成的订单成功", orders);
    }
}
