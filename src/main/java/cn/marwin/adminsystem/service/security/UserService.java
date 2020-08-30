package cn.marwin.adminsystem.service.security;

import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.entity.security.UserRole;
import cn.marwin.adminsystem.repository.security.UserDAO;
import cn.marwin.adminsystem.repository.security.UserRoleDAO;
import cn.marwin.adminsystem.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserRoleDAO userRoleDAO;

    public User add(User user) throws Exception {
        if (!checkUser(user)) {
            throw new Exception("输入参数不合理！");
        }
        if (userDAO.existsByUsername(user.getUsername())) {
            throw new Exception("该账号已经被注册！");
        }
        if (userDAO.existsByNickname(user.getNickname())) {
            throw new Exception("该昵称已经被注册！");
        }
        user.setGender(true);
        user.setState(1);
        user.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556811797335&di=0909581a423d1aab8117655d49e764d4&imgtype=0&src=http%3A%2F%2Fimg0.pconline.com.cn%2Fpconline%2F1503%2F12%2F6209157_09_thumb.jpg");
        user.setCreateTime(TimeUtil.getCurrentTime());
        return userDAO.save(user);
    }

    public User update(User user) {
        user.setModifyTime(TimeUtil.getCurrentTime());
        return userDAO.save(user);
    }

    /*
     * user：被更新的参数，它的roles[]是旧的
     * roles：新的roles[]
     */
    public void updateRoles(User user, List<Integer> roles) {
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (!roles.equals(user.getRoles())) {
            for (Integer rid: user.getRoles()) {
                if (!roles.contains(rid)) {
                    userRoleDAO.deleteByUidAndRid(user.getId(), rid);
                }
            }
            for (Integer rid: roles) {
                if (!user.getRoles().contains(rid)) {
                    UserRole userRole = new UserRole();
                    userRole.setUid(user.getId());
                    userRole.setRid(rid);
                    userRoleDAO.save(userRole);
                }
            }
        }
    }

    public boolean existsByNickname(String nickname) {
        return userDAO.existsByNickname(nickname);
    }

    public User findById(Integer id) {
        return initRoles(userDAO.findById(id).orElse(null));
    }

    public List<User> searchByUsername(String username) {
        return userDAO.findByUsernameLike("%" + username + "%");
    }

    public User findByUsername(String username) {
        return initRoles(userDAO.findByUsername(username).orElse(null));
    }

    public List<User> searchByNickname(String nickname) {
        return userDAO.findByNicknameLike("%" + nickname + "%");
    }

    public User findByNickname(String nickname) {
        return initRoles(userDAO.findByNickname(nickname).orElse(null));
    }

    public List<User> findOnPage(Integer pageSize, Integer currPage) {
        Integer offset = (currPage - 1) * pageSize;
        if (offset < 0 || pageSize < 0) {
            return null;
        }
        List<User> users = userDAO.findOnPage(pageSize, offset);
        for (User user: users) {
            initRoles(user);
        }
        return users;
    }

    public Long count() {
        return userDAO.count();
    }

    private User initRoles(User user) {
        if (user == null) {
            return null;
        }
        List<Integer> roles = new ArrayList<>();
        List<UserRole> userRoles = userRoleDAO.findByUid(user.getId());
        for (UserRole userRole: userRoles) {
            roles.add(userRole.getRid());
        }
        user.setRoles(roles);
        return user;
    }

    // 数据合法性校验
    private Boolean checkUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().equals("") || user.getUsername().length() < 8) {
            return false;
        }
        if (user.getPassword() == null || user.getPassword().trim().equals("") || user.getPassword().length() < 8) {
            return false;
        }
        if (user.getNickname() == null || user.getNickname().trim().equals("")) {
            return false;
        }
        return true;
    }
}
