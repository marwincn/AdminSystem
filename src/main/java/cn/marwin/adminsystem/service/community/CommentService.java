package cn.marwin.adminsystem.service.community;

import cn.marwin.adminsystem.entity.community.Comment;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.community.CommentDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    UserDAO userDAO;

    public Comment add(Comment comment) {
        comment.setCreateTime(TimeUtil.getCurrentTime());
        return commentDAO.save(comment);
    }

    public void delete(Integer id) {
        Comment comment = commentDAO.findById(id).orElse(null);
        if (comment != null) {
            comment.setAid(0);
            comment.setContent("该评论已经被删除");
            commentDAO.save(comment);
        }
    }

    public Comment findById(Integer id) {
        Comment comment = commentDAO.findById(id).orElse(null);
        return initComment(comment);
    }

    public List<Comment> findByBid(Integer bid) {
        List<Comment> comments = commentDAO.findByBid(bid);
        for (Comment comment: comments) {
            initComment(comment);
        }
        return comments;
    }

    public List<Comment> findByBidOnPage(Integer bid, Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<Comment> comments = commentDAO.findOnPage(bid, pageSize, offset);
        for (Comment comment: comments) {
            initComment(comment);
        }
        return comments;
    }

    public List<Comment> findByAid(Integer aid) {
        return commentDAO.findByAid(aid);
    }

    private Comment initComment(Comment comment) {
        if (comment == null) {
            return null;
        }
        User author = userDAO.findById(comment.getAid()).orElse(null);
        if (author != null) {
            comment.setAuthor(author.getNickname());
        } else {
            comment.setAuthor("注销用户");
        }
        if (comment.getRid() != null) {
            Comment replyComm = commentDAO.findById(comment.getRid()).orElse(null);
            if (replyComm != null) {
                User replyUser = userDAO.findById(replyComm.getAid()).orElse(null);
                if (replyUser != null) {
                    replyComm.setAuthor(replyUser.getNickname());
                } else {
                    replyComm.setAuthor("注销用户");
                }
                comment.setReply(replyComm.getAuthor() + "：" + replyComm.getContent());
            }
        }
        return comment;
    }
}
