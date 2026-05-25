<template>
  <div class="home-container">
    <div class="hero-section">
      <div class="hero-content">
        <div class="hero-badge">Lost & Found</div>
        <h1>欢迎来到失物招领系统</h1>
        <p>帮助丢失物品的人找到失物，让拾到物品的人物归原主</p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" @click="$router.push('/publish')" class="hero-btn-primary">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
            发布失物/招领
          </el-button>
          <el-button size="large" class="hero-btn-secondary" @click="$router.push('/items')">
            浏览所有信息
          </el-button>
        </div>
      </div>
      <div class="hero-decoration">
        <div class="deco-circle deco-1"></div>
        <div class="deco-circle deco-2"></div>
        <div class="deco-circle deco-3"></div>
      </div>
    </div>

    <div class="stats-section">
      <div class="stat-card" v-for="(stat, i) in statCards" :key="i" :style="{ animationDelay: `${i * 0.1}s` }">
        <div class="stat-icon" :style="{ background: stat.bg }">
          <span v-html="stat.icon"></span>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </div>

    <div class="recent-section" v-if="recentItems.length > 0">
      <h3>最新发布</h3>
      <div class="recent-grid">
        <div v-for="item in recentItems" :key="item.id" class="recent-card" @click="$router.push(`/items/${item.id}`)">
          <div class="recent-type" :class="item.itemType === 'LOST' ? 'lost' : 'found'">
            {{ item.itemType === 'LOST' ? '失物' : '招领' }}
          </div>
          <div class="recent-name">{{ item.name }}</div>
          <div class="recent-location">{{ item.location || '未指定位置' }}</div>
        </div>
      </div>
    </div>

    <div class="quick-actions">
      <h3>快捷操作</h3>
      <div class="actions-grid">
        <div class="action-card" @click="$router.push('/publish')">
          <div class="action-icon" style="background: linear-gradient(135deg, #ef4444, #f97316);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M21 15a2 2 0 01-2 2H7l-4 4V5a2 2 0 012-2h14a2 2 0 012 2z"/></svg>
          </div>
          <span>发布失物</span>
        </div>
        <div class="action-card" @click="$router.push('/publish')">
          <div class="action-icon" style="background: linear-gradient(135deg, #10b981, #06b6d4);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22,4 12,14.01 9,11.01"/></svg>
          </div>
          <span>发布招领</span>
        </div>
        <div class="action-card" @click="$router.push('/items')">
          <div class="action-icon" style="background: linear-gradient(135deg, #6366f1, #8b5cf6);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
          </div>
          <span>搜索物品</span>
        </div>
        <div class="action-card" @click="$router.push('/my-claims')">
          <div class="action-icon" style="background: linear-gradient(135deg, #f59e0b, #eab308);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z"/><polyline points="14,2 14,8 20,8"/></svg>
          </div>
          <span>我的认领</span>
        </div>
        <div v-if="authStore.isAdmin" class="action-card" @click="$router.push('/admin/claims')">
          <div class="action-icon" style="background: linear-gradient(135deg, #8b5cf6, #a78bfa);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
          </div>
          <span>认领管理</span>
        </div>
        <div v-if="authStore.isAdmin" class="action-card" @click="$router.push('/admin/users')">
          <div class="action-icon" style="background: linear-gradient(135deg, #ec4899, #f472b6);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4-4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>
          </div>
          <span>用户管理</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../../store/auth'
import api from '../../services/api'

const authStore = useAuthStore()

const stats = ref({})
const recentItems = ref([])

const statCards = computed(() => [
  { value: stats.value.totalUsers || 0, label: '注册用户', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4-4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 00-3-3.87"/><path d="M16 3.13a4 4 0 010 7.75"/></svg>', bg: 'linear-gradient(135deg, #6366f1, #818cf8)' },
  { value: stats.value.lostItems || 0, label: '失物信息', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>', bg: 'linear-gradient(135deg, #ef4444, #f97316)' },
  { value: stats.value.foundItems || 0, label: '招领信息', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22,4 12,14.01 9,11.01"/></svg>', bg: 'linear-gradient(135deg, #10b981, #06b6d4)' },
  { value: stats.value.pendingClaims || 0, label: '待处理认领', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/></svg>', bg: 'linear-gradient(135deg, #f59e0b, #fbbf24)' }
])

onMounted(async () => {
  try {
    const [statsRes, itemsRes] = await Promise.all([
      api.get('/stats'),
      api.get('/items')
    ])
    if (statsRes.data.success) {
      stats.value = statsRes.data.data
    }
    if (itemsRes.data.success) {
      recentItems.value = itemsRes.data.data.slice(0, 6)
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
})
</script>

<style scoped>
.home-container {
  padding: 32px;
  max-width: 1200px;
  margin: 0 auto;
  animation: fadeIn 0.4s var(--ease);
}
.hero-section {
  position: relative;
  text-align: center;
  padding: 80px 40px;
  background: linear-gradient(135deg, rgba(79, 70, 229, 0.9) 0%, rgba(124, 58, 237, 0.9) 50%, rgba(6, 182, 212, 0.9) 100%);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-xl);
  color: white;
  margin-bottom: 40px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(79, 70, 229, 0.2);
}
.hero-content { position: relative; z-index: 1; }
.hero-badge {
  display: inline-block;
  padding: 8px 20px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 30px;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 2px;
  text-transform: uppercase;
  margin-bottom: 24px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}
.hero-section h1 { font-size: 48px; font-weight: 800; margin: 0 0 16px; letter-spacing: -1px; text-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.hero-section p { font-size: 20px; opacity: 0.9; margin: 0 0 40px; font-weight: 300; }
.hero-buttons { display: flex; gap: 20px; justify-content: center; }
.hero-btn-primary {
  height: 54px !important;
  padding: 0 32px !important;
  font-size: 16px !important;
  background: white !important;
  color: var(--primary) !important;
  border: none !important;
  border-radius: var(--radius-lg) !important;
  font-weight: 700 !important;
  display: flex !important;
  align-items: center;
  gap: 10px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15) !important;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1) !important;
}
.hero-btn-primary:hover { transform: translateY(-4px) scale(1.02); box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2) !important; }
.hero-btn-secondary {
  height: 54px !important;
  padding: 0 32px !important;
  font-size: 16px !important;
  background: rgba(255, 255, 255, 0.1) !important;
  color: white !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: var(--radius-lg) !important;
  font-weight: 600 !important;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease !important;
}
.hero-btn-secondary:hover { background: rgba(255, 255, 255, 0.2) !important; transform: translateY(-2px); }
.hero-decoration { position: absolute; inset: 0; pointer-events: none; }
.deco-circle { position: absolute; border-radius: 50%; background: linear-gradient(135deg, rgba(255,255,255,0.1), rgba(255,255,255,0.02)); backdrop-filter: blur(5px); border: 1px solid rgba(255,255,255,0.05); }
.deco-1 { width: 400px; height: 400px; top: -150px; right: -100px; animation: float 6s ease-in-out infinite; }
.deco-2 { width: 250px; height: 250px; bottom: -80px; left: -50px; animation: float 8s ease-in-out infinite reverse; }
.deco-3 { width: 120px; height: 120px; top: 20%; right: 25%; animation: pulse 4s ease-in-out infinite; }

.stats-section {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}
.stat-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  padding: 24px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.03);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  animation: fadeInUp 0.5s var(--ease) both;
}
.stat-card:hover { transform: translateY(-5px); box-shadow: 0 12px 25px rgba(0,0,0,0.08); background: white; }
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.stat-number { font-size: 32px; font-weight: 800; color: var(--gray-900); line-height: 1; }
.stat-label { font-size: 14px; color: var(--gray-500); margin-top: 4px; }

.quick-actions h3 {
  font-size: 20px;
  font-weight: 700;
  color: var(--gray-800);
  margin: 0 0 20px;
}

.recent-section {
  margin-bottom: 32px;
}
.recent-section h3 {
  font-size: 20px;
  font-weight: 700;
  color: var(--gray-800);
  margin: 0 0 20px;
}
.recent-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}
.recent-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-lg);
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 4px 15px rgba(0,0,0,0.03);
}
.recent-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 25px rgba(0,0,0,0.08);
  background: white;
  border-color: var(--primary-lighter);
}
.recent-type {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  margin-bottom: 12px;
}
.recent-type.lost { background: #fef2f2; color: #dc2626; }
.recent-type.found { background: #ecfdf5; color: #059669; }
.recent-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-800);
  margin-bottom: 8px;
}
.recent-location {
  font-size: 13px;
  color: var(--gray-500);
}
.actions-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.action-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-lg);
  padding: 28px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 4px 15px rgba(0,0,0,0.03);
}
.action-card:hover { transform: translateY(-6px); box-shadow: 0 15px 30px rgba(0,0,0,0.08); background: white; }
.action-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 14px;
}
.action-card span { font-size: 15px; font-weight: 600; color: var(--gray-700); }

@media (max-width: 768px) {
  .home-container { padding: 16px; }
  .hero-section { padding: 40px 20px; }
  .hero-section h1 { font-size: 28px; }
  .stats-section { grid-template-columns: repeat(2, 1fr); }
  .recent-grid { grid-template-columns: 1fr; }
  .actions-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
