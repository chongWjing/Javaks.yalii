import { defineStore } from 'pinia'
import api from '../services/api'

export const useItemStore = defineStore('items', {
  state: () => ({
    items: [],
    currentItem: null,
    myItems: [],
    loading: false
  }),

  actions: {
    async fetchItems(type = null, status = null, keyword = null, category = null) {
      this.loading = true
      try {
        const params = new URLSearchParams()
        if (type) params.append('type', type)
        if (status) params.append('status', status)
        if (keyword) params.append('keyword', keyword)
        if (category) params.append('category', category)

        const response = await api.get('/items', { params })
        if (response.data.success) {
          this.items = response.data.data
        }
      } finally {
        this.loading = false
      }
    },

    async fetchItem(id) {
      const response = await api.get(`/items/${id}`)
      if (response.data.success) {
        this.currentItem = response.data.data
      }
      return response.data
    },

    async createItem(itemData) {
      const response = await api.post('/items', itemData)
      return response.data
    },

    async updateItem(id, itemData) {
      const response = await api.put(`/items/${id}`, itemData)
      return response.data
    },

    async deleteItem(id) {
      const response = await api.delete(`/items/${id}`)
      return response.data
    },

    async fetchMyItems(userId) {
      const response = await api.get(`/items/user/${userId}`)
      if (response.data.success) {
        this.myItems = response.data.data
      }
      return response.data
    }
  }
})
