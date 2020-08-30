<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label="名称">
          <el-input v-model="cataForm.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="addCata()">添加分类</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 10px">
      <el-table :data="cataList" style="width: 100%">
        <el-table-column prop="id" label="序号" width="100"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDialog(scope.$index, scope.row)">编辑</el-button>
            <el-dialog title="角色信息" :visible.sync="dialogFormVisible">
              <el-form label-position="right" label-width="80px" :model="cataForm" size="small">
                <el-form-item label="序号">
                  <el-input v-model="cataForm.id" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="名称">
                  <el-input v-model="cataForm.name"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateCata()">提 交</el-button>
              </div>
            </el-dialog>
            <el-popover placement="top" trigger="manual" :ref="'popover-' + scope.row.id">
              <p>你确定要删除该角色吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="close(scope.row.id)">取消</el-button>
                <el-button
                  type="primary"
                  size="mini"
                  @click="deleteCata(scope.$index, scope.row)"
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
      index: 0,
      cataList: [],
      cataForm: {
        // 编辑框的表单
        id: "",
        name: ""
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
      for (let i in this.cataForm) {
        // 将数据复制到输入框
        this.cataForm[i] = row[i];
      }
      this.index = index;
      this.dialogFormVisible = true;
    },
    addCata() {
      var _this = this;
      this.axios
        .post("/config/addCata", _this.cataForm)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.cataList.push(data);
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
    updateCata() {
      var _this = this;
      this.axios
        .post("/config/updateCata", _this.cataForm)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.cataList.splice(_this.index, 1, data);
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
    deleteCata(index, row) {
      var _this = this;
      this.axios
        .post("/config/deleteCata", { id: row.id })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.cataList.splice(index, 1);
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
      .get("/config/getCataList")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.cataList = data;
        } else {
          _this.error("获取分类列表失败！");
        }
      })
      .catch(function(error) {
        _this.error("获取分类列表失败！");
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