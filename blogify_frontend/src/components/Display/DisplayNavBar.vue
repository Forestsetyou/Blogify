<template>
  <!--导航-->
  <div>
    <div class="animate__animated animate__fadeIn title" :key="0" :style="titleStyle"></div>
    <el-header :style="'margin-bottom:'+ headerBottom +'px'" class="animate__animated animate__fadeIn">
      <h2 class="animate__animated animate__swing logo" :key="shade" @click="shade++">Blogify</h2>
      <el-menu :default-active="currentPath" class="el-menu-demo" mode="horizontal" background-color="rgba(0,0,0,0)"
               style="border: none;"
               router text-color="#fff" active-text-color="#ffd04b">
        <el-menu-item v-for="item in displayMenu" :index="item.path" background-color="rgba(0,0,0,0)" :key="item.path">
          <template slot="title">
            <i :class="item.iconClass"></i>
            <span>{{ item.zhName }}</span>
          </template>
        </el-menu-item>
      </el-menu>
<!--      <div v-if="menuHiddenVisiable">-->
<!--        <el-menu :default-active="currentPath" class="animate__animated animate__fadeInDown el-menu-hidden"-->
<!--                 background-color="#545c64" router text-color="#fff" active-text-color="#ffd04b">-->
<!--          <el-menu-item :index="item.path" v-for="item in displayMenu" :key="item.path">-->
<!--            <template slot="title">-->
<!--              <i :class="item.iconClass"></i>-->
<!--              <span>{{ item.zhName }}</span>-->
<!--            </template>-->
<!--          </el-menu-item>-->
<!--        </el-menu>-->
<!--      </div>-->
      <div class="menu-expend" @click="menuExpend">
        <i class="el-icon-menu"></i>
      </div>
      <div class="search_input">
        <el-input
          @focus="searchFocus"
          @blur="searchBlur"
          class="search"
          placeholder="请输入搜索内容"
          prefix-icon="el-icon-search"
          v-model="searchState.query"
          size="mini">
        </el-input>
        <ul v-if="searchState.focus && searchState.search">
          <el-link class="animate__animated animate__fadeInDown recommend-blog l-text" v-for="essay in searchState.dataList" :key="essay.id"
              @click="readEssay(essay)">
            <a>{{essay.title}}</a>
          </el-link>
        </ul>
      </div>
      <div v-if="!authState" style="margin-right: 50px">
        <el-button size="mini" effect="light" type="primary" @click="gotoLogin">登录</el-button>
        <el-button size="mini" effect="light" type="warning" @click="goToRegister">注册</el-button>
      </div>
      <div v-else class="loginInfo">
        <el-avatar :src="authUser.blogFile.publishUrl" @error="avatarLoadError">
          <el-avatar icon="el-icon-user-solid" size="large" style="background-color: #666"></el-avatar>
        </el-avatar>
        <div class="user-option">
          <h3 class="web-font nickname">{{ authUser.nickname }}</h3>
          <p v-if="authUser.roleId === 1" class="logout" @click="gotoManage">管理博客</p>
          <p class="logout" @click="logout">退出登录</p>
        </div>
      </div>
    </el-header>
  </div>
</template>

<script>
export default {
  components: {},
  data () {
    return {
      searchState: {
        focus: false,
        search: false,
        query: '',
        timer: null,
        dataList: [],
        source: '/display/essays'
      },
      shade: 0,
      backgroundImage: this.$blogify.default_background,
      titleStyle: {
        'background-image': 'url(\'' + this.$blogify.default_background + '\')'
      },
      displayMenu: this.$blogify.DisplayMenu,
      scrollFlag: false,
      menuHiddenVisiable: false,
      headerBottom: 0
    }
  },
  computed: {
    authUser () {
      return this.$store.state.user
    },
    currentPath () {
      return this.$route.path
    },
    authState () {
      return this.$store.state.user.id != null
    }
  },
  watch: {
    'searchState.query' () {
      if (this.searchState.query !== '') {
        this.searchState.search = true
      } else {
        this.searchState.search = false
      }
      if (this.searchState.timer) {
        clearTimeout(this.searchState.timer)
      }
      this.searchState.timer = setTimeout(() => {
        this.searchSubmit()
      }, 300)
    }
  },
  mounted () {
    window.addEventListener('scroll', this.handleScroll)
  },
  methods: {
    avatarLoadError (error) {
      console.log(error)
      return true
    },
    gotoLogin () {
      this.$confirm('前往登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.$router.replace('/login')
      }).catch(failResp => {
        console.log(failResp)
      })
    },
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
    gotoManage () {
      this.$confirm('前往管理后台?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        this.$router.replace('/manage')
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    logout () {
      this.$confirm('确认退出登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.get('/user/logout').then(resp => {
          console.log(resp)
          if (resp && resp.data.code === 200) {
            this.$store.commit('logout')
            this.$store.commit('refreshMenu')
            console.log(this)
            // **
          }
          this.$router.replace('/login')
          this.$message.info('已退出登录')
        }).catch(failResp => {
          this.$message.info('已取消')
        })
      })
    },
    searchFocus () {
      this.searchState.focus = true
    },
    searchBlur () {
      setTimeout(() => {
        this.searchState.focus = false
      }, 100)
      this.searchState.focus = false
    },
    searchSubmit () {
      console.log('search')
      console.log(this)
      if (this.searchState.focus && this.searchState.search) {
        this.$axios.post(this.searchState.source, {keyword: this.searchState.query}).then(resp => {
          if (resp.data.code === 200) {
            this.searchState.dataList = resp.data.data.slice(0, 5)
          } else {
            this.$message.info('搜索失败!')
          }
        }).catch(failResp => {
          console.log(failResp)
        })
      }
    },
    readEssay (essay) {
      this.searchBlur()
      this.searchState.query = ''
      this.$store.commit('readEssay', essay)
      this.$router.push('/display/read')
    },
    handleScroll () {
      const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      if (scrollTop > 60 && this.scrollFlag === false) {
        this.scrollFlag = true
      } else if (scrollTop <= 60 && this.scrollFlag === true) {
        this.scrollFlag = false
      }
    },
    menuExpend () {
      this.menuHiddenVisiable = !this.menuHiddenVisiable
      if (this.menuHiddenVisiable === true) {
        this.headerBottom = 280
      } else {
        this.headerBottom = 0
      }
    }
  }
}
</script>

<style scoped lang="less">

.title {
  position: absolute;
  width: 100%;
  height: 100%;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  //border-bottom: 3px solid #fff;
}

.el-header {
  transition: .2s;
}

.el-header:hover {
  opacity: 1 !important;
}

.el-menu {
  background-color: rgba(0, 0, 0, 0) !important;
}

.el-menu /deep/ .el-menu-item{
  background-color: rgba(0, 0, 0, 0) !important;
}

.el-menu /deep/ .el-menu-item i {
  color: rgba(255, 255, 255);
}

.el-menu /deep/ .el-menu-item:hover i {
  color: white;
}

.el-menu /deep/ .el-menu-item:hover {
  background-color: rgba(0, 0, 0, 0.5) !important;
}

.search_input {
  position: relative;
  box-sizing: border-box;
}

.search_input ul {
  position: absolute;
  top: 30px;
  width: 100%;
  border: 1px solid #e5e9ef;
  margin-top: 1px;
  background: #fff;
  z-index: 10000;
  border-radius: 2px;
  box-shadow: 0 2px 4px rgba(58, 118, 142, 0.16);
  padding: 10px 0;
  font-size: 14px;
  box-sizing: border-box;
}

.search_input ul li {
  padding: 0 16px;
  height: 32px;
  line-height: 32px;
  cursor: pointer;
  word-wrap: break-word;
  word-break: break-all;
  display: block;
  color: #222;
  position: relative;
  /*transition: .2 ease;*/
}

.search_input ul li:hover {
  background-color: #e8f3ff;
}

.search-blog {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  padding-left: 20px;
  padding-right: 20px;
}

.el-header {
  width: 100%;
  position: sticky;
  top: 0;
  z-index: 9999;
  background-color: rgba(0, 0, 0, 0.2);
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;

  > div {
    display: flex;
    align-items: center;
  }

  img {
    height: 40px;
  }

  span {
    margin-left: 15px;
  }

  button {
    opacity: 0.8;
  }

  .el-menu {
    flex-shrink: 0;
  }

  .logo {
    color: #ffd04b;
  }

  .logo:hover {
    cursor: pointer;
  }

}

.loginInfo {
  flex-shrink: 0;
  /*background-color: #545c64;*/
  color: white;
  height: 60px;
  border: none;
  width: 160px;
  position: relative;

  .el-avatar {
    width: 36px;
    height: 36px;
    margin: 0 auto;
    z-index: 200;
  }

  .user-option {
    position: absolute;
    top: 60px;
    width: 150px;
    left: 50%;
    transform: translate(-50%, 0);
    font-size: 14px;
    text-align: center;
    line-height: 30px;
    background-color: #fff;
    visibility: hidden;
    opacity: 0;
    color: #333;
    box-shadow: 0 2px 6px 0 rgb(0 0 0 / 10%);
    border: 1px solid #eee;
    border-radius: 5px;
  }

  .logout {
    margin: 10px auto;
    padding: 0;
    width: 100%;
  }

  .logout:hover {
    background-color: #eee;
  }
}

.loginInfo:hover {
  cursor: pointer;

  .el-avatar {
    transform: translate(0, 50%) scale(1.5);
    transition: .3s;
  }

  .user-option {
    visibility: visible;
    opacity: 1;
    transition: opacity .4s;
  }
}

.zZindex {
  margin-top: 300px;
  padding-top: 100px;
}

.menu-expend {
  display: none !important;
}

.el-menu-hidden {
  /*display: none;*/
  position: absolute;
  top: 60px;
  left: 0;
  border-top: 1px solid #ccc;
  border-right: none;
  width: 100%;

  .el-menu-item {
    border-bottom: 1px solid #ccc;
  }
}

@media screen and (max-width: 1000px) {
  .search_input {
    visibility: hidden;
  }
}

@media screen and (max-width: 768px) {
  .el-menu /deep/ .el-menu-item{
    background-color: rgba(0, 0, 0, 0.3) !important;
  }

  .el-menu-demo {
    display: none;
  }

  .title {
    position: absolute;
    width: 100%;
    background-position-x: center;
    background-position-y: 0;
  }

  .menu-expend {
    display: block !important;
    position: fixed;
    top: 18px;
    left: 150px;
  }

  .menu-expend:hover {
    color: #ffd04b;
    cursor: pointer;
  }
}

.recommend-blog {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  padding-left: 20px;
  padding-right: 20px;

  a {
    border-bottom: 1px solid rgba(34, 36, 38, .15);
    line-height: 40px;
    display: block;
    text-decoration: none;
    color: black;
  }

  a:hover {
    color: #3a8ee6;
  }
}
</style>

<style>
body{
  margin: 0 0;
}
.search input.el-input__inner {
  background-color: rgba(0, 0, 0, 0.1);
  /*border-radius: 20px;*/
  color: #cccccc;
}
</style>
