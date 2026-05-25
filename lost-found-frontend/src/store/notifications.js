import { defineStore } from 'pinia'
import api from '../services/api'

export const useNotificationStore = defineStore('notifications', {
  state: () => ({
    notifications: [],
    unreadCount: 0,
    loading: false
  }),

  actions: {
    async fetchNotifications(userId) {
      this.loading = true
      try {
        const response = await api.get(`/notifications/user/${userId}`)
        if (response.data.success) {
          this.notifications = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async fetchUnreadCount(userId) {
      try {
        const response = await api.get(`/notifications/user/${userId}/unread-count`)
        if (response.data.success) {
          this.unreadCount = response.data.data.count
        }
      } catch (error) {
        console.error('Failed to fetch unread count:', error)
      }
    },

    async markAsRead(id) {
      const response = await api.put(`/notifications/${id}/read`)
      return response.data
    },

    async markAllAsRead(userId) {
      const response = await api.put(`/notifications/user/${userId}/read-all`)
      return response.data
    },

    async deleteNotification(id) {
      const response = await api.delete(`/notifications/${id}`)
      return response.data
    }
  }
})
