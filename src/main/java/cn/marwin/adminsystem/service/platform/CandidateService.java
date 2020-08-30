package cn.marwin.adminsystem.service.platform;

import cn.marwin.adminsystem.entity.platform.Candidate;
import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.repository.platform.CandidateDAO;
import cn.marwin.adminsystem.repository.platform.ItemDAO;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {
    @Autowired
    CandidateDAO candidateDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ItemDAO itemDAO;

    public Candidate add(Candidate candidate) throws Exception {
        // 根据状态码确定
        Item item = itemDAO.findById(candidate.getIid()).orElse(null);
        if (item == null) {
            throw new Exception("项目不存在！");
        }
        if (item.getAid() == candidate.getUid()) {
            throw new Exception("无法竞选自己发布的项目！");
        }
        if (item.getState() == 0) {
            throw new Exception("项目已下架，无法竞选！");
        }
        if (item.getState() == 2) {
            throw new Exception("项目已结束竞选！");
        }
        if (existsByIidAndUid(candidate.getIid(), candidate.getUid())) {
            throw new Exception("请勿重复竞选！");
        }
        candidate.setState(0);
        candidate.setCreateTime(TimeUtil.getCurrentTime());
        return candidateDAO.save(candidate);
    }

    public Candidate update(Candidate candidate) {
        return candidateDAO.save(candidate);
    }

    public void delete(Integer id) {
        candidateDAO.deleteById(id);
    }

    public Boolean existsByIidAndUid(Integer iid, Integer uid) {
        return candidateDAO.existsByIidAndUid(iid, uid);
    }

    public Candidate findById(Integer id) {
        return initCandidate(candidateDAO.findById(id).orElse(null));
    }

    public List<Candidate> findByIid(Integer iid) {
        List<Candidate> candidates = candidateDAO.findByIid(iid);
        for (Candidate candidate: candidates) {
            initCandidate(candidate);
        }
        return candidates;
    }

    public List<Candidate> findByUid(Integer uid) {
        List<Candidate> candidates = candidateDAO.findByUid(uid);
        for (Candidate candidate: candidates) {
            initCandidate(candidate);
        }
        return candidates;
    }

    private Candidate initCandidate(Candidate candidate) {
        if (candidate == null) {
            return null;
        }
        User user = userDAO.findById(candidate.getUid()).orElse(null);
        if (user == null) {
            candidate.setUser("用户已注销");
            candidate.setAvatar(""); // 默认头像
        } else {
            candidate.setUser(user.getNickname());
            candidate.setAvatar(user.getAvatar());
        }
        Item item = itemDAO.findById(candidate.getIid()).orElse(null);
        if (item == null) {
            candidate.setItemTitle("项目已下架");
        } else if (item.getState() == 0) {
            candidate.setItemTitle("[已下架]" + item.getTitle());
        } else {
            candidate.setItemTitle(item.getTitle());
        }
        return candidate;
    }
}
