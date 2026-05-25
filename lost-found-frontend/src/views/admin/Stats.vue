<template>
  <div class="stats-container">
    <div class="page-header">
      <h2>统计报表</h2>
      <p>系统运营数据概览</p>
    </div>

    <div class="stats-cards">
      <div class="stat-card" v-for="(stat, i) in statCards" :key="i" :style="{ animationDelay: `${i * 0.1}s` }">
        <div class="stat-icon" :style="{ background: stat.bg }">
          <span v-html="stat.icon"></span>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </div>

    <div class="charts-grid">
      <div class="chart-card">
        <h3>热门地点 TOP 10</h3>
        <div class="location-list">
          <div v-for="(loc, index) in topLocations" :key="index" class="location-bar">
            <div class="bar-rank">{{ index + 1 }}</div>
            <div class="bar-label">{{ loc[0] || '未知' }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: `${(loc[1] / maxLocationCount) * 100}%` }"></div>
            </div>
            <div class="bar-value">{{ loc[1] }}</div>
          </div>
          <div v-if="topLocations.length === 0" class="empty-chart">暂无数据</div>
        </div>
      </div>

      <div class="chart-card">
        <h3>月度趋势</h3>
        <div class="trend-list">
          <div v-for="(trend, index) in monthlyTrend" :key="index" class="trend-item">
            <div class="trend-bar-wrapper">
              <div class="trend-fill" :style="{ height: `${(trend[1] / maxTrendCount) * 100}%` }"></div>
            </div>
            <div class="trend-value">{{ trend[1] }}</div>
            <div class="trend-month">{{ formatMonth(trend[0]) }}</div>
          </div>
          <div v-if="monthlyTrend.length === 0" class="empty-chart">暂无数据</div>
        </div>
      </div>

      <div class="chart-card recovery-card">
        <h3>平均找回时长</h3>
        <div class="recovery-content">
          <div class="recovery-number">{{ avgRecovery.averageDays || 0 }}</div>
          <div class="recovery-unit">天</div>
          <div class="recovery-detail">约 {{ avgRecovery.averageHours || 0 }} 小时</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useStatsStore } from '../../store/stats'

const statsStore = useStatsStore()

const stats = computed(() => statsStore.overview)
const topLocations = computed(() => statsStore.topLocations || [])
const monthlyTrend = computed(() => {
  const data = statsStore.monthlyTrend || []
  return data.slice(0, 12).reverse()
})
const avgRecovery = computed(() => statsStore.avgRecovery || { days: 0, hours: 0 })

const maxLocationCount = computed(() => {
  if (topLocations.value.length === 0) return 1
  return Math.max(...topLocations.value.map(l => Number(l[1]) || 0), 1)
})
const maxTrendCount = computed(() => {
  if (monthlyTrend.value.length === 0) return 1
  return Math.max(...monthlyTrend.value.map(t => Number(t[1]) || 0), 1)
})

const formatMonth = (month) => {
  if (!month) return ''
  const parts = month.split('-')
  return parts.length === 2 ? `${parts[1]}月` : month
}

const statCards = computed(() => [
  { value: stats.value.totalUsers || 0, label: '注册用户', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4-4v2"/><circle cx="9" cy="7" r="4"/></svg>', bg: 'linear-gradient(135deg, #6366f1, #818cf8)' },
  { value: stats.value.totalItems || 0, label: '物品总数', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><rect x="2" y="7" width="20" height="14" rx="2" ry="2"/></svg>', bg: 'linear-gradient(135deg, #0ea5e9, #38bdf8)' },
  { value: stats.value.approvedClaims || 0, label: '成功认领', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><polyline points="20,6 9,17 4,12"/></svg>', bg: 'linear-gradient(135deg, #10b981, #06b6d4)' },
  { value: stats.value.pendingReports || 0, label: '待处理举报', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" width="24" height="24"><path d="M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z"/></svg>', bg: 'linear-gradient(135deg, #f59e0b, #fbbf24)' }
])

onMounted(() => { statsStore.fetchAll() })
</script>

<style scoped>
.stats-container { padding: 32px; max-width: 1200px; margin: 0 auto; animation: fadeIn 0.4s var(--ease); }
.page-header { margin-bottom: 32px; }
.page-header h2 { font-size: 28px; font-weight: 700; color: var(--gray-900); margin: 0 0 4px; }
.page-header p { color: var(--gray-500); font-size: 15px; margin: 0; }

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}
.stat-card {
  background: white; padding: 24px; border-radius: var(--radius-lg);
  display: flex; align-items: center; gap: 20px;
  box-shadow: var(--shadow); animation: fadeInUp 0.5s var(--ease) both;
  transition: transform 0.2s var(--ease), box-shadow 0.2s var(--ease);
}
.stat-card:hover { transform: translateY(-3px); box-shadow: var(--shadow-lg); }
.stat-icon {
  width: 56px; height: 56px; border-radius: 14px;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.stat-number { font-size: 32px; font-weight: 800; color: var(--gray-900); line-height: 1; }
.stat-label { font-size: 14px; color: var(--gray-500); margin-top: 4px; }

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}
.chart-card {
  background: white; border-radius: var(--radius-lg); box-shadow: var(--shadow); padding: 24px;
}
.chart-card h3 {
  font-size: 18px; font-weight: 700; color: var(--gray-800); margin: 0 0 20px;
}

/* 热门地点 */
.location-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.location-bar {
  display: flex; align-items: center; gap: 12px;
}
.bar-rank {
  width: 24px; height: 24px; border-radius: 50%;
  background: var(--gray-100); color: var(--gray-500);
  display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 700; flex-shrink: 0;
}
.location-bar:nth-child(1) .bar-rank { background: #fef3c7; color: #d97706; }
.location-bar:nth-child(2) .bar-rank { background: #e0e7ff; color: #6366f1; }
.location-bar:nth-child(3) .bar-rank { background: #d1fae5; color: #059669; }
.bar-label { width: 80px; font-size: 13px; color: var(--gray-600); text-align: right; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.bar-track { flex: 1; height: 24px; background: var(--gray-100); border-radius: 12px; overflow: hidden; }
.bar-fill { height: 100%; background: linear-gradient(135deg, var(--primary), var(--accent)); border-radius: 12px; transition: width 0.5s ease; min-width: 8px; }
.bar-value { width: 40px; font-size: 14px; font-weight: 600; color: var(--gray-700); }

/* 月度趋势 */
.trend-list {
  display: flex;
  gap: 8px;
  align-items: flex-end;
  height: 200px;
  padding-top: 20px;
}
.trend-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
  height: 100%;
}
.trend-bar-wrapper {
  flex: 1;
  width: 100%;
  background: var(--gray-100);
  border-radius: 8px 8px 0 0;
  display: flex;
  align-items: flex-end;
  overflow: hidden;
}
.trend-fill {
  width: 100%;
  background: linear-gradient(180deg, var(--primary), var(--accent));
  border-radius: 8px 8px 0 0;
  transition: height 0.5s ease;
  min-height: 4px;
}
.trend-value { font-size: 13px; font-weight: 600; color: var(--gray-700); }
.trend-month { font-size: 11px; color: var(--gray-500); }

/* 平均找回时长 */
.recovery-card { display: flex; flex-direction: column; }
.recovery-content {
  flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center;
}
.recovery-number { font-size: 72px; font-weight: 800; color: var(--primary); line-height: 1; }
.recovery-unit { font-size: 20px; color: var(--gray-500); margin-top: 4px; }
.recovery-detail { font-size: 14px; color: var(--gray-400); margin-top: 12px; }
.empty-chart { text-align: center; padding: 40px; color: var(--gray-400); }

@media (max-width: 768px) {
  .stats-container { padding: 16px; }
  .stats-cards { grid-template-columns: repeat(2, 1fr); }
  .charts-grid { grid-template-columns: 1fr; }
  .trend-list { height: 150px; }
}
</style>
