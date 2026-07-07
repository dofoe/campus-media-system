import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: '/upload',
        name: 'Upload',
        component: () => import('@/views/Upload.vue')
      },
      {
        path: '/media/:id',
        name: 'MediaDetail',
        component: () => import('@/views/MediaDetail.vue')
      },
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: '/admin/media',
        name: 'MediaAdmin',
        component: () => import('@/views/admin/MediaAdmin.vue')
      },
      {
        path: '/admin/rules',
        name: 'Rules',
        component: () => import('@/views/admin/Rules.vue')
      },
      {
        path: '/admin/users',
        name: 'Users',
        component: () => import('@/views/admin/Users.vue')
      },
      {
        path: '/admin/audit',
        name: 'Audit',
        component: () => import('@/views/admin/Audit.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
