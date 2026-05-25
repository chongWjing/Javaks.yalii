<template>
  <div id="app">
    <AppHeader v-if="isAuthenticated" />
    <main class="app-main" :class="{ 'no-header': !isAuthenticated }">
      <router-view />
    </main>
    <AppFooter v-if="isAuthenticated" />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useAuthStore } from './store/auth'
import AppHeader from './components/layout/AppHeader.vue'
import AppFooter from './components/layout/AppFooter.vue'

const authStore = useAuthStore()
const isAuthenticated = computed(() => authStore.isAuthenticated)

onMounted(() => {
  if (authStore.isAuthenticated) {
    authStore.fetchProfile()
  }
})
</script>

<style>
.app-main {
  flex: 1;
  padding-top: 0;
}
.app-main.no-header {
  min-height: 100vh;
}
</style>
