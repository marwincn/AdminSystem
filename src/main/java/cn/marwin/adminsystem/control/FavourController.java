package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.community.Favour;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.community.FavourService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("favour")
public class FavourController {
    @Autowired
    FavourService favourService;

    @ApiOperation(value = "点赞动态")
    @RequestMapping(value = "doFavour", method = RequestMethod.GET)
    public Result doFavour(Integer bid) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        Favour favour = new Favour();
        favour.setBid(bid);
        favour.setUid(user.getId());
        try {
            favourService.add(favour);
        } catch (Exception e) {
            return new Result(Result.ERROR, e.getMessage());
        }
        return new Result(Result.SUCCESS, "点赞成功！");
    }
}
