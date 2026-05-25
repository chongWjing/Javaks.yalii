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
        <h2>创建账号</h2>
        <p class="auth-subtitle">注册后即可发布和认领物品</p>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="0" size="large">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名 (3-50字符)" prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码 (至少6位)" prefix-icon="Lock" show-password />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" show-password />
          </el-form-item>
          <el-form-item prop="phone">
            <el-input v-model="form.phone" placeholder="手机号 (可选)" prefix-icon="Phone" />
          </el-form-item>
          <el-form-item prop="email">
            <el-input v-model="form.email" placeholder="邮箱 (可选)" prefix-icon="Message" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleRegister" :loading="loading" class="auth-btn">
              注 册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="auth-footer">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../store/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref(null)
const loading = ref(false)

const form = ref({ username: '', password: '', confirmPassword: '', phone: '', email: '' })

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度为3-50字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.value.password) callback(new Error('两次输入的密码不一致'))
        else callback()
      },
      trigger: 'blur'
    }
  ]
}

const handleRegister = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    const response = await authStore.register({
      username: form.value.username,
      password: form.value.password,
      phone: form.value.phone,
      email: form.value.email
    })
    if (response.success) {
      ElMessage.success('注册成功，请登录')
      router.push('/login')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
}
.auth-left {
  flex: 1;
  background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 50%, #06b6d4 100%);
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
.auth-brand h1 { font-size: 36px; font-weight: 800; margin: 0 0 12px; letter-spacing: 1px; }
.auth-brand p { font-size: 18px; opacity: 0.85; margin: 0; }
.floating-shapes { position: absolute; inset: 0; pointer-events: none; }
.shape { position: absolute; border-radius: 50%; background: rgba(255, 255, 255, 0.06); }
.shape-1 { width: 300px; height: 300px; top: -50px; left: -80px; animation: float 6s ease-in-out infinite; }
.shape-2 { width: 200px; height: 200px; bottom: 50px; right: -40px; animation: float 8s ease-in-out infinite 1s; }
.shape-3 { width: 120px; height: 120px; top: 40%; left: 20%; animation: float 5s ease-in-out infinite 0.5s; }

.auth-right {
  width: 480px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  padding: 40px;
  overflow-y: auto;
}
.auth-card {
  width: 100%;
  max-width: 360px;
  animation: fadeInUp 0.6s var(--ease) 0.2s both;
}
.auth-card h2 { font-size: 28px; font-weight: 700; color: var(--gray-900); margin: 0 0 8px; }
.auth-subtitle { color: var(--gray-500); margin: 0 0 28px; font-size: 15px; }
.auth-btn {
  width: 100%;
  height: 48px !important;
  font-size: 16px !important;
  border-radius: var(--radius) !important;
  margin-top: 8px;
}
.auth-footer { text-align: center; margin-top: 24px; color: var(--gray-500); font-size: 14px; }
.auth-footer a { font-weight: 600; }

@media (max-width: 768px) {
  .auth-left { display: none; }
  .auth-right { width: 100%; }
}
</style>
