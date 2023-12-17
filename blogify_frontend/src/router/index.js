import Vue from 'vue'
import Router from 'vue-router'
import Login from '../components/Login.vue'
import Register from '../components/Register.vue'
import Display from '../components/Display.vue'
import Manage from '../components/Manage.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/test',
      name: 'Test',
      component: () => import('../components/SystemTest.vue')
    },
    {
      path: '/',
      name: 'Default',
      redirect: '/display',
      component: Display,
      meta: {
        authLevel: 0
      }
    },
    {
      path: '/display',
      name: 'Display',
      component: Display,
      redirect: '/index',
      meta: {
        authLevel: 0
      },
      children: [
        {
          path: '/index',
          name: 'DisplayIndex',
          component: () => import('../components/Display/DisplayIndex.vue'),
          meta: {
            authLevel: 0
          }
        },
        {
          path: '/display/read',
          name: 'DisplayRead',
          component: () => import('../components/Display/DisplayRead.vue'),
          meta: {
            authLevel: 0
          }
        },
        {
          path: '/display/about',
          name: 'DisplayAbout',
          component: () => import('../components/Display/DisplayAbout.vue'),
          meta: {
            authLevel: 0
          }
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        authLevel: 0
      }
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: {
        authLevel: 0
      }
    },
    {
      path: '/manage',
      name: '管理中心',
      component: Manage,
      redirect: '/manage/dashboard',
      meta: {
        authLevel: 1
      },
      children: [
        {
          path: '/manage/dashboard',
          name: '运行情况',
          component: () => import('../components/Manage/ManageDashboard.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/pictures',
          name: '图片管理',
          component: () => import('../components/Manage/ManagePictures.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/logs',
          name: '操作日志',
          component: () => import('../components/Manage/ManageOperationLogs.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/userList',
          name: '用户列表',
          component: () => import('../components/Manage/ManageUserList.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/userInfo',
          name: '个人中心',
          component: () => import('../components/Manage/ManageUserInfo.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/userNotice',
          name: '通知管理',
          component: () => import('../components/Manage/ManageUserNotice.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/essayWrite',
          name: '博文撰写',
          component: () => import('../components/Manage/ManageEssayWrite.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/essayList',
          name: '博文列表',
          component: () => import('../components/Manage/ManageEssayList.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/typeList',
          name: '分类列表',
          component: () => import('../components/Manage/ManageTypeList.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/tagList',
          name: '标签列表',
          component: () => import('../components/Manage/ManageTagList.vue'),
          meta: {
            authLevel: 1
          }
        },
        {
          path: '/manage/commentList',
          name: '评论管理',
          component: () => import('../components/Manage/ManageCommentList.vue'),
          meta: {
            authLevel: 1
          }
        }
      ]
    }
  ]
})
