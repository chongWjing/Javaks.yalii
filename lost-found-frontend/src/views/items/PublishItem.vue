<template>
  <div class="publish-container">
    <div class="publish-card" style="animation: fadeInUp 0.4s var(--ease)">
      <div class="card-header">
        <h2>发布物品信息</h2>
        <p>填写以下信息发布失物或招领信息</p>
      </div>

      <div class="type-selector">
        <div class="type-option" :class="{ active: form.itemType === 'LOST' }" @click="form.itemType = 'LOST'">
          <div class="type-icon" style="background: linear-gradient(135deg, #ef4444, #f97316);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="22" height="22"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>
          </div>
          <div>
            <span class="type-label">失物</span>
            <span class="type-desc">我丢失了物品</span>
          </div>
        </div>
        <div class="type-option" :class="{ active: form.itemType === 'FOUND' }" @click="form.itemType = 'FOUND'">
          <div class="type-icon" style="background: linear-gradient(135deg, #10b981, #06b6d4);">
            <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="22" height="22"><path d="M22 11.08V12a10 10 0 11-5.93-9.14"/><polyline points="22,4 12,14.01 9,11.01"/></svg>
          </div>
          <div>
            <span class="type-label">招领</span>
            <span class="type-desc">我拾到了物品</span>
          </div>
        </div>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="0" size="large" class="publish-form">
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="物品名称" />
        </el-form-item>
        <el-form-item prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="详细描述物品特征，越详细越容易匹配" />
        </el-form-item>
        <el-form-item prop="location">
          <el-input v-model="form.location" placeholder="发现/丢失位置" />
        </el-form-item>
        <el-form-item prop="category">
          <el-select v-model="form.category" placeholder="物品类别" style="width: 100%">
            <el-option label="证件" value="证件" />
            <el-option label="电子设备" value="电子" />
            <el-option label="日用品" value="日用" />
            <el-option label="书籍文具" value="书籍" />
            <el-option label="衣物饰品" value="衣物" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-upload
            v-model:file-list="uploadFiles"
            list-type="picture-card"
            :auto-upload="false"
            :limit="6"
            accept="image/*"
            :before-upload="beforeUpload"
            @exceed="handleExceed"
          >
            <div class="upload-card">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="22" height="22">
                <path d="M12 5v14M5 12h14" />
              </svg>
              <span>上传图片</span>
            </div>
          </el-upload>
          <div class="upload-hint">最多 6 张，单张不超过 5MB</div>
        </el-form-item>

        <div v-if="form.itemType === 'LOST'" class="type-fields">
          <el-form-item prop="lostTime">
            <el-input v-model="form.lostTime" placeholder="丢失时间" />
          </el-form-item>
          <el-form-item>
            <div class="reward-field">
              <span class="reward-label">悬赏金额</span>
              <el-input-number v-model="form.reward" :min="0" :step="10" />
              <span class="reward-unit">元</span>
            </div>
          </el-form-item>
        </div>

        <div v-if="form.itemType === 'FOUND'" class="type-fields">
          <el-form-item prop="foundTime">
            <el-input v-model="form.foundTime" placeholder="拾到时间" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.statusDescription" placeholder="物品当前状态（如：完好/有损）" />
          </el-form-item>
        </div>

        <div class="form-actions">
          <el-button @click="$router.back()" size="large">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="loading" size="large" class="submit-btn">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22,2 15,22 11,13 2,9"/></svg>
            发布信息
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useItemStore } from '../../store/items'
import { ElMessage } from 'element-plus'

const router = useRouter()
const itemStore = useItemStore()
const formRef = ref(null)
const loading = ref(false)
const uploadFiles = ref([])

const form = ref({
  itemType: 'LOST', name: '', description: '', location: '', category: '',
  lostTime: '', reward: 0, foundTime: '', statusDescription: '', imageUrls: []
})

const rules = {
  itemType: [{ required: true, message: '请选择物品类型', trigger: 'change' }],
  name: [{ required: true, message: '请输入物品名称', trigger: 'blur' }],
  location: [{ required: true, message: '请输入位置信息', trigger: 'blur' }]
}

const beforeUpload = (file) => {
  if (!file.type.startsWith('image/')) {
    ElMessage.error('仅支持图片格式')
    return false
  }
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB')
    return false
  }
  return true
}

const handleExceed = () => {
  ElMessage.warning('最多上传 6 张图片')
}

const uploadImages = async () => {
  const files = uploadFiles.value.map(file => file.raw).filter(Boolean)
  if (files.length === 0) return []
  const response = await itemStore.uploadImages(files)
  if (!response.success) {
    throw new Error(response.message || '图片上传失败')
  }
  return response.data
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    const imageUrls = await uploadImages()
    const payload = { ...form.value, imageUrls }
    const response = await itemStore.createItem(payload)
    if (response.success) {
      ElMessage.success('发布成功')
      router.push('/items')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || error.message || '发布失败')
  } finally { loading.value = false }
}
</script>

<style scoped>
.publish-container { padding: 32px; max-width: 640px; margin: 0 auto; }
.publish-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-xl);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}
.card-header {
  padding: 40px 40px 0;
  text-align: center;
}
.card-header h2 { font-size: 28px; font-weight: 800; color: var(--gray-900); margin: 0 0 8px; letter-spacing: -0.5px; }
.card-header p { color: var(--gray-500); font-size: 16px; margin: 0; }

.type-selector {
  display: grid; grid-template-columns: 1fr 1fr; gap: 16px;
  padding: 32px 40px 0;
}
.type-option {
  display: flex; align-items: center; gap: 16px;
  padding: 20px; border-radius: var(--radius-lg);
  border: 2px solid rgba(255, 255, 255, 0.5);
  background: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 4px 15px rgba(0,0,0,0.02);
}
.type-option:hover { transform: translateY(-3px); background: rgba(255, 255, 255, 0.9); box-shadow: 0 8px 20px rgba(0,0,0,0.05); }
.type-option.active { border-color: var(--primary); background: linear-gradient(135deg, rgba(238, 242, 255, 0.9), rgba(224, 231, 255, 0.8)); box-shadow: 0 8px 25px rgba(79, 70, 229, 0.15); }
.type-icon {
  width: 48px; height: 48px; border-radius: 14px;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.type-label { display: block; font-size: 16px; font-weight: 700; color: var(--gray-800); }
.type-desc { display: block; font-size: 13px; color: var(--gray-500); margin-top: 4px; }

.publish-form { padding: 32px 40px 40px; }
.reward-field {
  display: flex; align-items: center; gap: 12px;
  background: rgba(255, 255, 255, 0.5);
  padding: 12px 16px; border-radius: var(--radius-sm); border: 1px solid rgba(255,255,255,0.8);
}
.reward-label { font-size: 14px; color: var(--gray-700); font-weight: 600; white-space: nowrap; }
.reward-unit { font-size: 14px; color: var(--gray-500); }

.upload-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  color: var(--gray-500);
  font-size: 12px;
}
.upload-hint { font-size: 12px; color: var(--gray-500); margin-top: 6px; }

.form-actions {
  display: flex; justify-content: flex-end; gap: 16px; margin-top: 16px;
  padding-top: 24px; border-top: 1px solid rgba(0,0,0,0.05);
}
.submit-btn {
  display: flex !important; align-items: center; gap: 8px;
  height: 52px !important; padding: 0 32px !important; font-size: 16px !important;
  border-radius: var(--radius-lg) !important;
}

@media (max-width: 768px) {
  .publish-container { padding: 16px; }
  .type-selector { grid-template-columns: 1fr; }
}
</style>
