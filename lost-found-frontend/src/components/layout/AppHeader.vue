<template>
  <header class="app-header">
    <div class="header-inner">
      <div class="header-left">
        <router-link to="/" class="logo-link">
          <span class="logo-icon">&#128269;</span>
          <span class="logo-text">失物招领</span>
        </router-link>
      </div>
      <nav class="header-nav">
        <router-link to="/" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>
          首页
        </router-link>
        <router-link to="/items" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
          物品列表
        </router-link>
        <router-link to="/publish" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
          发布物品
        </router-link>
        <router-link to="/my-claims" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14,2 14,8 20,8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
          我的认领
        </router-link>
        <router-link v-if="authStore.isAdmin" to="/admin/claims" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
          认领管理
        </router-link>
        <router-link v-if="authStore.isAdmin" to="/admin/users" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4-4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>
          用户管理
        </router-link>
        <router-link v-if="authStore.isAdmin" to="/admin/reports" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
          举报管理
        </router-link>
        <router-link v-if="authStore.isAdmin" to="/admin/stats" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 20V10"/><path d="M12 20V4"/><path d="M6 20v-6"/></svg>
          统计报表
        </router-link>
        <router-link to="/notifications" class="nav-link" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/></svg>
          通知
          <span v-if="unreadCount > 0" class="unread-badge">{{ unreadCount }}</span>
        </router-link>
      </nav>
      <div class="header-right">
        <div class="user-avatar">{{ authStore.user?.username?.charAt(0)?.toUpperCase() }}</div>
        <span class="user-name">{{ authStore.user?.username }}</span>
        <button class="logout-btn" @click="authStore.logout()">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/><polyline points="16,17 21,12 16,7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
          退出
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../../store/auth'
import api from '../../services/api'

const authStore = useAuthStore()
const unreadCount = ref(0)

const fetchUnreadCount = async () => {
  try {
    const response = await api.get(`/notifications/user/${authStore.user.id}/unread-count`)
    if (response.data.success) {
      unreadCount.value = response.data.data.count
    }
  } catch (error) {
    console.error('Failed to fetch unread count:', error)
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchUnreadCount()
  }
})
</script>

<style scoped>
.app-header {
  background: rgba(15, 23, 42, 0.7);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}
.header-inner {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 64px;
}
.header-left { display: flex; align-items: center; }
.logo-link {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: white;
}
.logo-icon {
  font-size: 24px;
  filter: drop-shadow(0 0 8px rgba(99, 102, 241, 0.5));
}
.logo-text {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, #818cf8, #6ee7b7);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.header-nav {
  display: flex;
  align-items: center;
  gap: 4px;
}
.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  color: var(--gray-300);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  border-radius: var(--radius-sm);
  transition: all 0.3s var(--ease);
  position: relative;
  overflow: hidden;
}
.nav-link::before {
  content: '';
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: var(--primary);
  opacity: 0;
  transform: translateY(100%);
  transition: all 0.3s var(--ease);
  z-index: -1;
  border-radius: var(--radius-sm);
}
.nav-link:hover {
  color: white;
  transform: translateY(-1px);
}
.nav-link:hover::before {
  opacity: 0.1;
  transform: translateY(0);
}
.nav-link.active {
  color: white;
  background: rgba(79, 70, 229, 0.2);
  border: 1px solid rgba(79, 70, 229, 0.3);
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.15);
}
.nav-icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}
.unread-badge {
  position: absolute;
  top: 2px;
  right: 2px;
  background: var(--danger);
  color: white;
  border-radius: 50%;
  min-width: 18px;
  height: 18px;
  font-size: 10px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
  box-shadow: 0 0 0 2px var(--gray-900);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
}
.user-name {
  color: var(--gray-300);
  font-size: 14px;
  font-weight: 500;
}
.logout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(239, 68, 68, 0.15);
  color: #fca5a5;
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: var(--radius-sm);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s var(--ease);
}
.logout-btn:hover {
  background: var(--danger);
  color: white;
  border-color: var(--danger);
}
</style>
