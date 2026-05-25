import { defineStore } from 'pinia'
import api from '../services/api'

export const useReportStore = defineStore('reports', {
  state: () => ({
    reports: [],
    pendingReports: [],
    loading: false
  }),

  actions: {
    async fetchAllReports() {
      this.loading = true
      try {
        const response = await api.get('/reports')
        if (response.data.success) {
          this.reports = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async fetchPendingReports() {
      this.loading = true
      try {
        const response = await api.get('/reports/pending')
        if (response.data.success) {
          this.pendingReports = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async createReport(reportData) {
      const response = await api.post('/reports', reportData)
      return response.data
    },

    async resolveReport(id, adminNote = '') {
      const response = await api.put(`/reports/${id}/resolve`, { adminNote })
      return response.data
    },

    async dismissReport(id, adminNote = '') {
      const response = await api.put(`/reports/${id}/dismiss`, { adminNote })
      return response.data
    }
  }
})
