package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.community.Comment;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.community.CommentService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @ApiOperation(value = "发表评论")
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public Result publish(Integer bid, String content) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        Comment comment = new Comment();
        comment.setBid(bid);
        comment.setContent(content);
        comment.setAid(user.getId());
        commentService.add(comment);
        return new Result(Result.SUCCESS, "评论成功。");
    }

    @ApiOperation(value = "回复评论 (rid为要回复的评论id")
    @RequestMapping(value = "reply", method = RequestMethod.POST)
    public Result reply(Integer bid, String content, Integer rid) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        Comment comment = new Comment();
        comment.setBid(bid);
        comment.setRid(rid);
        comment.setContent(content);
        comment.setAid(user.getId());
        commentService.add(comment);
        return new Result(Result.SUCCESS, "评论成功。");
    }

    @ApiOperation(value = "删除评论")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(Integer id) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        Comment comment = commentService.findById(id);
        if (comment == null) {
            return new Result(Result.SUCCESS, "删除成功。");
        }
        if (comment.getAid() == user.getId()) {
            commentService.delete(id);
            return new Result(Result.SUCCESS, "删除成功。");
        } else {
            return new Result(Result.ERROR, "对不起，您没有权限！");
        }
    }

    @ApiOperation(value = "获取动态的评论列表（将被废弃）")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Result list(Integer bid) {
        return new Result(Result.SUCCESS, "获取动态的评论列表成功", commentService.findByBid(bid));
    }

    @ApiOperation(value = "获取动态的评论列表（分页）")
    @RequestMapping(value = "listOnPage", method = RequestMethod.GET)
    public Result listOnPage(Integer bid, Integer currPage, Integer pageSize) {
        return new Result(Result.SUCCESS, "获取动态的评论列表成功", commentService.findByBidOnPage(bid, pageSize, currPage));
    }

    @ApiOperation(value = "获取自己发布的评论")
    @RequestMapping(value = "listMyComm", method = RequestMethod.GET)
    public Result list() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }
        return new Result(Result.SUCCESS, "获取个人评论成功", commentService.findByAid(user.getId()));
    }
}
