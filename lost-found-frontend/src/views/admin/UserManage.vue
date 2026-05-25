<template>
  <div class="users-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <p>管理系统中的所有用户</p>
    </div>
    <div class="table-wrapper">
      <el-table :data="users" v-loading="loading" style="width: 100%" empty-text="暂无用户" :header-cell-style="{ background: '#f8fafc', color: '#475569', fontWeight: '600' }">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="用户名" min-width="120">
          <template #default="{ row }">
            <div class="user-cell">
              <div class="mini-avatar">{{ row.username?.charAt(0)?.toUpperCase() }}</div>
              <span class="username-text">{{ row.username }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="140">
          <template #default="{ row }">{{ row.phone || '-' }}</template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180">
          <template #default="{ row }">{{ row.email || '-' }}</template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'danger' : 'info'" round>
              {{ row.role === 'ADMIN' ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="170">
          <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="openEditDialog(row)" class="action-btn">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)" class="action-btn" :disabled="row.role === 'ADMIN'">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="showEditDialog" title="编辑用户" width="500px" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="80px">
        <el-form-item label="用户名">
          <el-input :value="editForm.username" disabled />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="editForm.role" style="width: 100%">
            <el-option label="普通用户" value="REGULAR" />
            <el-option label="管理员" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="editForm.password" type="password" placeholder="留空则不修改密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate" :loading="updating">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../../services/api'

const users = ref([])
const loading = ref(false)
const showEditDialog = ref(false)
const updating = ref(false)
const editFormRef = ref(null)
const editingUserId = ref(null)

const editForm = ref({
  username: '',
  phone: '',
  email: '',
  role: 'REGULAR',
  password: ''
})

const editRules = {
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }]
}

const formatDate = (d) => d ? new Date(d).toLocaleString('zh-CN') : ''

const fetchUsers = async () => {
  loading.value = true
  try {
    const response = await api.get('/users')
    if (response.data.success) users.value = response.data.data
  } finally {
    loading.value = false
  }
}

const openEditDialog = (user) => {
  editingUserId.value = user.id
  editForm.value = {
    username: user.username,
    phone: user.phone || '',
    email: user.email || '',
    role: user.role,
    password: ''
  }
  showEditDialog.value = true
}

const handleUpdate = async () => {
  try {
    await editFormRef.value.validate()
    updating.value = true
    const data = {
      phone: editForm.value.phone,
      email: editForm.value.email,
      role: editForm.value.role
    }
    if (editForm.value.password) {
      data.password = editForm.value.password
    }
    const response = await api.put(`/users/${editingUserId.value}`, data)
    if (response.data.success) {
      ElMessage.success('用户信息更新成功')
      showEditDialog.value = false
      fetchUsers()
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '更新失败')
  } finally {
    updating.value = false
  }
}

const handleDelete = async (user) => {
  try {
    await ElMessageBox.confirm(`确定要删除用户「${user.username}」吗？此操作不可撤销。`, '确认删除', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await api.delete(`/users/${user.id}`)
    if (response.data.success) {
      ElMessage.success('用户删除成功')
      fetchUsers()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

onMounted(() => { fetchUsers() })
</script>

<style scoped>
.users-container { padding: 32px; max-width: 1100px; margin: 0 auto; animation: fadeIn 0.4s var(--ease); }
.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 28px; font-weight: 700; color: var(--gray-900); margin: 0 0 4px; }
.page-header p { color: var(--gray-500); font-size: 15px; margin: 0; }
.table-wrapper { background: white; border-radius: var(--radius-lg); box-shadow: var(--shadow); overflow: hidden; }
.user-cell { display: flex; align-items: center; gap: 8px; }
.mini-avatar {
  width: 28px; height: 28px; border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-lighter), var(--accent));
  color: white; display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 700; flex-shrink: 0;
}
.username-text { font-weight: 600; color: var(--gray-800); }
.action-btn { display: inline-flex !important; align-items: center; gap: 4px; }
</style>
