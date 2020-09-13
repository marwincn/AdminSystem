package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.platform.Star;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.platform.ItemService;
import cn.marwin.adminsystem.service.platform.StarService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("star")
public class StarController {

    @Autowired
    StarService starService;

    @Autowired
    ItemService itemService;

    @ApiOperation(value = "添加收藏项目")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public HttpResult addStarItem(Integer iid) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        Item item = itemService.findById(iid);
        if (item == null) {
            return new HttpResult(HttpResult.ERROR, "该项目不存在！");
        }
        try {
            Star star = new Star();
            star.setIid(iid);
            star.setUid(user.getId());
            starService.add(star);
            return new HttpResult(HttpResult.SUCCESS, "添加成功");

        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR,e.getMessage());
        }

    }

    @ApiOperation(value = "是否收藏项目")
    @RequestMapping(value = "hasStar", method = RequestMethod.GET)
    public HttpResult hasStar(Integer iid) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.SUCCESS, "获取收藏状态成功", false);
        }
        return new HttpResult(HttpResult.SUCCESS, "获取收藏状态成功", starService.isStar(user.getId(), iid));
    }

    @ApiOperation(value = "获取自己的收藏项目")
    @RequestMapping(value = "listMyStars", method = RequestMethod.GET)
    public HttpResult listMy() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        return new HttpResult(HttpResult.SUCCESS, "获取个人的动态列表成功", starService.listByUid(user.getId()));
    }

    @ApiOperation(value = "获取某用户收藏项目")
    @RequestMapping(value = "listByUid", method = RequestMethod.GET)
    public HttpResult getStarList(Integer uid) {
        return new HttpResult(HttpResult.SUCCESS, "获取收藏列表成功", starService.listByUid(uid));
    }

    @ApiOperation(value = "删除收藏的项目")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public HttpResult deleteStarItem(Integer iid) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        starService.delete(iid, user.getId());
        return new HttpResult(HttpResult.SUCCESS, "删除成功");
    }
}
