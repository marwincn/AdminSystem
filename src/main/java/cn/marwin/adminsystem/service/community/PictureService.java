package cn.marwin.adminsystem.service.community;

import cn.marwin.adminsystem.repository.community.PictureDAO;
import cn.marwin.adminsystem.util.FileUtil;
import cn.marwin.adminsystem.entity.community.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureService {
    @Autowired
    PictureDAO pictureDAO;

    public Picture add(Picture picture) {
        return pictureDAO.save(picture);
    }

    public void delete(Integer id) {
        Picture picture = pictureDAO.findById(id).orElse(null);
        // 删除文件
        if (picture != null) {
            FileUtil.deleteFile(picture.getUrl());
            pictureDAO.deleteById(id);
        }
    }

    public List<Picture> findOnPage(Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        return pictureDAO.findOnPage(pageSize, offset);
    }

    public Long count() {
        return pictureDAO.count();
    }

    public List<Picture> findByBid(Integer bid) {
        return pictureDAO.findByBid(bid);
    }

    public Picture findById(Integer id) {
        return pictureDAO.findById(id).orElse(null);
    }
}
