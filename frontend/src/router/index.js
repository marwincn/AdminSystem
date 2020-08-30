import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/views/Index'
import Login from '@/views/Login'
import Blog from '@/views/nav/Blog'
import Catalog from '@/views/nav/Catalog'
import Picture from '@/views/nav/Picture'
import File from '@/views/nav/File'
import Item from '@/views/nav/Item'
import Order from '@/views/nav/Order'
import Perm from '@/views/nav/Perm'
import Role from '@/views/nav/Role'
import User from '@/views/nav/User'
import SearchUser from '@/views/card/SearchUser'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      component: Login,
      name: '登录',
      hidden: true
    },
    {
      path: '/',
      component: Index,
      name: '管理中心',
      children: [
        {
          path:'/blog',
          component: Blog,
          name: '动态管理'
        },
        {
          path:'/catalog',
          component: Catalog,
          name: '分类管理'
        },
        {
          path:'/picture',
          component: Picture,
          name: '配图管理'
        },
        {
          path:'/file',
          component: File,
          name: '文件管理'
        },
        {
          path:'/item',
          component: Item,
          name: '项目管理'
        },
        {
          path:'/order',
          component: Order,
          name: '订单管理'
        },
        {
          path:'/perm',
          component: Perm,
          name: '权限管理'
        },
        {
          path:'/role',
          component: Role,
          name: '角色管理'
        },
        {
          path:'/user',
          component: User,
          name: '用户管理',
          children: [
            {
              path: '/searchUser',
              component: SearchUser,
              name: '搜索用户'
            }
          ]
        }
      ]
    }
  ]
})
