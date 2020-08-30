package cn.marwin.adminsystem.service.community;

import cn.marwin.adminsystem.entity.community.Blog;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.community.BlogDAO;
import cn.marwin.adminsystem.repository.community.CommentDAO;
import cn.marwin.adminsystem.repository.community.FavourDAO;
import cn.marwin.adminsystem.repository.community.PictureDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import cn.marwin.adminsystem.util.ViewUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogDAO blogDAO;
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    FavourDAO favourDAO;
    @Autowired
    PictureDAO pictureDAO;
    @Autowired
    UserDAO userDAO;

    public Blog add(Blog blog) {
        blog.setPublishTime(TimeUtil.getCurrentTime());
        return blogDAO.save(blog);
    }

    public void delete(Integer id) {
        blogDAO.deleteById(id);
        commentDAO.deleteByBid(id);
        favourDAO.deleteByBid(id);
        pictureDAO.deleteByBid(id);
    }

    public Blog update(Blog blog) {
        blog.setModifyTime(TimeUtil.getCurrentTime());
        return blogDAO.save(blog);
    }

    public Blog findById(Integer id) {
        Blog blog = blogDAO.findById(id).orElse(null);
        return initBlog(blog);
    }

    public List<Blog> findByAid(Integer aid) {
        List<Blog> blogs =  blogDAO.findByAid(aid);
        for (Blog blog: blogs) {
            initBlog(blog);
        }
        return blogs;
    }

    public List<Blog> findOnPage(Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<Blog> blogs = blogDAO.findOnPage(pageSize, offset);
        for (Blog blog: blogs) {
            initBlog(blog);
        }
        return blogs;
    }

    public Long count() {
        return blogDAO.count();
    }

    private Blog initBlog(Blog blog) {
        if (blog == null) {
            return null;
        }
        // 级联查询作者信息
        User user = userDAO.findById(blog.getAid()).orElse(null);
        if (user != null) {
            blog.setAuthor(user.getNickname());
            blog.setAvatar(user.getAvatar());
        } else {
            blog.setAuthor("用户已注销");
            blog.setAvatar("");
        }
        // 级联查询图片
        blog.setPictures(pictureDAO.findByBid(blog.getId()));
        // 级联查询点赞数
        blog.setFavourCount(favourDAO.countByBid(blog.getId()));
        // 级联查询评论数
        blog.setCommentCount(commentDAO.countByBid(blog.getId()));
        // 级联查询浏览数
        blog.setViewCount(ViewUtil.getViewCount("blog", blog.getId()));
        return blog;
    }

}
