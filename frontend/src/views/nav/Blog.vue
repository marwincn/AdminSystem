<template>
  <div>
    <el-card>
      <el-form :inline="true">
        <el-form-item label>
          <el-input v-model="searchWord"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary">搜索动态</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 10px">
      <el-card class="box-card" v-for="blog in blogList" :key="blog.id">
        <div slot="header" class="clearfix">
          <img :src="blog.avatar" style="height: 35px; width: 35px; border-radius: 50%;">
          <div style="display: inline-block; margin-left: 5px">
            <el-button style="padding: 0 0;" type="text">{{blog.author}}</el-button>
            <br>
            <span class="text" style="margin-right: 5px;">{{blog.publishTime}}</span>
          </div>
        </div>
        <div style="margin-bottom: 10px">{{blog.content}}</div>
        <el-carousel trigger="click" height="200px" v-if="hasPicture(blog.pictures)">
          <el-carousel-item v-for="picture in blog.pictures" :key="picture.url">
            <el-image :src="picture.url" fit="contain">
              <div slot="placeholder">
                图片加载中<span>...</span>
              </div>
              <div slot="error">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </el-carousel-item>
        </el-carousel>
        <div style="margin-top: 15px;">
          <span class="text">浏览({{blog.viewCount}})</span>
          <el-divider direction="vertical"></el-divider>
          <span class="text">赞({{blog.favourCount}})</span>
          <el-divider direction="vertical"></el-divider>
          <span class="text">评论({{blog.commentCount}})</span>
        </div>
      </el-card>
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
      total: 0,
      currPage: 1,
      pageSize: 12,
      searchWord: "",
      blogList: []
    };
  },
  methods: {
    handleCurrChange(val) {
      var _this = this;
      this.axios
        .get("/blog/listByTime?pageSize=" + _this.pageSize + "&currPage=" + val)
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 1) {
            _this.blogList = data;
          } else {
            _this.error(message);
          }
        })
        .catch(function(error) {
          _this.error("获取动态列表失败！");
        });
    },
    hasPicture(list) {
      if (list.length > 0) {
        return true;
      } else {
        return false;
      }
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
      .get("/blog/count")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.total = data;
        } else {
          error(message);
        }
      })
      .catch(function(error) {
        error("获取动态列表失败！");
      });
    this.axios
      .get("/blog/listByTime?pageSize=" + _this.pageSize + "&currPage=1")
      .then(function(response) {
        let { status, message, data } = response.data;
        if (status === 1) {
          _this.blogList = data;
        } else {
          error(message);
        }
      })
      .catch(function(error) {
        error("获取动态列表失败！");
      });
  }
};
</script>
<style scoped>
.text {
  font-size: 14px;
}
.box-card {
  width: 30%;
  display: inline-table;
  margin: 10px 10px 10px 10px;
}
</style>