<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label="">
          <el-input v-model="searchWord"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary">搜索订单</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 10px">
      <el-table :data="orderList" style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="标题">
                <span>{{ props.row.itemTitle }}</span>
              </el-form-item>
              <el-form-item label="标题">
                <span>{{ props.row.price }}</span>
              </el-form-item>
              <el-form-item label="提单人">
                <span>{{ props.row.author }}</span>
              </el-form-item>
              <el-form-item label="接单人">
                <span>{{ props.row.user }}</span>
              </el-form-item>
              <el-form-item label="创建时间">
                <span>{{ props.row.createTime }}</span>
              </el-form-item>
              <el-form-item label="上次修改">
                <span>{{ props.row.modifyTime }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="序号" width="100"></el-table-column>
        <el-table-column prop="itemTitle" label="标题"></el-table-column>
        <el-table-column prop="price" label="定价"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="state" label="状态"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDialog(scope.$index, scope.row)">编辑</el-button>
            <el-dialog title="订单信息" :visible.sync="dialogFormVisible">
              <el-form label-position="right" label-width="80px" :model="orderForm" size="small">
                <el-form-item label="序号">
                  <el-input v-model="orderForm.id" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="状态">
                  <el-input v-model="orderForm.state"></el-input>
                </el-form-item>
                <el-form-item label="标题">
                  <el-input v-model="orderForm.itemTitle" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="定价">
                  <el-input v-model="orderForm.price"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateOrder()">提 交</el-button>
              </div>
            </el-dialog>
            <el-popover placement="top" trigger="manual" :ref="'popover-' + scope.row.id">
              <p>你确定要删除该订单吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="close(scope.row.id)">取消</el-button>
                <el-button type="primary" size="mini" @click="delOrder(scope.$index, scope.row)">确定</el-button>
              </div>
              <el-button type="danger" size="mini" slot="reference" @click="open(scope.row.id)">删除</el-button>
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
      index: 0,
      orderList: [],
      orderForm: {
        // 编辑框的表单
        id: "",
        state: 0,
        itemTitle: "",
        price: "",
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
      for (let i in this.orderForm) {
        // 将数据复制到输入框
        this.orderForm[i] = row[i];
      }
      this.index = index;
      this.dialogFormVisible = true;
    },
    handleCurrChange(val) {
      var _this = this;
      this.axios
        .get(
          "/config/getOrderList?pageSize=" +
            _this.pageSize +
            "&currPage=" +
            val
        )
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.orderList = data;
          } else {
            _this.error(message);
          }
        })
        .catch(function(error) {
          _this.error("获取订单列表失败！");
        });
    },
    updateOrder() {
      var _this = this;
      this.axios
        .post("/config/updateOrder", _this.orderForm)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.orderList.splice(_this.index, 1, data);
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
    deleteOrder(index, row) {
      var _this = this;
      this.axios
        .post("/config/deleteOrder", { id: row.id })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.orderList.splice(index, 1);
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
      .get(
        "/config/getOrderList?pageSize=" +
          _this.pageSize +
          "&currPage=" +
          1
      )
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.orderList = data;
        } else {
          _this.error(message);
        }
      })
      .catch(function(error) {
        _this.error("获取订单列表失败！");
      });
    this.axios
      .get("/config/getOrderCount")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.total = data;
        } else {
          _this.error(message);
        }
      })
      .catch(function(error) {
        _this.error("获取订单总数失败！");
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