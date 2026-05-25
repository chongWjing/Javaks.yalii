import { defineStore } from 'pinia'
import api from '../services/api'

export const useClaimStore = defineStore('claims', {
  state: () => ({
    claims: [],
    myClaims: [],
    pendingClaims: [],
    loading: false
  }),

  actions: {
    async fetchAllClaims() {
      this.loading = true
      try {
        const response = await api.get('/claims')
        if (response.data.success) {
          this.claims = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async fetchMyClaims(userId) {
      this.loading = true
      try {
        const response = await api.get(`/claims/user/${userId}`)
        if (response.data.success) {
          this.myClaims = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async fetchPendingClaims() {
      this.loading = true
      try {
        const response = await api.get('/claims/pending')
        if (response.data.success) {
          this.pendingClaims = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async createClaim(claimData) {
      const response = await api.post('/claims', claimData)
      return response.data
    },

    async approveClaim(id) {
      const response = await api.put(`/claims/${id}/approve`)
      return response.data
    },

    async rejectClaim(id, rejectReason = null) {
      const response = await api.put(`/claims/${id}/reject`, { rejectReason })
      return response.data
    },

    async deleteClaim(id) {
      const response = await api.delete(`/claims/${id}`)
      return response.data
    }
  }
})
