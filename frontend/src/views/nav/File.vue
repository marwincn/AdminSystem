<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label>
          <el-input v-model="searchWord"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary">搜索文件</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 10px">
      <el-table :data="fileList" style="width: 100%">
        <el-table-column prop="name" label="名称"></el-table-column>

        <el-table-column prop="size" label="大小（B）"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="showPicture(scope.row)">查看</el-button>
            <el-dialog :visible.sync="dialogPictureVisible">
              <el-image :src="pictureUrl">
                <div slot="placeholder" class="image-slot">
                  图片加载中
                  <span class="dot">...</span>
                </div>
              </el-image>
              <div slot="footer" class="dialog-footer">
                <el-button size="mini" type="primary" @click="compPicture()">压 缩</el-button>
                <el-button @click="dialogPictureVisible = false">返 回</el-button>
              </div>
            </el-dialog>
            <el-button size="mini" @click="showDialog(scope.$index, scope.row)">编辑</el-button>
            <el-dialog title="文件信息" :visible.sync="dialogFormVisible">
              <el-form label-position="right" label-width="80px" :model="fileForm" size="small">
                <el-form-item label="文件名">
                  <el-input v-model="fileForm.name" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="大小">
                  <el-input v-model="fileForm.size" :disabled="true"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateFile()">提 交</el-button>
              </div>
            </el-dialog>
            <el-popover placement="top" trigger="manual" :ref="'popover-' + scope.row.id">
              <p>你确定要删除该文件吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="close(scope.row.id)">取消</el-button>
                <el-button type="primary" size="mini" @click="delFile(scope.$index, scope.row)">确定</el-button>
              </div>
              <el-button type="danger" size="mini" slot="reference" @click="open(scope.row.id)">删除</el-button>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
      <div
        class="block"
        style="float:right; margin-right: 50px; margin-top: 20px; padding-bottom: 20px"
      >
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
      context: "http://114.116.77.118:8888/",
      searchWord: "",
      total: 0,
      currPage: 1,
      pageSize: 20,
      pictureUrl: "",
      dialogPictureVisible: false, // 图片框
      dialogFormVisible: false, // 编辑框
      index: 0,
      fileList: [],
      fileForm: {
        // 编辑框的表单
        name: "",
        size: 0
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
    showPicture(row) {
      this.pictureUrl = this.context + row.name;
      this.dialogPictureVisible = true;
    },
    showDialog(index, row) {
      for (let i in this.fileForm) {
        // 将数据复制到输入框
        this.fileForm[i] = row[i];
      }
      this.index = index;
      this.dialogFormVisible = true;
    },
    compPicture() {
      var _this = this;
      this.axios
        .post("/config/compPicture", { url: _this.pictureUrl, scale: 0.6 })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.dialogPictureVisible = false;
            _this.success(message);
          } else {
            _this.dialogPictureVisible = false;
            _this.error(message);
          }
        })
        .catch(function(error) {
          _this.dialogPictureVisible = false;
          _this.error("ERROR!");
        });
    },
    updateFile() {
      this.dialogFormVisible = false;
    },
    delFile(index, row) {
      var _this = this;
      this.axios
        .post("/config/deleteFile", { url: row.name })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.fileList.splice(index, 1);
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
    handleCurrChange(val) {
      var _this = this;
      this.axios
        .get(
          "/config/getFileList?pageSize=" + _this.pageSize + "&currPage=" + val
        )
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.fileList = data;
          } else {
            _this.error(message);
          }
        })
        .catch(function(error) {
          _this.error("获取文件列表失败！");
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
      .get("/config/getFileList?pageSize=" + _this.pageSize + "&currPage=" + 1)
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.fileList = data;
        } else {
          _this.error(message);
        }
      })
      .catch(function(error) {
        _this.error("获取文件列表失败！");
      });
    this.axios
      .get("/config/getFileCount")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.total = data;
        } else {
          _this.error(message);
        }
      })
      .catch(function(error) {
        _this.error("获取文件总数失败！");
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