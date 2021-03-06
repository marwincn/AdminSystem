package cn.marwin.adminsystem.control;

import cn.marwin.adminsystem.entity.platform.Catalog;
import cn.marwin.adminsystem.entity.platform.Item;
import cn.marwin.adminsystem.entity.platform.Order;
import cn.marwin.adminsystem.entity.security.Permission;
import cn.marwin.adminsystem.entity.security.Role;
import cn.marwin.adminsystem.entity.security.User;
import cn.marwin.adminsystem.service.community.PictureService;
import cn.marwin.adminsystem.service.platform.CatalogService;
import cn.marwin.adminsystem.service.platform.ItemService;
import cn.marwin.adminsystem.service.platform.OrderService;
import cn.marwin.adminsystem.service.security.PermissionService;
import cn.marwin.adminsystem.service.security.RoleService;
import cn.marwin.adminsystem.service.security.UserService;
import cn.marwin.adminsystem.util.FileUtil;
import cn.marwin.adminsystem.facade.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("config")
public class ConfigController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    ItemService itemService;
    @Autowired
    OrderService orderService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    PictureService pictureService;
    @Autowired
    CatalogService catalogService;

    /****************************************************************
     *
     * 用户管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    public HttpResult getUserList(Integer pageSize, Integer currPage) {
        return new HttpResult(HttpResult.SUCCESS, "用户列表获取成功", userService.findOnPage(pageSize, currPage));
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public HttpResult updateUser(User user) {
        User oldUser = userService.findById(user.getId());
        if (oldUser == null) {
            return new HttpResult(HttpResult.ERROR, "用户不存在！");
        }
        oldUser.setNickname(user.getNickname());
        oldUser.setGender(user.isGender());
        oldUser.setCollege(user.getCollege());
        oldUser.setMajor(user.getMajor());
        oldUser.setPhone(user.getPhone());
        oldUser.setEmail(user.getEmail());
        try {
            userService.update(oldUser);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        userService.updateRoles(oldUser, user.getRoles());
        return new HttpResult(HttpResult.SUCCESS, "修改用户信息成功！", userService.findById(user.getId()));
    }

    @RequestMapping(value = "banUser", method = RequestMethod.POST)
    public HttpResult banUser(Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return new HttpResult(HttpResult.SUCCESS, "注销用户成功！");
        }
        user.setState(0);
        userService.update(user);
        return new HttpResult(HttpResult.SUCCESS, "注销用户成功！");
    }

    @RequestMapping(value = "getUserCount", method = RequestMethod.GET)
    public HttpResult getUserCount() {
        return new HttpResult(HttpResult.SUCCESS, "获取用户数量成功", userService.count());
    }

    /****************************************************************
     *
     * 角色管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getRoleList", method = RequestMethod.GET)
    public HttpResult getRoleList() {
        return new HttpResult(HttpResult.SUCCESS, "获取角色列表成功", roleService.findAll());
    }

    @RequestMapping(value = "updateRole", method = RequestMethod.POST)
    public HttpResult updateRole(Role role) {
        Role oldRole = roleService.findById(role.getId());
        if (oldRole == null) {
            return new HttpResult(HttpResult.ERROR, "角色不存在！");
        }
        oldRole.setName(role.getName());
        roleService.update(oldRole);
        roleService.updatePerms(oldRole, role.getPerms());
        return new HttpResult(HttpResult.SUCCESS, "修改角色成功！", roleService.findById(role.getId()));
    }

    @RequestMapping(value = "deleteRole", method = RequestMethod.POST)
    public HttpResult deleteRole(Integer id) {
        try {
            roleService.delete(id);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "删除角色成功！");
    }

    @RequestMapping(value = "addRole", method = RequestMethod.POST)
    public HttpResult addRole(String name) {
        Role role = new Role();
        role.setName(name);
        role = roleService.add(role);
        return new HttpResult(HttpResult.SUCCESS, "添加角色成功！", role);
    }

    /****************************************************************
     *
     * 权限管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getPermList", method = RequestMethod.GET)
    public HttpResult getPermList() {
        return new HttpResult(HttpResult.SUCCESS, "获取权限列表成功", permissionService.findAll());
    }

    @RequestMapping(value = "updatePerm", method = RequestMethod.POST)
    public HttpResult updatePerm(Permission perm) {
        permissionService.update(perm);
        return new HttpResult(HttpResult.SUCCESS, "更新权限成功！", permissionService.findById(perm.getId()));
    }

    @RequestMapping(value = "deletePerm", method = RequestMethod.POST)
    public HttpResult deletePerm(Integer id) {
        try {
            permissionService.delete(id);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "删除权限成功！");
    }

    @RequestMapping(value = "addPerm", method = RequestMethod.POST)
    public HttpResult addPerm(String name, String url, String description) {
        Permission perm = new Permission();
        perm.setName(name);
        perm.setUrl(url);
        perm.setDescription(description);
        perm = permissionService.add(perm);
        return new HttpResult(HttpResult.SUCCESS, "添加权限成功！", perm);
    }

    /****************************************************************
     *
     * 分类管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getCataList", method = RequestMethod.GET)
    public HttpResult getCataList() {
        return new HttpResult(HttpResult.SUCCESS, "获取分类列表成功", catalogService.findAll());
    }

    @RequestMapping(value = "updateCata", method = RequestMethod.POST)
    public HttpResult updateCata(Catalog catalog) {
        catalogService.update(catalog);
        return new HttpResult(HttpResult.SUCCESS, "更新分类成功！", catalogService.findById(catalog.getId()));
    }

    @RequestMapping(value = "deleteCata", method = RequestMethod.POST)
    public HttpResult deleteCata(Integer id) {
        try {
            catalogService.delete(id);
        } catch (Exception e) {
            return new HttpResult(HttpResult.ERROR, e.getMessage());
        }
        return new HttpResult(HttpResult.SUCCESS, "删除分类成功！");
    }

    @RequestMapping(value = "addCata", method = RequestMethod.POST)
    public HttpResult addCata(String name) {
        Catalog catalog = new Catalog();
        catalog.setName(name);
        catalog = catalogService.add(catalog);
        return new HttpResult(HttpResult.SUCCESS, "添加分类成功", catalog);
    }

    /****************************************************************
     *
     * 项目管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getItemList", method = RequestMethod.GET)
    public HttpResult getItemList(Integer pageSize, Integer currPage) {
        return new HttpResult(HttpResult.SUCCESS, "获取项目列表成功", itemService.findOnPageC(pageSize, currPage));
    }

    @RequestMapping(value = "getItemCount", method = RequestMethod.GET)
    public HttpResult getItemCount() {
        return new HttpResult(HttpResult.SUCCESS, "获取项目总数成功",itemService.countC());
    }

    @RequestMapping(value = "updateItem", method = RequestMethod.POST)
    public HttpResult updateItem(Item item) {
        Item newItem = itemService.findByIdC(item.getId());
        newItem.setCid(item.getCid());
        newItem.setState(item.getState());
        newItem.setPrice(item.getPrice());
        newItem.setTitle(item.getTitle());
        newItem.setDescription(item.getDescription());
        itemService.update(newItem);
        return new HttpResult(HttpResult.SUCCESS, "更新分类成功！", itemService.findById(item.getId()));
    }

    /****************************************************************
     *
     * 订单管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getOrderList", method = RequestMethod.GET)
    public HttpResult getOrderList(Integer pageSize, Integer currPage) {
        return new HttpResult(HttpResult.SUCCESS, "获取订单列表成功", orderService.findOnPage(pageSize, currPage));
    }

    @RequestMapping(value = "getOrderCount", method = RequestMethod.GET)
    public HttpResult getOrderCount() {
        return new HttpResult(HttpResult.SUCCESS, "获取订单数量成功", orderService.count());
    }

    @RequestMapping(value = "updateOrder", method = RequestMethod.POST)
    public HttpResult updateOrder(Order order) {
        Order newOrder = orderService.findById(order.getId());
        newOrder.setState(order.getState());
        newOrder.setPrice(order.getPrice());
        orderService.update(newOrder);
        return new HttpResult(HttpResult.SUCCESS, "更新分类成功！", orderService.findById(order.getId()));
    }

    /****************************************************************
     *
     * 文件管理接口
     *
     ***************************************************************/
    @RequestMapping(value = "getFileList", method = RequestMethod.GET)
    public HttpResult getFileList(Integer currPage, Integer pageSize) {
        return new HttpResult(HttpResult.SUCCESS, "获取文件列表成功！", FileUtil.getFileList(currPage, pageSize));
    }

    @RequestMapping(value = "getFileCount", method = RequestMethod.GET)
    public HttpResult getFileCount() {
        return new HttpResult(HttpResult.SUCCESS, "获取文件总数成功！", FileUtil.getFileCount());
    }

    @RequestMapping(value = "deleteFile", method = RequestMethod.POST)
    public HttpResult delFile(String url) {
        url = FileUtil.context + url;
        FileUtil.deleteFile(url);
        return new HttpResult(HttpResult.SUCCESS, "删除文件成功！");
    }

    @RequestMapping(value = "compPicture", method = RequestMethod.POST)
    public HttpResult compPicture(String url, Double scale) {
        if (scale == null || scale <= 0 || scale >= 1) {
            return new HttpResult(HttpResult.SUCCESS, "请输入正确的压缩率");
        }
        try {
            FileUtil.compPic(url, scale);
        } catch (IOException e) {
            return new HttpResult(HttpResult.SUCCESS, "压缩图片失败");
        }
        return new HttpResult(HttpResult.SUCCESS, "压缩图片成功！");
    }
}
