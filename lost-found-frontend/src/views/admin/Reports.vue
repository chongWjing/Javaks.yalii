<template>
  <div class="reports-container">
    <div class="page-header">
      <h2>举报管理</h2>
      <p>处理用户举报的物品信息</p>
    </div>
    <div class="table-wrapper">
      <el-table :data="reports" v-loading="loading" style="width: 100%" empty-text="暂无举报" :header-cell-style="{ background: '#f8fafc', color: '#475569', fontWeight: '600' }">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="物品" min-width="140">
          <template #default="{ row }">
            <span class="item-name">{{ row.item?.name || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="举报者" width="120">
          <template #default="{ row }">
            <div class="user-cell">
              <div class="mini-avatar">{{ row.reporter?.username?.charAt(0)?.toUpperCase() }}</div>
              {{ row.reporter?.username || '-' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="reportType" label="类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getReportTypeTag(row.reportType)" round>{{ getReportTypeText(row.reportType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="举报原因" show-overflow-tooltip min-width="180" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" round>{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="举报时间" width="170">
          <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <template v-if="row.status === 'PENDING'">
              <el-button type="success" size="small" @click="handleResolve(row)" class="action-btn">处理</el-button>
              <el-button type="info" size="small" @click="handleDismiss(row)" class="action-btn">驳回</el-button>
            </template>
            <span v-else class="processed-text">已处理</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="showDialog" :title="dialogTitle" width="500px" :close-on-click-modal="false">
      <el-input v-model="adminNote" type="textarea" :rows="3" placeholder="请输入处理备注（可选）" />
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button :type="dialogAction === 'resolve' ? 'success' : 'info'" @click="confirmAction" :loading="processing">
          {{ dialogAction === 'resolve' ? '确认处理' : '确认驳回' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useReportStore } from '../../store/reports'

const reportStore = useReportStore()
const reports = ref([])
const loading = ref(false)
const showDialog = ref(false)
const processing = ref(false)
const currentReportId = ref(null)
const dialogAction = ref('')
const dialogTitle = ref('')
const adminNote = ref('')

const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : ''
const getReportTypeTag = (t) => ({ FAKE: 'danger', DUPLICATE: 'warning', INAPPROPRIATE: 'danger', OTHER: 'info' }[t] || 'info')
const getReportTypeText = (t) => ({ FAKE: '虚假信息', DUPLICATE: '重复发布', INAPPROPRIATE: '不当内容', OTHER: '其他' }[t] || t)
const getStatusType = (s) => ({ PENDING: 'warning', RESOLVED: 'success', DISMISSED: 'info' }[s] || 'info')
const getStatusText = (s) => ({ PENDING: '待处理', RESOLVED: '已处理', DISMISSED: '已驳回' }[s] || s)

const fetchReports = async () => {
  loading.value = true
  try {
    await reportStore.fetchAllReports()
    reports.value = reportStore.reports
  } finally { loading.value = false }
}

const handleResolve = (row) => {
  currentReportId.value = row.id
  dialogAction.value = 'resolve'
  dialogTitle.value = '处理举报'
  adminNote.value = ''
  showDialog.value = true
}

const handleDismiss = (row) => {
  currentReportId.value = row.id
  dialogAction.value = 'dismiss'
  dialogTitle.value = '驳回举报'
  adminNote.value = ''
  showDialog.value = true
}

const confirmAction = async () => {
  processing.value = true
  try {
    if (dialogAction.value === 'resolve') {
      await reportStore.resolveReport(currentReportId.value, adminNote.value)
    } else {
      await reportStore.dismissReport(currentReportId.value, adminNote.value)
    }
    ElMessage.success(dialogAction.value === 'resolve' ? '举报已处理' : '举报已驳回')
    showDialog.value = false
    fetchReports()
  } catch (error) {
    ElMessage.error('操作失败')
  } finally { processing.value = false }
}

onMounted(() => { fetchReports() })
</script>

<style scoped>
.reports-container { padding: 32px; max-width: 1200px; margin: 0 auto; animation: fadeIn 0.4s var(--ease); }
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
</style>
