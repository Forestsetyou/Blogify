import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: window.localStorage.getItem('user' || '[]') == null ? {blogFile: {}} : JSON.parse(window.localStorage.getItem('user' || '[]')),
    manageMenu: window.localStorage.getItem('menu' || '[]') == null ? [] : JSON.parse(window.localStorage.getItem('menu' || '[]')),
    readEssay: window.localStorage.getItem('essay' || '[]') == null ? {} : JSON.parse(window.localStorage.getItem('essay' || '[]'))
  },
  mutations: {
    initManageMenu (state, menus) {
      state.manageMenu = menus
      window.localStorage.setItem('menu', JSON.stringify(menus))
    },
    login (state, user) {
      state.user = user
      if (state.user.blogFile == null) {
        state.user.blogFile = {}
      }
      window.localStorage.setItem('user', JSON.stringify(user))
    },
    logout (state) {
      state.user = {
        blogFile: {}
      }
      window.localStorage.removeItem('user')
    },
    refreshMenu (state) {
      state.manageMenu = []
      window.localStorage.removeItem('menu')
    },
    readEssay (state, options) {
      state.readEssay = options.essay
      window.localStorage.setItem('essay', JSON.stringify(options.essay))
      options.axios.get('/display/essays/view/' + options.essay.id).then(resp => {
        if (resp.data.code === 200) {
          console.log('博客: "' + options.essay.title + '" 浏览量 +1')
        } else {
          console.log('博客: "' + options.essay.title + '" 浏览量更新失败')
        }
      })
    },
    removeEssay (state) {
      state.readEssay = null
      window.localStorage.removeItem('essay')
    }
  }
})
