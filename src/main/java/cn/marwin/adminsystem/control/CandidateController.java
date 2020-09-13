package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.platform.Candidate;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.platform.CandidateService;
import cn.marwin.adminsystem.service.platform.ItemService;
import cn.marwin.adminsystem.util.UserUtil;
import cn.marwin.adminsystem.facade.HttpResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("candidate")
public class CandidateController {
    @Autowired
    CandidateService candidateService;
    @Autowired
    ItemService itemService;

    @ApiOperation(value = "参与竞选")
    @RequestMapping(value = "join", method = RequestMethod.POST)
    public HttpResult join(Integer iid) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }

        try {
            Candidate candidate = new Candidate();
            candidate.setIid(iid);
            candidate.setUid(user.getId());
            candidateService.add(candidate);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "报名成功！");
    }

    @ApiOperation(value = "退出竞选")
    @RequestMapping(value = "exit", method = RequestMethod.POST)
    public HttpResult exit(Integer id) {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        Candidate candidate = candidateService.findById(id);
        if (candidate == null) {
            return new HttpResult(HttpResult.SUCCESS, "退出成功！");
        }
        if (candidate.getUid() != user.getId()) {
            return new HttpResult(HttpResult.ERROR, "您没有权限！");
        }
        candidateService.delete(id);
        return new HttpResult(HttpResult.SUCCESS, "退出成功！");
    }

    @ApiOperation(value = "获取项目的竞选列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResult list(Integer iid) {
        return new HttpResult(HttpResult.SUCCESS, "获取项目的竞选列表成功", candidateService.findByIid(iid));
    }

    @ApiOperation(value="列出我竞选的项目列表")
    @RequestMapping(value = "listMyJoin", method = RequestMethod.GET)
    public HttpResult list() {
        User user = UserUtil.getLoginUser();
        if (user == null) {
            return new HttpResult(HttpResult.ERROR, "请先登录！");
        }
        return new HttpResult(HttpResult.SUCCESS, "个人竞选的项目列表获取成功", candidateService.findByUid(user.getId()));
    }
}
