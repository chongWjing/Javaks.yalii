import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

import Login from '../views/auth/Login.vue'
import Register from '../views/auth/Register.vue'
import Home from '../views/home/Home.vue'
import ItemList from '../views/items/ItemList.vue'
import ItemDetail from '../views/items/ItemDetail.vue'
import PublishItem from '../views/items/PublishItem.vue'
import EditItem from '../views/items/EditItem.vue'
import MyClaims from '../views/claims/MyClaims.vue'
import AdminClaims from '../views/claims/AdminClaims.vue'
import NotificationList from '../views/notifications/NotificationList.vue'
import UserManage from '../views/admin/UserManage.vue'
import Reports from '../views/admin/Reports.vue'
import Stats from '../views/admin/Stats.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login, meta: { guest: true } },
  { path: '/register', name: 'Register', component: Register, meta: { guest: true } },
  { path: '/', name: 'Home', component: Home, meta: { requiresAuth: true } },
  { path: '/items', name: 'ItemList', component: ItemList, meta: { requiresAuth: true } },
  { path: '/items/:id', name: 'ItemDetail', component: ItemDetail, meta: { requiresAuth: true } },
  { path: '/items/:id/edit', name: 'EditItem', component: EditItem, meta: { requiresAuth: true } },
  { path: '/publish', name: 'PublishItem', component: PublishItem, meta: { requiresAuth: true } },
  { path: '/my-claims', name: 'MyClaims', component: MyClaims, meta: { requiresAuth: true } },
  { path: '/admin/claims', name: 'AdminClaims', component: AdminClaims, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/users', name: 'UserManage', component: UserManage, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/reports', name: 'Reports', component: Reports, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/stats', name: 'Stats', component: Stats, meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/notifications', name: 'Notifications', component: NotificationList, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.guest && authStore.isAuthenticated) {
    next('/')
  } else if (to.meta.requiresAdmin && authStore.user?.role !== 'ADMIN') {
    next('/')
  } else {
    next()
  }
})

export default router
