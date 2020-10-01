<template>
  <div id="main">
    <el-container>
      <el-header>
        <el-menu
          default-active="/"
          mode="horizontal"
          background-color="#202020"
          text-color="#fff"
          active-text-color="#ffd04b"
          router
        >
          <el-menu-item index="/">管理中心</el-menu-item>
          <el-submenu index="0000" style="float:right; margin-right: 20px;">
            <template slot="title">
              <div style=" overflow: hidden; display: inline-block">
                <img :src="sysUserAvatar" style="height: 30px; width: 30px; border-radius: 50%;">
              </div>
              {{sysUserName}}
            </template>
            <el-menu-item index="update" @click="update()">修改信息</el-menu-item>
            <el-menu-item index="logout" @click="logout()">退出</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-header>
    </el-container>
    <el-container>
      <el-aside style="width: 230px;">
        <el-menu
          background-color="#fff"
          text-color="#606266"
          active-text-color="#20a0ff"
          unique-opened
          router
        >
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-user"></i>用户管理
            </template>
            <el-menu-item index="user">用户列表</el-menu-item>
            <el-menu-item index="role">角色列表</el-menu-item>
            <el-menu-item index="perm">权限列表</el-menu-item>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-edit-outline"></i>动态管理
            </template>
            <el-menu-item index="blog">动态列表</el-menu-item> 
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-tickets"></i>项目管理
            </template>
            <el-menu-item index="item">项目列表</el-menu-item>
            <el-menu-item index="catalog">分类列表</el-menu-item>
          </el-submenu>
          <el-submenu index="4">
            <template slot="title">
              <i class="el-icon-message"></i>订单管理
            </template>
            <el-menu-item index="order">项目列表</el-menu-item>
          </el-submenu>
          <el-submenu index="5">
            <template slot="title">
              <i class="el-icon-document"></i>文件管理
            </template>
            <el-menu-item index="file">文件列表</el-menu-item>
            <el-menu-item index="picture">配图列表</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <el-col :span="24" class="breadcrumb-container">
          <el-breadcrumb
            separator-class="el-icon-arrow-right"
            style="float: left; margin-bottom: 10px;"
          >
            <el-breadcrumb-item
              v-for="item in $route.matched"
              :key="item.path"
              :to="{path: item.path}"
            >{{item.name}}</el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
        <el-col>
          <!--主视图-->
          <router-view></router-view>
        </el-col>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  created() {
    var _this = this;
    this.axios
      .get("/user/getLoginUser")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.sysUserName = data.nickname;
          _this.sysUserAvatar = data.avatar;
        } else {
          _this.sysUserName = "请重新登录";
        }
      })
      .catch(function(error) {
        _this.sysUserName = "请重新登录";
      });
  },
  data() {
    return {
      sysUserAvatar: "",
      sysUserName: ""
    };
  },
  methods: {
    update() {

    },
    logout() {
      var _this = this;
      this.axios
      .get("/user/logout")
      .then(function(response) {
        _this.$router.push({ path: "/login" });
      })
      .catch(function(error) {
        _this.$router.push({ path: "/login" });
      });
    }
  }
};
</script>
<style scoped>
</style>


