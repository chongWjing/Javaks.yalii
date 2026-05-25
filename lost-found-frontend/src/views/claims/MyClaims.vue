<template>
  <div class="claims-container">
    <div class="page-header">
      <h2>我的认领记录</h2>
      <p>查看你提交的所有认领申请</p>
    </div>
    <div class="table-wrapper">
      <el-table :data="claims" v-loading="loading" style="width: 100%" empty-text="暂无认领记录" :header-cell-style="{ background: '#f8fafc', color: '#475569', fontWeight: '600' }">
        <el-table-column prop="itemName" label="物品名称" min-width="140">
          <template #default="{ row }">
            <span class="item-name">{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="claimReason" label="认领理由" show-overflow-tooltip min-width="200" />
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" round>{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="claimTime" label="认领时间" width="180">
          <template #default="{ row }">{{ formatDate(row.claimTime) }}</template>
        </el-table-column>
        <el-table-column prop="processTime" label="处理时间" width="180">
          <template #default="{ row }">
            <span v-if="row.processTime">{{ formatDate(row.processTime) }}</span>
            <span v-else class="pending-text">待处理</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status === 'PENDING'" type="danger" size="small" @click="handleDelete(row.id)" class="action-btn">
              撤回
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div v-if="claims.length > 0" class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        :total="claims.length"
        layout="total, sizes, prev, pager, next"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../../store/auth'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../../services/api'

const authStore = useAuthStore()
const claims = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

const getStatusType = (s) => ({ PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }[s] || 'info')
const getStatusText = (s) => ({ PENDING: '待处理', APPROVED: '已批准', REJECTED: '已拒绝' }[s] || s)
const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : ''

const fetchClaims = async () => {
  loading.value = true
  try {
    const response = await api.get(`/claims/user/${authStore.user.id}`)
    if (response.data.success) claims.value = response.data.data
  } finally { loading.value = false }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要撤回此认领申请吗？', '确认撤回', {
      confirmButtonText: '确定撤回',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await api.delete(`/claims/${id}`)
    if (response.data.success) {
      ElMessage.success('认领申请已撤回')
      fetchClaims()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || '撤回失败')
    }
  }
}

onMounted(() => { fetchClaims() })
</script>

<style scoped>
.claims-container { padding: 32px; max-width: 1100px; margin: 0 auto; animation: fadeIn 0.4s var(--ease); }
.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 28px; font-weight: 700; color: var(--gray-900); margin: 0 0 4px; }
.page-header p { color: var(--gray-500); font-size: 15px; margin: 0; }
.table-wrapper { background: white; border-radius: var(--radius-lg); box-shadow: var(--shadow); overflow: hidden; }
.item-name { font-weight: 600; color: var(--gray-800); }
.pending-text { color: var(--gray-400); font-style: italic; }
.action-btn { display: inline-flex !important; align-items: center; gap: 4px; }
.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: center;
  padding: 20px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
}
</style>
