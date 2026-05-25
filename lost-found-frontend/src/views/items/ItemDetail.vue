<template>
  <div class="detail-container" v-loading="loading">
    <div v-if="item" class="item-detail" style="animation: fadeInUp 0.4s var(--ease)">
      <div class="detail-header">
        <div class="header-top">
          <div class="item-type" :class="item.itemType === 'LOST' ? 'lost' : 'found'">
            {{ item.itemType === 'LOST' ? '失物' : '招领' }}
          </div>
          <span class="status-tag" :class="item.status">{{ statusMap[item.status] || item.status }}</span>
        </div>
        <h1>{{ item.name }}</h1>
        <div class="header-meta">
          <span class="meta-chip">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>
            {{ item.location || '未指定' }}
          </span>
          <span class="meta-chip">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
            {{ formatDate(item.time) }}
          </span>
          <span class="meta-chip" v-if="item.category">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M22 19a2 2 0 01-2 2H4a2 2 0 01-2-2V5a2 2 0 012-2h5l2 3h9a2 2 0 012 2z"/></svg>
            {{ item.category }}
          </span>
        </div>
      </div>

      <div class="detail-body">
        <div class="info-section" v-if="item.itemType === 'LOST'">
          <h3>失物信息</h3>
          <div class="info-cards">
            <div class="info-card">
              <span class="info-label">丢失时间</span>
              <span class="info-value">{{ item.lostTime || '未知' }}</span>
            </div>
            <div class="info-card highlight">
              <span class="info-label">悬赏金额</span>
              <span class="info-value reward">&yen;{{ item.reward || 0 }}</span>
            </div>
          </div>
        </div>

        <div class="info-section" v-if="item.itemType === 'FOUND'">
          <h3>招领信息</h3>
          <div class="info-cards">
            <div class="info-card">
              <span class="info-label">拾到时间</span>
              <span class="info-value">{{ item.foundTime || '未知' }}</span>
            </div>
            <div class="info-card">
              <span class="info-label">物品状态</span>
              <span class="info-value">{{ item.statusDescription || '未知' }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <h3>详细描述</h3>
          <div class="description-box">
            {{ item.description || '暂无详细描述' }}
          </div>
        </div>

        <div class="info-section">
          <h3>发布者信息</h3>
          <div class="publisher-card">
            <div class="publisher-avatar">{{ item.publisherName?.charAt(0)?.toUpperCase() }}</div>
            <div class="publisher-info">
              <span class="publisher-name">{{ item.publisherName }}</span>
              <span class="publisher-label">信息发布者</span>
            </div>
          </div>
        </div>
      </div>

      <div class="detail-actions">
        <el-button v-if="item.status === 'ACTIVE' && !isOwner" type="primary" size="large" @click="showClaimDialog = true" class="claim-btn">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="18" height="18"><polyline points="20,6 9,17 4,12"/></svg>
          我要认领
        </el-button>
        <el-button v-if="isOwner || isAdmin" type="warning" size="large" @click="$router.push(`/items/${item.id}/edit`)" class="edit-btn">
          编辑
        </el-button>
        <el-button v-if="isOwner || isAdmin" type="danger" size="large" @click="handleDelete" class="delete-btn-detail">
          删除
        </el-button>
        <el-button size="large" @click="$router.back()">返回列表</el-button>
      </div>
    </div>

    <el-dialog v-model="showClaimDialog" title="认领物品" width="500px" :close-on-click-modal="false">
      <el-form :model="claimForm" :rules="claimRules" ref="claimFormRef">
        <el-form-item label="认领理由" prop="claimReason">
          <el-input v-model="claimForm.claimReason" type="textarea" :rows="4" placeholder="请描述你为什么认为这是你的物品" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showClaimDialog = false">取消</el-button>
        <el-button type="primary" @click="submitClaim" :loading="claiming">提交认领</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useItemStore } from '../../store/items'
import { useAuthStore } from '../../store/auth'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../../services/api'

const route = useRoute()
const router = useRouter()
const itemStore = useItemStore()
const authStore = useAuthStore()
const item = ref(null)
const loading = ref(false)
const showClaimDialog = ref(false)
const claiming = ref(false)
const claimFormRef = ref(null)
const claimForm = ref({ claimReason: '' })
const claimRules = { claimReason: [{ required: true, message: '请填写认领理由', trigger: 'blur' }] }
const statusMap = { ACTIVE: '进行中', CLAIMED: '已认领', CLOSED: '已关闭' }
const isOwner = computed(() => item.value?.publisherName === authStore.user?.username)
const isAdmin = computed(() => authStore.user?.role === 'ADMIN')
const formatDate = (dateStr) => dateStr ? new Date(dateStr).toLocaleString('zh-CN') : '未知'

const fetchItem = async () => {
  loading.value = true
  try {
    await itemStore.fetchItem(route.params.id)
    item.value = itemStore.currentItem
  } finally { loading.value = false }
}

const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确定要删除此物品吗？此操作不可撤销。', '确认删除', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await itemStore.deleteItem(route.params.id)
    if (response.success) {
      ElMessage.success('物品删除成功')
      router.push('/items')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

const submitClaim = async () => {
  try {
    await claimFormRef.value.validate()
    claiming.value = true
    const response = await api.post('/claims', { itemId: item.value.id, claimReason: claimForm.value.claimReason })
    if (response.data.success) {
      ElMessage.success('认领申请已提交')
      showClaimDialog.value = false
      claimForm.value.claimReason = ''
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '提交失败')
  } finally { claiming.value = false }
}

onMounted(() => { fetchItem() })
</script>

<style scoped>
.detail-container { padding: 32px; max-width: 800px; margin: 0 auto; }
.item-detail { background: white; border-radius: var(--radius-lg); box-shadow: var(--shadow); overflow: hidden; }
.detail-header {
  padding: 32px;
  background: linear-gradient(135deg, #f8fafc, #eef2ff);
  border-bottom: 1px solid var(--gray-100);
}
.header-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.item-type { padding: 6px 16px; border-radius: 20px; font-size: 13px; font-weight: 700; }
.item-type.lost { background: #fef2f2; color: #dc2626; }
.item-type.found { background: #ecfdf5; color: #059669; }
.status-tag { padding: 6px 14px; border-radius: 20px; font-size: 13px; font-weight: 600; }
.status-tag.ACTIVE { background: #ecfdf5; color: #059669; }
.status-tag.CLAIMED { background: #fffbeb; color: #d97706; }
.status-tag.CLOSED { background: var(--gray-100); color: var(--gray-500); }
.detail-header h1 { font-size: 28px; font-weight: 800; color: var(--gray-900); margin: 0 0 16px; }
.header-meta { display: flex; flex-wrap: wrap; gap: 12px; }
.meta-chip {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 6px 14px; background: white; border-radius: 20px;
  font-size: 13px; color: var(--gray-600);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.detail-body { padding: 32px; }
.info-section { margin-bottom: 28px; }
.info-section:last-child { margin-bottom: 0; }
.info-section h3 {
  font-size: 16px; font-weight: 700; color: var(--gray-800);
  margin: 0 0 16px; padding-bottom: 8px; border-bottom: 2px solid var(--primary-bg);
}
.info-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 12px; }
.info-card {
  padding: 16px; background: var(--gray-50); border-radius: var(--radius);
  border: 1px solid var(--gray-100);
}
.info-card.highlight { background: var(--primary-bg); border-color: var(--primary-lighter); }
.info-label { display: block; font-size: 12px; color: var(--gray-500); margin-bottom: 4px; text-transform: uppercase; letter-spacing: 0.5px; }
.info-value { font-size: 16px; font-weight: 600; color: var(--gray-800); }
.info-value.reward { color: var(--primary); font-size: 24px; }

.description-box {
  padding: 20px; background: var(--gray-50); border-radius: var(--radius);
  color: var(--gray-700); line-height: 1.8; white-space: pre-wrap;
  border: 1px solid var(--gray-100);
}
.publisher-card {
  display: flex; align-items: center; gap: 16px;
  padding: 16px 20px; background: var(--gray-50); border-radius: var(--radius);
  border: 1px solid var(--gray-100);
}
.publisher-avatar {
  width: 48px; height: 48px; border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--accent));
  color: white; display: flex; align-items: center; justify-content: center;
  font-size: 20px; font-weight: 700; flex-shrink: 0;
}
.publisher-name { font-size: 16px; font-weight: 600; color: var(--gray-800); display: block; }
.publisher-label { font-size: 13px; color: var(--gray-500); }

.detail-actions {
  padding: 24px 32px; border-top: 1px solid var(--gray-100);
  display: flex; gap: 12px;
}
.claim-btn {
  display: flex !important; align-items: center; gap: 8px;
  height: 48px !important; padding: 0 28px !important; font-size: 16px !important;
  border-radius: var(--radius) !important;
}
.edit-btn {
  display: flex !important; align-items: center; gap: 8px;
  height: 48px !important; padding: 0 28px !important; font-size: 16px !important;
  border-radius: var(--radius) !important;
}
.delete-btn-detail {
  display: flex !important; align-items: center; gap: 8px;
  height: 48px !important; padding: 0 28px !important; font-size: 16px !important;
  border-radius: var(--radius) !important;
}

@media (max-width: 768px) {
  .detail-container { padding: 16px; }
  .detail-header, .detail-body, .detail-actions { padding: 20px; }
}
</style>
