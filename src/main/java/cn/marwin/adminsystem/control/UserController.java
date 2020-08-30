package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.security.UserService;
import cn.marwin.adminsystem.util.FileUtil;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.UserFacade;
import cn.marwin.adminsystem.facade.Result;
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
    public Result register(User user) {
        try {
            userService.add(user);
        } catch (Exception e) {
            return new Result(Result.ERROR, e.getMessage());
        }
        return new Result(Result.SUCCESS, "注册成功！");
    }

    @ApiOperation(value = "修改基本信息")
    @RequestMapping(value = "updateInfo", method = RequestMethod.POST)
    public Result updateInfo(String nickname, boolean gender, String college, String major, String phone, String email) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登录！");
        }
        if (!user.getNickname().equals(nickname)) {
            if (userService.existsByNickname(nickname)) {
                return new Result(Result.ERROR, "对不起，该昵称已被使用了！");
            }
            user.setNickname(nickname);
        }
        user.setGender(gender);
        user.setCollege(college);
        user.setMajor(major);
        user.setPhone(phone);
        user.setEmail(email);
        userService.update(user);
        return new Result(Result.SUCCESS, "修改成功！");
    }

    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "updatePwd", method = RequestMethod.POST)
    public Result updatePwd(String oldPwd, String newPwd) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        if (!user.getPassword().equals(oldPwd)) {
            return new Result(Result.ERROR, "输入的密码错误！");
        }

        user.setPassword(newPwd);
        try {
            userService.update(user);
        } catch (Exception e) {
            return new Result(Result.ERROR, e.getMessage());
        }
        return new Result(Result.SUCCESS, "修改成功！");
    }

    @ApiOperation(value = "修改头像")
    @RequestMapping(value = "updateAvatar", method = RequestMethod.POST)
    public Result updateAvatar(MultipartFile file) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }

        // 验证文件格式
        if (!FileUtil.checkPicType(file)) {
            return new Result(Result.ERROR, "图片格式错误。");
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
            return new Result(Result.SUCCESS, "上传头像成功。");
        } catch (IOException e) {
            return new Result(Result.ERROR, "上传图片错误。");
        } catch (Exception e) {
            return new Result(Result.ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "getLoginUser", method = RequestMethod.GET)
    public Result loginUser() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new Result(Result.ERROR, "请先登陆！");
        }
        // 封装成门面类
        return new Result(Result.SUCCESS, "获取当前登录用户信息成功", new UserFacade(user));
    }

    @ApiOperation(value = "根据id查询用户信息")
    @RequestMapping(value = "userInfo", method = RequestMethod.GET)
    public Result userInfo(Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return new Result(Result.ERROR, "该用户不存在！");
        } else {
            // 封装成门面类
            return new Result(Result.SUCCESS, "根据id查询用户信息成功", new UserFacade(user));
        }
    }

    @ApiOperation(value = "通过账号搜索用户")
    @RequestMapping(value = "searchByUsername", method = RequestMethod.GET)
    public Result searchByUsername(String keyword) {
        return new Result(Result.SUCCESS, "搜索成功！", userService.searchByUsername(keyword));
    }

    @ApiOperation(value = "通过昵称搜索用户")
    @RequestMapping(value = "searchByNickname", method = RequestMethod.GET)
    public Result searchByNickname(String keyword) {
        return new Result(Result.SUCCESS, "搜索成功！", userService.searchByNickname(keyword));
    }

    @ApiOperation(value = "确认验证码")
    @RequestMapping(value = "checkCode", method = RequestMethod.GET)
    public Result checkCode(String verifyCode, HttpSession session) {
        String code = ((String) session.getAttribute("validateCode")).toLowerCase();
        if (verifyCode.toLowerCase().equals(code)) {
            return new Result(Result.SUCCESS, "验证码正确");
        } else {
            return new Result(Result.ERROR, "验证码错误");
        }
    }
}
