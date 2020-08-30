package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.community.Blog;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.community.BlogService;
import cn.marwin.adminsystem.service.community.PictureService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.util.ViewUtil;
import cn.marwin.adminsystem.entity.community.Picture;
import cn.marwin.adminsystem.facade.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("blog")
public class  BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    PictureService pictureService;

    @ApiOperation(value = "发布动态")
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    public Result publish(@RequestParam(required = false) String[] picUrls, String content) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        Blog blog = new Blog();
        blog.setContent(content);
        blog.setAid(user.getId());
        // 提前上传blog，获取bid
        Blog newBlog = blogService.add(blog);

        if (picUrls == null || picUrls.length == 0) {
            return new Result(Result.SUCCESS, "发布动态成功！", newBlog.getId());
        }

        for (String url : picUrls) {
            Picture picture = new Picture();
            picture.setBid(newBlog.getId());
            picture.setUrl(url);
            pictureService.add(picture);
        }
        return new Result(Result.SUCCESS, "发布动态成功", newBlog.getId());
    }

    @ApiOperation(value = "更新动态")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(Blog blog) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        if (blog.getAid() == user.getId()) {
            blogService.update(blog);
            return new Result(Result.SUCCESS, "修改成功。");
        } else {
            return new Result(Result.ERROR, "对不起，您没有权限！");
        }
    }

    @ApiOperation(value = "删除动态")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(Integer id) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        Blog blog = blogService.findById(id);
        if (blog.getAid() == user.getId()) {
            blogService.delete(id);
            return new Result(Result.SUCCESS, "删除成功！");
        } else {
            return new Result(Result.ERROR, "对不起，您没有权限！");
        }
    }

    @ApiOperation(value = "根据id获取动态信息")
    @RequestMapping(value = "getById", method = RequestMethod.GET)
    public Result getById(Integer id, HttpServletRequest request) {
        Blog blog = blogService.findById(id);
        ViewUtil.logView("blog", id, request);
        return new Result(Result.SUCCESS, "根据id获取动态信息成功", blog);
    }

    @ApiOperation(value = "获取某用户的动态")
    @RequestMapping(value = "listOnes", method = RequestMethod.GET)
    public Result list(Integer aid) {
        List<Blog> blogs = blogService.findByAid(aid);
        return new Result(Result.SUCCESS, "获取用户动态成功", blogs);
    }

    @ApiOperation(value = "获取总数")
    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Result count() {
        return new Result(Result.SUCCESS, "获取动态总数成功！", blogService.count());
    }

    @ApiOperation(value = "根据时间获取")
    @RequestMapping(value = "listByTime", method = RequestMethod.GET)
    public Result listByTime(Integer currPage, Integer pageSize) {
        List<Blog> blogs = blogService.findOnPage(pageSize, currPage);
        return new Result(Result.SUCCESS, "根据时间获取动态成功", blogs);
    }


    @ApiOperation(value = "获取自己的动态列表")
    @RequestMapping(value = "listMyBlogs", method = RequestMethod.GET)
    public Result listMy() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登录！");
        }
        return new Result(Result.SUCCESS, "获取个人的动态列表成功", blogService.findByAid(user.getId()));
    }
}
