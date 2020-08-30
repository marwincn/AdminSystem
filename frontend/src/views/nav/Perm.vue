<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label="名称">
          <el-input v-model="permForm.name"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="permForm.url"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="permForm.description"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="addPerm()">添加权限</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 10px">
      <el-table :data="permList" style="width: 100%">
        <el-table-column prop="id" label="序号" width="100"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="url" label="路径"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDialog(scope.$index, scope.row)">编辑</el-button>
            <el-dialog title="权限信息" :visible.sync="dialogFormVisible">
              <el-form label-position="right" label-width="80px" :model="permForm" size="small">
                <el-form-item label="序号">
                  <el-input v-model="permForm.id" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="名称">
                  <el-input v-model="permForm.name"></el-input>
                </el-form-item>
                <el-form-item label="路径">
                  <el-input v-model="permForm.url"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                  <el-input v-model="permForm.description"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updatePerm()">提 交</el-button>
              </div>
            </el-dialog>
            <el-popover placement="top" trigger="manual" :ref="'popover-' + scope.row.id">
              <p>你确定要删除该权限吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="close(scope.row.id)">取消</el-button>
                <el-button
                  type="primary"
                  size="mini"
                  @click="deletePerm(scope.$index, scope.row)"
                >确定</el-button>
              </div>
              <el-button type="danger" size="mini" slot="reference" @click="open(scope.row.id)">删除</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogFormVisible: false, // 编辑框
      index: 0, // 编辑的perm在table中的index，动态修改permList时使用
      permList: [],
      permForm: {
        // 编辑框的表单
        id: "",
        name: "",
        url: "",
        description: ""
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
    showDialog(index, row) {
      for (let i in this.permForm) {
        // 将数据复制到输入框
        this.permForm[i] = row[i];
      }
      this.index = index;
      this.dialogFormVisible = true;
    },
    addPerm() {
      var _this = this;
      this.axios
        .post("/config/addPerm", _this.permForm)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.permList.push(data);
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
    updatePerm() {
      var _this = this;
      this.axios
        .post("/config/updatePerm", _this.permForm)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.permList.splice(_this.index, 1, data);
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
    deletePerm(index, row) {
      var _this = this;
      this.axios
        .post("/config/deletePerm", { id: row.id })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.permList.splice(index, 1);
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
      .get("/config/getPermList")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.permList = data;
        } else {
          _this.error("获取权限信息失败！");
        }
      })
      .catch(function(error) {
        _this.error("获取权限信息失败！");
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