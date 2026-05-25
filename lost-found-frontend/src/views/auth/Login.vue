<template>
  <div class="auth-page">
    <div class="auth-left">
      <div class="auth-brand">
        <span class="brand-icon">&#128269;</span>
        <h1>失物招领系统</h1>
        <p>让失物回家，让善意传递</p>
      </div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>
    <div class="auth-right">
      <div class="auth-card">
        <h2>欢迎回来</h2>
        <p class="auth-subtitle">登录你的账号继续使用</p>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="0" size="large">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleLogin" :loading="loading" class="auth-btn">
              登 录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="auth-footer">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../../store/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const formRef = ref(null)
const loading = ref(false)

const form = ref({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    await authStore.login(form.value.username, form.value.password)
    ElMessage.success('登录成功')
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  background-image: radial-gradient(at 0% 0%, hsla(253,16%,7%,1) 0, transparent 50%), radial-gradient(at 50% 0%, hsla(225,39%,30%,1) 0, transparent 50%), radial-gradient(at 100% 0%, hsla(339,49%,30%,1) 0, transparent 50%);
}
.auth-left {
  flex: 1;
  background: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}
.auth-brand {
  text-align: center;
  color: white;
  z-index: 1;
  animation: fadeInUp 0.6s var(--ease);
}
.brand-icon {
  font-size: 72px;
  display: block;
  margin-bottom: 20px;
  filter: drop-shadow(0 4px 20px rgba(0, 0, 0, 0.3));
  animation: float 3s ease-in-out infinite;
}
.auth-brand h1 {
  font-size: 42px;
  font-weight: 800;
  margin: 0 0 16px;
  letter-spacing: -1px;
}
.auth-brand p {
  font-size: 20px;
  opacity: 0.9;
  margin: 0;
}
.floating-shapes { position: absolute; inset: 0; pointer-events: none; }
.shape {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.02));
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.05);
}
.shape-1 { width: 300px; height: 300px; top: 10%; left: 10%; animation: float 6s ease-in-out infinite; }
.shape-2 { width: 200px; height: 200px; bottom: 15%; right: 15%; animation: float 8s ease-in-out infinite reverse; }
.shape-3 { width: 120px; height: 120px; top: 50%; left: 30%; animation: pulse 5s ease-in-out infinite; }

.auth-right {
  width: 480px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-left: 1px solid rgba(255, 255, 255, 0.2);
  padding: 40px;
}
.auth-card {
  width: 100%;
  max-width: 380px;
  animation: slideInLeft 0.6s var(--ease) 0.2s both;
}
.auth-card h2 {
  font-size: 32px;
  font-weight: 800;
  color: var(--gray-900);
  margin: 0 0 12px;
  letter-spacing: -0.5px;
}
.auth-subtitle {
  color: var(--gray-500);
  margin: 0 0 40px;
  font-size: 16px;
}
.auth-btn {
  width: 100%;
  height: 52px !important;
  font-size: 16px !important;
  border-radius: var(--radius-lg) !important;
  margin-top: 16px;
  font-weight: 700 !important;
}
.auth-footer {
  text-align: center;
  margin-top: 32px;
  color: var(--gray-600);
  font-size: 14px;
}
.auth-footer a {
  font-weight: 600;
  color: var(--primary);
  margin-left: 4px;
}

@media (max-width: 768px) {
  .auth-left { display: none; }
  .auth-right { width: 100%; background: transparent; }
  .auth-card { background: rgba(255, 255, 255, 0.9); backdrop-filter: blur(10px); padding: 32px; border-radius: var(--radius-xl); border: 1px solid rgba(255,255,255,0.5); }
}
</style>
