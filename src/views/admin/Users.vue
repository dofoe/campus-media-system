<template>
  <div class="users-page">
    <div class="users-header">
      <div class="header-left">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索用户名"
          size="default"
          style="width: 250px;"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="filterRole" placeholder="角色" clearable style="width: 120px; margin-left: 12px;">
          <el-option label="超级管理员" value="admin" />
          <el-option label="部门管理员" value="dept_admin" />
          <el-option label="普通用户" value="user" />
        </el-select>
        <el-select v-model="filterDept" placeholder="部门" clearable style="width: 150px; margin-left: 12px;">
          <el-option label="宣传部" value="宣传部" />
          <el-option label="教务处" value="教务处" />
          <el-option label="学生处" value="学生处" />
          <el-option label="计算机学院" value="计算机学院" />
          <el-option label="文学院" value="文学院" />
          <el-option label="商学院" value="商学院" />
        </el-select>
        <el-button type="primary" @click="handleSearch" style="margin-left: 12px;">筛选</el-button>
      </div>
      <div class="header-right">
        <el-button @click="handleBatchExport" :disabled="selectedIds.length === 0">导出Excel</el-button>
        <el-button type="primary" @click="showAddDialog = true">新增用户</el-button>
      </div>
    </div>
    <el-table 
      :data="userList" 
      border 
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="工号/学号" width="120" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="dept" label="部门" width="120" />
      <el-table-column prop="role" label="角色" width="120">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.role)">{{ getRoleText(row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataScope" label="数据范围" width="120">
        <template #default="{ row }">
          <el-tag size="small">{{ getDataScopeText(row.dataScope) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-switch v-model="row.status" @change="toggleStatus(row)" />
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="160" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-button size="small" @click="editUser(row)">编辑</el-button>
          <el-button size="small" @click="resetPassword(row)">重置密码</el-button>
          <el-button size="small" type="danger" @click="deleteUser(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, prev, pager, next"
        @current-change="handleSearch"
      />
    </div>
    <el-dialog v-model="showAddDialog" :title="editingUser ? '编辑用户' : '新增用户'" width="500px">
      <el-form :model="userForm" label-width="80px">
        <el-form-item label="工号/学号" required>
          <el-input v-model="userForm.username" placeholder="请输入工号/学号" />
        </el-form-item>
        <el-form-item label="姓名" required>
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="密码" :required="!editingUser">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="部门" required>
          <el-select v-model="userForm.dept">
            <el-option label="宣传部" value="宣传部" />
            <el-option label="教务处" value="教务处" />
            <el-option label="学生处" value="学生处" />
            <el-option label="计算机学院" value="计算机学院" />
            <el-option label="文学院" value="文学院" />
            <el-option label="商学院" value="商学院" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" required>
          <el-select v-model="userForm.role">
            <el-option label="超级管理员" value="admin" />
            <el-option label="部门管理员" value="dept_admin" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据范围">
          <el-select v-model="userForm.dataScope">
            <el-option label="全校" value="all" />
            <el-option label="本部门" value="dept" />
            <el-option label="仅自己" value="self" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="userForm.status" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="showResetDialog" title="重置密码" width="400px">
      <el-form :model="resetForm" label-width="80px">
        <el-form-item label="新密码" required>
          <el-input v-model="resetForm.password" type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认密码" required>
          <el-input v-model="resetForm.confirmPassword" type="password" placeholder="请确认密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showResetDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmReset">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getUsers, createUser, updateUser, deleteUser as deleteUserApi, resetPassword as resetPasswordApi } from '@/api/admin'

const searchKeyword = ref('')
const filterRole = ref('')
const filterDept = ref('')
const selectedIds = ref([])
const editingUser = ref(null)
const resetUserId = ref(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const userList = ref([])

const showAddDialog = ref(false)
const showResetDialog = ref(false)

const userForm = reactive({
  username: '',
  name: '',
  password: '',
  dept: '',
  role: 'user',
  dataScope: 'self',
  status: true
})

const resetForm = reactive({
  password: '',
  confirmPassword: ''
})

onMounted(() => {
  fetchUsers()
})

async function fetchUsers() {
  try {
    const params = {
      keyword: searchKeyword.value,
      role: filterRole.value,
      dept: filterDept.value,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getUsers(params)
    userList.value = res.data.list || mockUsers
    pagination.total = res.data.total || mockUsers.length
  } catch (error) {
    userList.value = mockUsers
    pagination.total = mockUsers.length
  }
}

function handleSearch() {
  pagination.pageNum = 1
  fetchUsers()
}

function handleSelectionChange(selection) {
  selectedIds.value = selection.map(item => item.id)
}

function editUser(user) {
  editingUser.value = user
  Object.assign(userForm, {
    username: user.username,
    name: user.name,
    dept: user.dept,
    role: user.role,
    dataScope: user.dataScope,
    status: user.status
  })
  showAddDialog.value = true
}

async function saveUser() {
  if (!userForm.username || !userForm.name || !userForm.dept || !userForm.role) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  try {
    if (editingUser.value) {
      await updateUser(editingUser.value.id, userForm)
      ElMessage.success('用户更新成功')
    } else {
      if (!userForm.password) {
        ElMessage.warning('请设置密码')
        return
      }
      await createUser(userForm)
      ElMessage.success('用户创建成功')
    }
    showAddDialog.value = false
    editingUser.value = null
    resetFormData()
    fetchUsers()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

function resetFormData() {
  userForm.username = ''
  userForm.name = ''
  userForm.password = ''
  userForm.dept = ''
  userForm.role = 'user'
  userForm.dataScope = 'self'
  userForm.status = true
}

async function deleteUser(user) {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示')
    await deleteUserApi(user.id)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

async function toggleStatus(user) {
  try {
    await updateUser(user.id, { status: user.status })
    ElMessage.success(user.status ? '用户已启用' : '用户已禁用')
  } catch (error) {
    ElMessage.error('操作失败')
    user.status = !user.status
  }
}

function resetPassword(user) {
  resetUserId.value = user.id
  resetForm.password = ''
  resetForm.confirmPassword = ''
  showResetDialog.value = true
}

async function confirmReset() {
  if (!resetForm.password || !resetForm.confirmPassword) {
    ElMessage.warning('请填写密码')
    return
  }
  if (resetForm.password !== resetForm.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  
  try {
    await resetPasswordApi(resetUserId.value, { password: resetForm.password })
    ElMessage.success('密码重置成功')
    showResetDialog.value = false
  } catch (error) {
    ElMessage.error('重置失败')
  }
}

function handleBatchExport() {
  ElMessage.info('导出功能开发中')
}

function getRoleType(role) {
  const types = {
    admin: 'danger',
    dept_admin: 'warning',
    user: 'info'
  }
  return types[role] || 'info'
}

function getRoleText(role) {
  const texts = {
    admin: '超级管理员',
    dept_admin: '部门管理员',
    user: '普通用户'
  }
  return texts[role] || role
}

function getDataScopeText(scope) {
  const texts = {
    all: '全校',
    dept: '本部门',
    self: '仅自己'
  }
  return texts[scope] || scope
}

const mockUsers = [
  { id: 1, username: 'admin', name: '管理员', dept: '宣传部', role: 'admin', dataScope: 'all', status: true, createdAt: '2024-01-01' },
  { id: 2, username: 'dept001', name: '张科长', dept: '教务处', role: 'dept_admin', dataScope: 'dept', status: true, createdAt: '2024-01-15' },
  { id: 3, username: '2021001', name: '张三', dept: '计算机学院', role: 'user', dataScope: 'self', status: true, createdAt: '2024-02-01' },
  { id: 4, username: '2021002', name: '李四', dept: '文学院', role: 'user', dataScope: 'self', status: true, createdAt: '2024-02-05' },
  { id: 5, username: '2021003', name: '王五', dept: '商学院', role: 'user', dataScope: 'self', status: false, createdAt: '2024-02-10' },
  { id: 6, username: 'dept002', name: '李主任', dept: '学生处', role: 'dept_admin', dataScope: 'dept', status: true, createdAt: '2024-03-01' }
]
</script>

<style scoped>
.users-page {
  padding: 0;
}

.users-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
}

@media (max-width: 768px) {
  .users-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
