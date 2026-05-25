import { defineStore } from 'pinia'
import api from '../services/api'

export const useStatsStore = defineStore('stats', {
  state: () => ({
    overview: {},
    topLocations: [],
    monthlyTrend: [],
    avgRecovery: { days: 0, hours: 0 },
    loading: false
  }),

  actions: {
    async fetchOverview() {
      try {
        const response = await api.get('/stats')
        if (response.data.success) {
          this.overview = response.data.data
        }
      } catch (error) {
        console.error('Failed to fetch stats overview:', error)
      }
    },

    async fetchTopLocations(limit = 10) {
      try {
        const response = await api.get(`/items/stats/top-locations?limit=${limit}`)
        if (response.data.success) {
          this.topLocations = response.data.data
        }
      } catch (error) {
        console.error('Failed to fetch top locations:', error)
      }
    },

    async fetchMonthlyTrend() {
      try {
        const response = await api.get('/items/stats/monthly-trend')
        if (response.data.success) {
          this.monthlyTrend = response.data.data
        }
      } catch (error) {
        console.error('Failed to fetch monthly trend:', error)
      }
    },

    async fetchAvgRecovery() {
      try {
        const response = await api.get('/items/stats/avg-recovery')
        if (response.data.success) {
          this.avgRecovery = response.data.data
        }
      } catch (error) {
        console.error('Failed to fetch avg recovery:', error)
      }
    },

    async fetchAll() {
      this.loading = true
      try {
        await Promise.all([
          this.fetchOverview(),
          this.fetchTopLocations(),
          this.fetchMonthlyTrend(),
          this.fetchAvgRecovery()
        ])
      } finally {
        this.loading = false
      }
    }
  }
})
