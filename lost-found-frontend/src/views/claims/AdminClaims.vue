<template>
  <div class="claims-container">
    <div class="page-header">
      <h2>认领管理</h2>
      <p>审核和管理所有认领申请</p>
    </div>
    <div class="table-wrapper">
      <el-table :data="claims" v-loading="loading" style="width: 100%" empty-text="暂无认领申请" :header-cell-style="{ background: '#f8fafc', color: '#475569', fontWeight: '600' }">
        <el-table-column prop="itemName" label="物品名称" min-width="140">
          <template #default="{ row }">
            <span class="item-name">{{ row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="claimerName" label="认领者" width="120">
          <template #default="{ row }">
            <div class="user-cell">
              <div class="mini-avatar">{{ row.claimerName?.charAt(0)?.toUpperCase() }}</div>
              {{ row.claimerName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="claimReason" label="认领理由" show-overflow-tooltip min-width="180" />
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" round>{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="claimTime" label="认领时间" width="170">
          <template #default="{ row }">{{ formatDate(row.claimTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="240" align="center">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING'">
              <el-button type="success" size="small" @click="approveClaim(row.id)" class="action-btn">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><polyline points="20,6 9,17 4,12"/></svg>
                批准
              </el-button>
              <el-button type="danger" size="small" @click="rejectClaim(row.id)" class="action-btn">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                拒绝
              </el-button>
            </template>
            <span v-else class="processed-text">已处理</span>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../../services/api'

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
    const response = await api.get('/claims')
    if (response.data.success) claims.value = response.data.data
  } finally { loading.value = false }
}

const approveClaim = async (id) => {
  try {
    await api.put(`/claims/${id}/approve`)
    ElMessage.success('已批准')
    fetchClaims()
  } catch (error) { ElMessage.error('操作失败') }
}

const rejectClaim = async (id) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入拒绝原因（可选）', '拒绝认领', {
      confirmButtonText: '确定拒绝',
      cancelButtonText: '取消',
      inputType: 'textarea',
      inputPlaceholder: '请输入拒绝原因...'
    })
    await api.put(`/claims/${id}/reject`, { rejectReason: value || '' })
    ElMessage.success('已拒绝')
    fetchClaims()
  } catch (error) {
    if (error !== 'cancel' && error?.message !== 'cancel') {
      ElMessage.error('操作失败')
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
.user-cell { display: flex; align-items: center; gap: 8px; }
.mini-avatar {
  width: 28px; height: 28px; border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-lighter), var(--accent));
  color: white; display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 700; flex-shrink: 0;
}
.action-btn { display: inline-flex !important; align-items: center; gap: 4px; }
.processed-text { color: var(--gray-400); font-size: 13px; }
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
