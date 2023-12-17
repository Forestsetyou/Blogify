<template>
  <body id="paper">
  <el-form class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <div class="back-index" @click="goToIndex" style="position: absolute; left: 36.5vw; top: 95px;">
      <el-button circle class="el-icon-s-home" style="font-size: 24px"></el-button>
    </div>
    <h3 class="login_title">用户登录</h3>
    <el-form-item>
      <el-input type="text" v-model="loginForm.username"
                auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item>
      <el-input type="password" v-model="loginForm.password"
                auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
      <el-checkbox class="password_remember" v-model="passwordRemember"
                   label-position="left"><span style="color: #505458">记住密码</span></el-checkbox>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 40%;background: #505458;border: none" v-on:click="login">登录</el-button>
      <el-button type="primary" v-on:click="goToRegister"
                 style="width: 40%; background: #505458; border: none">注册</el-button>
    </el-form-item>
  </el-form>
  </body>
</template>
<script>
export default{
  name: 'Login',
  data () {
    return {
      passwordRemember: true,
      loginForm: {
        username: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    goToRegister () {
      this.$confirm('前往注册?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.$router.replace('/register')
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    goToIndex () {
      this.$confirm('回到主页?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.$router.replace('/index')
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    login () {
      let loading = this.$loading({
        text: '登录中',
        background: 'rgba(0, 0, 0, 0.8)',
        spinner: 'el-icon-loading'
      })
      this.$axios
        .post('/user/login', {
          username: this.loginForm.username,
          password: this.loginForm.password
        })
        .then(resp => {
          loading.close()
          if (resp.data.code === 200) {
            this.$store.commit('login', resp.data.data)
            let menus = [
              {
                zhName: '运行情况',
                path: '/manage/dashboard',
                iconClass: 'el-icon-s-home'
              },
              {
                zhName: '博文撰写',
                path: '/manage/essayWrite',
                iconClass: 'el-icon-edit-outline'
              },
              {
                zhName: '博文列表',
                path: '/manage/essayList',
                iconClass: 'el-icon-document-copy'
              },
              {
                zhName: '分类列表',
                path: '/manage/typeList',
                iconClass: 'el-icon-folder-opened'
              },
              {
                zhName: '标签列表',
                path: '/manage/tagList',
                iconClass: 'el-icon-price-tag'
              },
              {
                zhName: '评论列表',
                path: '/manage/commentList',
                iconClass: 'el-icon-s-comment'
              },
              {
                zhName: '个人中心',
                path: '/manage/userInfo',
                iconClass: 'el-icon-s-custom'
              },
              {
                zhName: '用户列表',
                path: '/manage/userList',
                iconClass: 'el-icon-user'
              },
              {
                zhName: '图片管理',
                path: '/manage/pictures',
                iconClass: 'el-icon-picture'
              },
              {
                zhName: '通知管理',
                path: '/manage/userNotice',
                iconClass: 'el-icon-chat-line-round'
              },
              {
                zhName: '操作日志',
                path: '/manage/logs',
                iconClass: 'el-icon-tickets'
              }
            ]
            this.$store.commit('initManageMenu', menus)
            this.$alert('登录成功', '提示', {
              confirmButtonText: '确定',
              callback: action => this.$router.replace('/index')
            })
          } else {
            this.$alert('用户名或密码错误!', '提示', {
              confirmButtonText: '确定'
            })
          }
        })
        .catch(failResponse => {
          loading.close()
          console.log(failResponse)
        })
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
margin: 0px 0px;
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
.password_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>
