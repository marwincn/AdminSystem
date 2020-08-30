<template>
  <div>
    <el-card>
      <el-upload
        action="http://localhost:8888/file/upAvatar"
        list-type="picture-card"
        :on-success="handleSuccess"
        :on-preview="handlePictureCardPreview"
        :on-remove="handleRemove"
      >
        <i class="el-icon-plus"></i>
      </el-upload>
      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogImageUrl: "",
      dialogVisible: false,
      pictures: []
    };
  },
  methods: {
    handleSuccess(response, file, fileList) {
      this.pictures.push(response.data);
      file.url = response.data;
      console.log(this.pictures, file);
    },
    handleRemove(file, fileList) {
      // 从服务器和数据库里删除
      // 从this.picture里删除
      var _this = this;
      console.log(file);
      this.axios
        .post("/file/delPicture", { url: file.url })
        .then(function(response) {
          let { status, message, data } = response.data;
          if (status === 0) {
            _this.error("图片在服务器删除失败，请重试或手动删除！");
          }
        })
        .catch(function(error) {
          _this.error("图片在服务器删除失败，请重试或手动删除！");
        });
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
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
  }
};
</script>