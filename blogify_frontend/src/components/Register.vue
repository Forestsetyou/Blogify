<template>
  <body id="paper">
  <el-form class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <h3 class="login_title">用户注册</h3>
    <el-form-item>
      <el-input type="text" v-model="loginForm.username"
                auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item>
      <el-input type="password" v-model="loginForm.password"
                auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item>
      <el-input type="password" v-model="loginForm.checkPassword"
                auto-complete="off" placeholder="确认密码"></el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 40%;background: #505458;border: none" v-on:click="register">注册</el-button>
      <el-button type="primary" style="width: 40%;background: #505458;border: none" v-on:click="goToLogin">登录</el-button>
    </el-form-item>
  </el-form>
  </body>
</template>
<script>
export default{
  name: 'Register',
  data () {
    return {
      checked: true,
      loginForm: {
        username: '',
        password: '',
        checkPassword: ''
      },
      loading: false
    }
  },
  methods: {
    goToLogin () {
      this.$confirm('前往登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.$router.replace('/login')
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    register () {
      if (this.loginForm.checkPassword !== this.loginForm.password) {
        this.$alert('请输入两次相同的密码!', '提示')
        return
      }
      let loading = this.$loading({
        text: '注册中',
        background: 'rgba(0, 0, 0, 0.8)',
        spinner: 'el-icon-loading'
      })
      this.$axios
        .post('/user', {
          username: this.loginForm.username,
          password: this.loginForm.password
        })
        .then(resp => {
          loading.close()
          if (resp.data.code === 200) {
            this.$alert('注册成功', '提示', {
              confirmButtonText: '确定',
              callback: action => this.$router.replace('/login')
            })
          } else {
            this.$alert(resp.data.message, '提示', {
              confirmButtonText: '确定'
            })
          }
        })
        .catch(failResponse => {})
    }
  }
}
</script>
<style>
#paper {
  background: url("../assets/img/Large/login_bg.jpg") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}
body{
  margin: 0 0;
}
.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 90px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.login_title {
  margin: 0 auto 40px auto;
  text-align: center;
  color: #505458;
}
</style>
