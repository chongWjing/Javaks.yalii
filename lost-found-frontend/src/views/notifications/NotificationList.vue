<template>
  <div class="notifications-container">
    <div class="page-header">
      <div class="header-row">
        <div>
          <h2>通知消息</h2>
          <p>查看系统通知和认领动态</p>
        </div>
        <el-button v-if="notifications.length > 0" type="primary" plain @click="handleMarkAllRead" :loading="markingAll">
          全部标记已读
        </el-button>
      </div>
    </div>
    <div v-if="notifications.length > 0" class="notification-list">
      <div v-for="(notif, index) in notifications" :key="notif.id" class="notification-item" :class="{ unread: !notif.read }" :style="{ animationDelay: `${index * 0.05}s` }">
        <div class="notif-indicator" :class="{ unread: !notif.read }"></div>
        <div class="notif-body">
          <div class="notif-header">
            <span class="notif-title">{{ notif.title }}</span>
            <span class="notif-time">{{ formatDate(notif.createTime) }}</span>
          </div>
          <div class="notif-content">{{ notif.content }}</div>
          <div class="notif-actions">
            <el-button v-if="!notif.read" size="small" @click="markAsRead(notif.id)" class="action-btn">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><polyline points="20,6 9,17 4,12"/></svg>
              标记已读
            </el-button>
            <el-button size="small" @click="deleteNotification(notif.id)" class="delete-btn">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><polyline points="3,6 5,6 21,6"/><path d="M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/></svg>
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="empty-state">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="56" height="56"><path d="M18 8A6 6 0 006 8c0 7-3 9-3 9h18s-3-2-3-9"/><path d="M13.73 21a2 2 0 01-3.46 0"/></svg>
      </div>
      <p>暂无通知消息</p>
    </div>

    <div v-if="notifications.length > 0" class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        :total="notifications.length"
        layout="total, sizes, prev, pager, next"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../../store/auth'
import { ElMessage } from 'element-plus'
import api from '../../services/api'

const authStore = useAuthStore()
const notifications = ref([])
const markingAll = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : ''

const fetchNotifications = async () => {
  try {
    const response = await api.get(`/notifications/user/${authStore.user.id}`)
    if (response.data.success) notifications.value = response.data.data
  } catch (error) { console.error('Failed to fetch notifications:', error) }
}

const markAsRead = async (id) => {
  try {
    await api.put(`/notifications/${id}/read`)
    ElMessage.success('已标记为已读')
    fetchNotifications()
  } catch (error) { ElMessage.error('操作失败') }
}

const deleteNotification = async (id) => {
  try {
    await api.delete(`/notifications/${id}`)
    ElMessage.success('已删除')
    fetchNotifications()
  } catch (error) { ElMessage.error('删除失败') }
}

const handleMarkAllRead = async () => {
  markingAll.value = true
  try {
    await api.put(`/notifications/user/${authStore.user.id}/read-all`)
    ElMessage.success('全部标记已读')
    fetchNotifications()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    markingAll.value = false
  }
}

onMounted(() => { fetchNotifications() })
</script>

<style scoped>
.notifications-container { padding: 32px; max-width: 800px; margin: 0 auto; animation: fadeIn 0.4s var(--ease); }
.page-header { margin-bottom: 32px; }
.header-row { display: flex; justify-content: space-between; align-items: center; }
.page-header h2 { font-size: 32px; font-weight: 800; color: var(--gray-900); margin: 0 0 8px; letter-spacing: -0.5px; }
.page-header p { color: var(--gray-500); font-size: 16px; margin: 0; }

.notification-list { display: flex; flex-direction: column; gap: 16px; }
.notification-item {
  display: flex; gap: 16px; background: rgba(255, 255, 255, 0.85); padding: 24px;
  backdrop-filter: blur(12px); border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-xl); box-shadow: 0 4px 15px rgba(0,0,0,0.03);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1); animation: fadeInUp 0.4s var(--ease) both;
}
.notification-item:hover { box-shadow: 0 12px 25px rgba(0,0,0,0.06); transform: translateY(-3px); background: white; }
.notification-item.unread { background: linear-gradient(135deg, rgba(240, 245, 255, 0.9), rgba(255, 255, 255, 0.8)); border: 1px solid rgba(191, 219, 254, 0.8); }

.notif-indicator {
  width: 6px; border-radius: 6px; flex-shrink: 0; background: var(--gray-200); margin: -4px 0;
}
.notif-indicator.unread { background: linear-gradient(to bottom, var(--primary), var(--accent)); box-shadow: 0 0 10px rgba(79, 70, 229, 0.3); }

.notif-body { flex: 1; }
.notif-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.notif-title { font-weight: 700; color: var(--gray-900); font-size: 17px; }
.notif-time { color: var(--gray-400); font-size: 13px; font-weight: 500; }
.notif-content { color: var(--gray-600); font-size: 15px; line-height: 1.6; margin-bottom: 16px; }
.notif-actions { display: flex; gap: 12px; }
.action-btn {
  display: inline-flex !important; align-items: center; gap: 6px;
  border-radius: var(--radius-sm) !important; font-weight: 600 !important;
  border-color: var(--primary-lighter) !important; color: var(--primary) !important; transition: all 0.2s;
}
.action-btn:hover { background: var(--primary) !important; color: white !important; box-shadow: 0 4px 12px rgba(79, 70, 229, 0.2); }
.delete-btn {
  display: inline-flex !important; align-items: center; gap: 6px;
  border-radius: var(--radius-sm) !important; transition: all 0.2s;
}
.delete-btn:hover { color: var(--danger) !important; border-color: rgba(239, 68, 68, 0.3) !important; background: rgba(239, 68, 68, 0.05) !important; }

.empty-state {
  text-align: center; padding: 100px 40px; background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(12px);
  border-radius: var(--radius-xl); box-shadow: 0 10px 30px rgba(0,0,0,0.03); border: 1px solid rgba(255, 255, 255, 0.5);
}
.empty-icon { color: var(--gray-300); margin-bottom: 16px; }
.empty-state p { font-size: 16px; color: var(--gray-500); margin: 0; }

.pagination-wrapper {
  margin-top: 32px;
  display: flex;
  justify-content: center;
  padding: 24px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-xl);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.03);
}
</style>
