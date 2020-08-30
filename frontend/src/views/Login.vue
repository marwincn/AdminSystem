<template>
  <el-form :model="loginForm" :rules="rules" ref="loginForm" status-icon class="login-container">
    <h3 class="formTitle">管理系统登录</h3>
    <el-form-item label="账号" prop="username">
      <el-input v-model="loginForm.username"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input type="password" v-model="loginForm.password" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="login()" :loading="logining">登 录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.loginForm.password !== "") {
          callback();
        }
      }
    };
    var validateName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入账号"));
      } else {
        if (this.loginForm.password !== "") {
          callback();
        }
      }
    };
    return {
      // 登录标记
      logining: false,
      // 登录表单
      loginForm: {
        username: "",
        password: ""
      },
      // 输入框验证规则
      rules: {
        username: [{ validator: validateName, trigger: "blur" }],
        password: [{ validator: validatePass, trigger: "blur" }]
      }
    };
  },
  methods: {
    login() {
      var _this = this;
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          var params = {"username": this.loginForm.username, "password": this.loginForm.password};
          this.logining = true;
          this.axios
            .post("/user/login", params)
            .then(function(response) {
              _this.logining = false;
              let { status, message, data } = response.data;
              if (status === 1) {
                sessionStorage.setItem('logged', true);
                _this.$router.push({ path: "/" });
              } else {
                _this.error(message);
              }
            })
            .catch(function(error) {
              _this.logining = false;
              _this.error("ERROR!");
            });
        } else {
          _this.error("ERROR SUBMIT!");
          return false;
        }
      });
    },
    error(message) {
      this.$message({
          showClose: true,
          message: message,
          type: 'error'
        });
    }
  }
};
</script>

<style scoped>
.login-container {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.formTitle {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}
</style>
