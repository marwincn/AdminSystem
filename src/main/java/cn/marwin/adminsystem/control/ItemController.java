package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.platform.CatalogService;
import cn.marwin.adminsystem.service.platform.ItemService;
import cn.marwin.adminsystem.service.platform.RecomService;
import cn.marwin.adminsystem.service.platform.StarService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.util.ViewUtil;
import cn.marwin.adminsystem.facade.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("item")
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    StarService starService;
    @Autowired
    RecomService recomService;

    @ApiOperation(value = "获取分类列表")
    @RequestMapping(value = "getCataList", method = RequestMethod.GET)
    public Result getCataList() {
        return new Result(Result.SUCCESS, "获取分类列表成功", catalogService.findAll());
    }

    @ApiOperation(value = "获取热门项目列表")
    @RequestMapping(value = "getHotItems", method = RequestMethod.GET)
    public Result getHotItems(Integer size) {
        // 验证size
        return new Result(Result.SUCCESS, "获取热门项目列表成功", itemService.findHotItems(size));
    }

    @ApiOperation(value = "获取推荐项目列表")
    @RequestMapping(value = "getRecomItems", method = RequestMethod.GET)
    public Result getRecomItems(Integer size) {
        // 验证size
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.SUCCESS, "获取推荐项目列表成功", itemService.findHotItems(size));
        }
        return new Result(Result.SUCCESS, "获取推荐项目列表成功", recomService.getRecomItems(user.getId(), size));
    }

    @ApiOperation(value = "按时间顺序获取项目列表")
    @RequestMapping(value = "listByTime", method = RequestMethod.GET)
    public Result listAll(Integer pageSize, Integer currPage) {
        return new Result(Result.SUCCESS, "按时间顺序获取项目列表成功", itemService.findOnPage(pageSize, currPage));
    }

    @ApiOperation(value = "按分类获取项目列表")
    @RequestMapping(value = "listByCid", method = RequestMethod.GET)
    public Result list(Integer cid, Integer pageSize, Integer currPage) {
        return new Result(Result.SUCCESS, "按分类获取项目列表成功", itemService.findByCid(cid, pageSize, currPage));
    }

    @ApiOperation(value = "获取自己发布的项目")
    @RequestMapping(value = "listMyItems", method = RequestMethod.GET)
    public Result listMyItems() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登录！");
        }
        return new Result(Result.SUCCESS, "获取自己发布的项目成功", itemService.findByAid(user.getId()));
    }

    @ApiOperation(value = "通过id获取项目信息")
    @RequestMapping(value = "getById", method = RequestMethod.GET)
    public Result get(Integer id, HttpServletRequest request) {
        ViewUtil.logView("item", id, request);
        return new Result(Result.SUCCESS, "通过id获取项目信息成功", itemService.findById(id));
    }

    @ApiOperation(value = "发布项目")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Result add(Item item) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登录！");
        }
        item.setAid(user.getId());
        Item newItem = itemService.add(item);
        return new Result(Result.SUCCESS, "发布成功！", newItem.getId());
    }

    @ApiOperation(value = "更新项目")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(Integer id, String title, String description, Double price) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登录！");
        }

        Item item = itemService.findById(id);
        if (item.getAid() != user.getId()) {
            return new Result(Result.ERROR, "你没有权限修改！");
        }
        item.setTitle(title);
        item.setDescription(description);
        item.setPrice(price);
        itemService.update(item);
        return new Result(Result.SUCCESS, "修改成功！");
    }

    @ApiOperation(value = "下架项目")
    @RequestMapping(value = "close", method = RequestMethod.POST)
    public Result close(Integer id) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登录！");
        }

        Item item = itemService.findById(id);
        if (item.getAid() != user.getId()) {
            return new Result(Result.ERROR, "你没有权限修改！");
        }
        // 设置状态码为不可用
        item.setState(0);
        itemService.update(item);
        return new Result(Result.SUCCESS, "项目下架成功！");
    }

    @ApiOperation(value = "获取项目总数")
    @RequestMapping(value = "getItemCount", method = RequestMethod.GET)
    public Result getItemCount() {
        return new Result(Result.SUCCESS, "获取项目总数成功", itemService.count());
    }

    @ApiOperation(value = "获取分类下项目总数")
    @RequestMapping(value = "getItemCountByCid", method = RequestMethod.GET)
    public Result getItemCountByCid(Integer cid) {
        return new Result(Result.SUCCESS, "获取分类下项目总数成功", itemService.countByCid(cid));
    }

    @ApiOperation(value = "通过标题搜索项目")
    @RequestMapping(value = "searchByTitle", method = RequestMethod.GET)
    public Result searchByTitle(String keyword) {
        return new Result(Result.SUCCESS, "搜索成功！", itemService.searchByTitle(keyword));
    }
}
