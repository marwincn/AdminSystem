package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.security.UserService;
import cn.marwin.adminsystem.util.FileUtil;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.UserFacade;
import cn.marwin.adminsystem.facade.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "注册账号")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public HttpResult register(User user) {
        try {
            userService.add(user);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "注册成功！");
    }

    @ApiOperation(value = "修改基本信息")
    @RequestMapping(value = "updateInfo", method = RequestMethod.POST)
    public HttpResult updateInfo(String nickname, boolean gender, String college, String major, String phone, String email) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        if (!user.getNickname().equals(nickname)) {
            if (userService.existsByNickname(nickname)) {
                return new HttpResult(HttpResult.ERROR, "对不起，该昵称已被使用了！");
            }
            user.setNickname(nickname);
        }
        user.setGender(gender);
        user.setCollege(college);
        user.setMajor(major);
        user.setPhone(phone);
        user.setEmail(email);
        userService.update(user);
        return new HttpResult(HttpResult.SUCCESS, "修改成功！");
    }

    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public HttpResult updatePwd(String oldPwd, String newPwd) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登陆！");
        }

        if (!user.getPassword().equals(oldPwd)) {
            return new HttpResult(HttpResult.ERROR, "输入的密码错误！");
        }

        user.setPassword(newPwd);
        try {
            userService.update(user);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "修改成功！");
    }

    @ApiOperation(value = "修改头像")
    @RequestMapping(value = "updateAvatar", method = RequestMethod.POST)
    public HttpResult updateAvatar(MultipartFile file) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登陆！");
        }

        // 验证文件格式
        if (!FileUtil.checkPicType(file)) {
            return new HttpResult(HttpResult.ERROR, "图片格式错误。");
        }
        try {
            String oldUrl = user.getAvatar();
            // 上传头像图片，获取图片相对路径
            // 包含了压缩文件操作
            String url = FileUtil.uploadAvatar(file);
            user.setAvatar(url);
            userService.update(user);
            // 删除旧文件
            FileUtil.deleteFile(oldUrl);
            return new HttpResult(HttpResult.SUCCESS, "上传头像成功。");
        } catch (IOException e) {
            return new HttpResult(HttpResult.ERROR, "上传图片错误。");
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "getLoginUser", method = RequestMethod.GET)
    public HttpResult loginUser() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登陆！");
        }
        // 封装成门面类
        return new HttpResult(HttpResult.SUCCESS, "获取当前登录用户信息成功", new UserFacade(user));
    }

    @ApiOperation(value = "根据id查询用户信息")
    @RequestMapping(value = "userInfo", method = RequestMethod.GET)
    public HttpResult userInfo(Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "该用户不存在！");
        } else {
            // 封装成门面类
            return new HttpResult(HttpResult.SUCCESS, "根据id查询用户信息成功", new UserFacade(user));
        }
    }

    @ApiOperation(value = "通过账号搜索用户")
    @RequestMapping(value = "searchByUsername", method = RequestMethod.GET)
    public HttpResult searchByUsername(String keyword) {
        return new HttpResult(HttpResult.SUCCESS, "搜索成功！", userService.searchByUsername(keyword));
    }

    @ApiOperation(value = "通过昵称搜索用户")
    @RequestMapping(value = "searchByNickname", method = RequestMethod.GET)
    public HttpResult searchByNickname(String keyword) {
        return new HttpResult(HttpResult.SUCCESS, "搜索成功！", userService.searchByNickname(keyword));
    }

    @ApiOperation(value = "确认验证码")
    @RequestMapping(value = "checkCode", method = RequestMethod.GET)
    public HttpResult checkCode(String verifyCode, HttpSession session) {
        String code = ((String) session.getAttribute("validateCode")).toLowerCase();
        if (verifyCode.toLowerCase().equals(code)) {
            return new HttpResult(HttpResult.SUCCESS, "验证码正确");
        } else {
            return new HttpResult(HttpResult.ERROR, "验证码错误");
        }
    }
}
