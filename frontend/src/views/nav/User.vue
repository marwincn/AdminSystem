<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label="">
          <el-input v-model="searchWord" placeholder="根据id，账号或昵称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="searchUser()">搜索用户</el-button>
        </el-form-item>
      </el-form>
      <router-view></router-view>
    </el-card>
    <el-card style="margin-top: 10px">
      <el-table :data="userList" style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="性别">
                <span>{{ props.row.gender===true? '男':'女' }}</span>
              </el-form-item>
              <el-form-item label="学校">
                <span>{{ props.row.college }}</span>
              </el-form-item>
              <el-form-item label="专业">
                <span>{{ props.row.major }}</span>
              </el-form-item>
              <el-form-item label="手机">
                <span>{{ props.row.phone }}</span>
              </el-form-item>
              <el-form-item label="邮箱">
                <span>{{ props.row.email }}</span>
              </el-form-item>
              <el-form-item label="上次修改">
                <span>{{ props.row.modifyTime }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="序号" width="100"></el-table-column>
        <el-table-column label="头像">
          <template slot-scope="scope">
            <div>
              <img :src="scope.row.avatar" style="height: 40px; width: 40px; border-radius: 50%;">
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column prop="state" label="状态"></el-table-column>
        <el-table-column label="角色">
          <template slot-scope="scope">
            <el-tag v-for="role in scope.row.roles" :key="role">{{roleName(role)}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDialog(scope.$index, scope.row)">编辑</el-button>
            <el-dialog title="用户信息" :visible.sync="dialogFormVisible">
              <el-form label-position="right" label-width="80px" :model="userForm" size="small">
                <el-form-item label="序号">
                  <el-input v-model="userForm.id" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="账号">
                  <el-input v-model="userForm.username" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="昵称">
                  <el-input v-model="userForm.nickname"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="userForm.gender">
                    <el-radio :label="true">男</el-radio>
                    <el-radio :label="false">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="学校">
                  <el-input v-model="userForm.college"></el-input>
                </el-form-item>
                <el-form-item label="专业">
                  <el-input v-model="userForm.major"></el-input>
                </el-form-item>
                <el-form-item label="手机">
                  <el-input v-model="userForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="userForm.email"></el-input>
                </el-form-item>
                <el-form-item label="角色">
                  <el-checkbox-group v-model="userForm.roles" size="small">
                    <el-checkbox-button
                      v-for="roleOption in roleList"
                      :label="roleOption.id"
                      :key="roleOption.id"
                    >{{roleOption.name}}</el-checkbox-button>
                  </el-checkbox-group>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateUser()">提 交</el-button>
              </div>
            </el-dialog>
            <el-popover placement="top" trigger="manual" :ref="'popover-' + scope.row.id">
              <p>你确定要注销该用户吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="close(scope.row.id)">取消</el-button>
                <el-button type="primary" size="mini" @click="banUser(scope.$index, scope.row)">确定</el-button>
              </div>
              <el-button type="danger" size="mini" slot="reference" @click="open(scope.row.id)">注销</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
      <div class="block" style="float:right; margin-right: 50px; margin-top: 20px; padding-bottom: 20px">
        <el-pagination
          @current-change="handleCurrChange"
          :current-page.sync="currPage"
          :page-size="pageSize"
          :total="total"
          layout="prev, pager, next, jumper"
        ></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchWord: "",
      total: 0,
      currPage: 1,
      pageSize: 20,
      dialogFormVisible: false, // 编辑框
      index: 0, // 编辑的用户在table中的index，动态修改userList时使用
      roleList: [],
      userList: [],
      userForm: {
        // 编辑框的表单
        id: "",
        username: "",
        nickname: "",
        gender: true,
        college: "",
        major: "",
        phone: "",
        email: "",
        roles: []
      }
    };
  },
  methods: {
    // 打开确认框
    open(id) {
      this.$refs[`popover-` + id].doShow();
    },
    // 关闭确认框
    close(id) {
      this.$refs[`popover-` + id].doClose();
    },
    // 通过rid解析role的name
    roleName(role) {
      for (var i in this.roleList) {
        if (role === this.roleList[i].id) {
          return this.roleList[i].name;
        }
      }
      return "";
    },
    showDialog(index, row) {
      for (let i in this.userForm) {
        // 将数据复制到输入框
        this.userForm[i] = row[i];
      }
      this.index = index;
      this.dialogFormVisible = true;
    },
    handleCurrChange(val) {
      var _this = this;
      this.axios
        .get(
          "/config/getUserList?pageSize=" +
            _this.pageSize +
            "&currPage=" +
            val
        )
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.userList = data;
          } else {
            _this.error("获取用户列表失败！");
          }
        })
        .catch(function(error) {
          _this.error("获取用户列表失败！");
        });
    },
    updateUser() {
      var _this = this;
      this.axios
        .post("/config/updateUser", _this.userForm)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.userList.splice(_this.index, 1, data);
            _this.dialogFormVisible = false;
            _this.success(message);
          } else {
            _this.dialogFormVisible = false;
            _this.error(message);
          }
        })
        .catch(function(error) {
          _this.dialogFormVisible = false;
          _this.error("ERROR!");
        });
    },
    banUser(index, row) {
      var _this = this;
      this.axios
        .post("/config/banUser", { id: row.id })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.$refs[`popover-` + row.id].doClose();
            _this.success(message);
          } else {
            _this.$refs[`popover-` + row.id].doClose();
            _this.error(message);
          }
        })
        .catch(function(error) {
          _this.$refs[`popover-` + row.id].doClose();
          _this.error("ERROR!");
        });
    },
    searchUser() {
      this.$router.push({ path: "searchUser" });
    },
    success(message) {
      this.$message({
        showClose: true,
        message: message,
        type: "success"
      });
    },
    error(message) {
      this.$message({
        showClose: true,
        message: message,
        type: "error"
      });
    }
  },
  created: function() {
    var _this = this;
    this.axios
      .get("/config/getRoleList")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.roleList = data;
        } else {
          _this.error("获取角色信息失败！");
        }
      })
      .catch(function(error) {
        _this.error("获取角色信息失败！");
      });
    this.axios
      .get("/config/getUserCount")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.total = data;
        } else {
          _this.error("获取用户总数失败！");
        }
      })
      .catch(function(error) {
        _this.error("获取用户总数失败！");
      });
    this.axios
      .get(
        "/config/getUserList?pageSize=" +
          _this.pageSize +
          "&currPage=" +
          1
      )
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.userList = data;
        } else {
          _this.error("获取用户列表失败！");
        }
      })
      .catch(function(error) {
        _this.error("获取用户列表失败！");
      });
  }
};
</script>
<style>
.table-expand {
  font-size: 0;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
.el-tag {
  margin-right: 5px;
}
.table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>