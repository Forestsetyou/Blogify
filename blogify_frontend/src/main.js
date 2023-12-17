// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import store from './store'
import * as echarts from 'echarts'
import waterfall from 'vue-waterfall2'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import 'github-markdown-css/github-markdown.css'
import hljs from 'highlight.js'

document.title = 'Blogify'

let blogify = {
  default_avatar: '/static/default_avatar.png',
  default_background: '/static/default_background.png',
  default_footer: '/static/default_footer.jpg',
  default_essay_pic: '/static/default_essay_pic.jpg',
  status: {
    editEssay: null,
    searchType: null,
    searchTag: null
  },
  statusClear () {
    this.status = {
      editEssay: null,
      searchType: null,
      searchTag: null
    }
  },
  DisplayMenu: [
    // {
    //   zhName: '关于',
    //   path: '/display/about',
    //   iconClass: 'el-icon-info'
    // },
    {
      zhName: '首页',
      path: '/index',
      iconClass: 'el-icon-s-home'
    }
  ]
}

const yiyan = axios.create({ // 博客后台api地址
  baseURL: 'https://v1.hitokoto.cn/'
})

axios.defaults.baseURL = 'http://localhost:7969/api/v1'
axios.defaults.withCredentials = true

Vue.prototype.$axios = axios
Vue.prototype.$yiyan = yiyan
Vue.prototype.$echarts = echarts
Vue.prototype.$blogify = blogify

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(waterfall)
Vue.use(mavonEditor)

// 如果开启了typescript 需要额外安装 npm install @types/highlight.js
// 也可以把这自定义指令封装 通过Vue.use()，来注入
Vue.directive('highlight', function (el) {
  const blocks = el.querySelectorAll('pre code')
  blocks.forEach(block => {
    hljs.highlightBlock(block)
  })
})

router.beforeEach((to, from, next) => {
  if (to.meta.authLevel) {
    axios.get('/role/auth').then(resp => {
      if (resp.data.code !== 200) {
        next({
          path: '/login',
          query: {redirect: to.fullPath}
        })
      } else if (resp.data.data.id > to.meta.authLevel) {
        next({
          path: '/login',
          query: {redirect: to.fullPath}
        })
      } else {
        next()
      }
    })
  } else {
    next()
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
