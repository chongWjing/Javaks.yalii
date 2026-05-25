import { defineStore } from 'pinia'
import api from '../services/api'
import router from '../router'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token')
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'ADMIN'
  },

  actions: {
    async login(username, password) {
      const response = await api.post('/auth/login', { username, password })
      if (response.data.success) {
        this.token = response.data.data.token
        this.user = response.data.data.user
        localStorage.setItem('token', this.token)
        api.defaults.headers.common['Authorization'] = `Bearer ${this.token}`
        router.push('/')
      }
      return response.data
    },

    async register(userData) {
      const response = await api.post('/auth/register', userData)
      return response.data
    },

    async fetchProfile() {
      try {
        const response = await api.get('/auth/me')
        if (response.data.success) {
          this.user = response.data.data
        }
      } catch (error) {
        this.logout()
      }
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('token')
      delete api.defaults.headers.common['Authorization']
      router.push('/login')
    }
  }
})
