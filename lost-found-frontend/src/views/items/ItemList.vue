<template>
  <div class="items-container">
    <div class="page-header">
      <div class="header-text">
        <h2>物品列表</h2>
        <p>浏览所有失物和招领信息</p>
      </div>
      <div class="filters">
        <el-select v-model="filters.type" placeholder="物品类型" clearable size="large">
          <el-option label="失物" value="LOST" />
          <el-option label="招领" value="FOUND" />
        </el-select>
        <el-select v-model="filters.status" placeholder="状态" clearable size="large">
          <el-option label="进行中" value="ACTIVE" />
          <el-option label="已认领" value="CLAIMED" />
          <el-option label="已关闭" value="CLOSED" />
        </el-select>
        <el-select v-model="filters.category" placeholder="分类" clearable size="large">
          <el-option label="证件" value="证件" />
          <el-option label="电子设备" value="电子" />
          <el-option label="日用品" value="日用" />
          <el-option label="书籍文具" value="书籍" />
          <el-option label="衣物饰品" value="衣物" />
          <el-option label="其他" value="其他" />
        </el-select>
        <el-input v-model="filters.keyword" placeholder="搜索关键词..." clearable size="large" class="search-input" />
        <el-button type="primary" size="large" @click="handleSearch" class="search-btn">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="16" height="16"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
          搜索
        </el-button>
      </div>
    </div>

    <div class="items-grid" v-loading="loading">
      <div v-for="(item, index) in items" :key="item.id" class="item-card" @click="$router.push(`/items/${item.id}`)" :style="{ animationDelay: `${index * 0.05}s` }">
        <div class="card-top">
          <div class="item-type" :class="item.itemType === 'LOST' ? 'lost' : 'found'">
            {{ item.itemType === 'LOST' ? '失物' : '招领' }}
          </div>
          <span class="status-dot" :class="item.status"></span>
        </div>
        <h3>{{ item.name }}</h3>
        <div class="item-meta">
          <div class="meta-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z"/><circle cx="12" cy="10" r="3"/></svg>
            {{ item.location || '未指定' }}
          </div>
          <div class="meta-item">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4-4v2"/><circle cx="12" cy="7" r="4"/></svg>
            {{ item.publisherName }}
          </div>
          <div class="meta-item" v-if="item.category">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" width="14" height="14"><path d="M22 19a2 2 0 01-2 2H4a2 2 0 01-2-2V5a2 2 0 012-2h5l2 3h9a2 2 0 012 2z"/></svg>
            {{ item.category }}
          </div>
        </div>
        <p class="item-desc">{{ item.description?.substring(0, 80) || '暂无描述' }}</p>
        <div class="item-footer">
          <span class="status-tag" :class="item.status">{{ statusMap[item.status] || item.status }}</span>
          <span class="view-link">查看详情 &rarr;</span>
        </div>
      </div>
    </div>

    <div v-if="!loading && items.length === 0" class="empty-state">
      <div class="empty-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" width="64" height="64"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
      </div>
      <p>暂无物品信息</p>
      <el-button type="primary" @click="$router.push('/publish')">发布第一条信息</el-button>
    </div>

    <div v-if="totalPages > 1" class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[6, 12, 24, 48]"
        :total="totalElements"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        background
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../../services/api'

const items = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)
const totalElements = ref(0)
const totalPages = ref(0)

const statusMap = { ACTIVE: '进行中', CLAIMED: '已认领', CLOSED: '已关闭' }

const filters = ref({ type: null, status: null, category: null, keyword: '' })

const fetchItems = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    if (filters.value.type) params.append('type', filters.value.type)
    if (filters.value.status) params.append('status', filters.value.status)
    if (filters.value.category) params.append('category', filters.value.category)
    if (filters.value.keyword) params.append('keyword', filters.value.keyword)
    params.append('page', currentPage.value - 1)
    params.append('size', pageSize.value)

    const response = await api.get('/items/paged', { params })
    if (response.data.success) {
      const data = response.data.data
      items.value = data.content
      totalElements.value = data.totalElements
      totalPages.value = data.totalPages
    }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchItems()
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchItems()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchItems()
}

onMounted(() => { fetchItems() })
</script>

<style scoped>
.items-container {
  padding: 32px;
  max-width: 1200px;
  margin: 0 auto;
  animation: fadeIn 0.4s var(--ease);
}
.page-header {
  margin-bottom: 32px;
}
.header-text h2 {
  font-size: 28px;
  font-weight: 700;
  color: var(--gray-900);
  margin: 0 0 4px;
}
.header-text p {
  color: var(--gray-500);
  font-size: 15px;
  margin: 0 0 20px;
}
.filters {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}
.search-input { flex: 1; min-width: 200px; }
.search-btn {
  display: flex !important;
  align-items: center;
  gap: 6px;
}

.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}
.item-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: var(--radius-lg);
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  box-shadow: 0 4px 15px rgba(0,0,0,0.03);
  animation: fadeInUp 0.4s var(--ease) both;
}
.item-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 15px 30px rgba(0,0,0,0.08);
  background: white;
  border-color: var(--primary-lighter);
}
.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.item-type {
  padding: 4px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.5px;
}
.item-type.lost { background: #fef2f2; color: #dc2626; }
.item-type.found { background: #ecfdf5; color: #059669; }

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}
.status-dot.ACTIVE { background: var(--success); box-shadow: 0 0 6px rgba(16, 185, 129, 0.4); }
.status-dot.CLAIMED { background: var(--warning); }
.status-dot.CLOSED { background: var(--gray-400); }

.item-card h3 {
  font-size: 18px;
  font-weight: 700;
  color: var(--gray-900);
  margin: 0 0 12px;
}
.item-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 12px;
}
.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--gray-500);
}
.item-desc {
  font-size: 14px;
  color: var(--gray-500);
  line-height: 1.6;
  margin: 0 0 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid var(--gray-100);
}
.status-tag {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}
.status-tag.ACTIVE { background: #ecfdf5; color: #059669; }
.status-tag.CLAIMED { background: #fffbeb; color: #d97706; }
.status-tag.CLOSED { background: var(--gray-100); color: var(--gray-500); }
.view-link {
  font-size: 13px;
  color: var(--primary);
  font-weight: 600;
  transition: gap 0.2s;
}

.pagination-wrapper {
  margin-top: 32px;
  display: flex;
  justify-content: center;
  padding: 20px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
}

.empty-state {
  text-align: center;
  padding: 80px 40px;
  background: white;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
}
.empty-icon {
  color: var(--gray-300);
  margin-bottom: 16px;
}
.empty-state p {
  font-size: 16px;
  color: var(--gray-500);
  margin: 0 0 24px;
}

@media (max-width: 768px) {
  .items-container { padding: 16px; }
  .items-grid { grid-template-columns: 1fr; }
}
</style>
