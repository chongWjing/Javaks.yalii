<template>
  <div class="edit-container">
    <div class="edit-card" style="animation: fadeInUp 0.4s var(--ease)">
      <div class="card-header">
        <h2>编辑物品信息</h2>
        <p>修改物品的详细信息</p>
      </div>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="0" size="large" class="edit-form" v-loading="loading">
        <el-form-item prop="name">
          <el-input v-model="form.name" placeholder="物品名称" />
        </el-form-item>
        <el-form-item prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="详细描述物品特征" />
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

        <div v-if="form.itemType === 'LOST'" class="type-fields">
          <el-form-item>
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
          <el-form-item>
            <el-input v-model="form.foundTime" placeholder="拾到时间" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="form.statusDescription" placeholder="物品当前状态" />
          </el-form-item>
        </div>

        <div class="form-actions">
          <el-button @click="$router.back()" size="large">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting" size="large" class="submit-btn">
            保存修改
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useItemStore } from '../../store/items'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const itemStore = useItemStore()
const formRef = ref(null)
const loading = ref(false)
const submitting = ref(false)

const form = ref({
  name: '', description: '', location: '', category: '',
  itemType: '', lostTime: '', reward: 0, foundTime: '', statusDescription: ''
})

const rules = {
  name: [{ required: true, message: '请输入物品名称', trigger: 'blur' }],
  location: [{ required: true, message: '请输入位置信息', trigger: 'blur' }]
}

const fetchItem = async () => {
  loading.value = true
  try {
    const response = await itemStore.fetchItem(route.params.id)
    if (response.success) {
      const item = itemStore.currentItem
      form.value = {
        name: item.name || '',
        description: item.description || '',
        location: item.location || '',
        category: item.category || '',
        itemType: item.itemType || '',
        lostTime: item.lostTime || '',
        reward: item.reward || 0,
        foundTime: item.foundTime || '',
        statusDescription: item.statusDescription || ''
      }
    }
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    const response = await itemStore.updateItem(route.params.id, form.value)
    if (response.success) {
      ElMessage.success('物品信息更新成功')
      router.push(`/items/${route.params.id}`)
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '更新失败')
  } finally {
    submitting.value = false
  }
}

onMounted(() => { fetchItem() })
</script>

<style scoped>
.edit-container { padding: 32px; max-width: 640px; margin: 0 auto; }
.edit-card {
  background: white; border-radius: var(--radius-lg); box-shadow: var(--shadow); overflow: hidden;
}
.card-header { padding: 32px 32px 0; }
.card-header h2 { font-size: 24px; font-weight: 700; color: var(--gray-900); margin: 0 0 4px; }
.card-header p { color: var(--gray-500); font-size: 15px; margin: 0; }

.edit-form { padding: 24px 32px 32px; }
.reward-field { display: flex; align-items: center; gap: 12px; }
.reward-label { font-size: 14px; color: var(--gray-600); white-space: nowrap; }
.reward-unit { font-size: 14px; color: var(--gray-500); }

.form-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 8px; }
.submit-btn {
  display: flex !important; align-items: center; gap: 8px;
  height: 48px !important; padding: 0 28px !important; font-size: 16px !important;
  border-radius: var(--radius) !important;
}

@media (max-width: 768px) {
  .edit-container { padding: 16px; }
}
</style>
