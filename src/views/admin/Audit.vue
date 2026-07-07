<template>
  <div class="audit-page" v-loading="loading">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="下载日志" name="download">
        <div class="tab-header">
          <div class="header-left">
            <el-input 
              v-model="searchKeyword" 
              placeholder="搜索用户名或素材名称"
              size="default"
              style="width: 250px;"
              @keyup.enter="handleDownloadSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-date-picker 
              v-model="dateRange" 
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 300px; margin-left: 12px;"
            />
            <el-button type="primary" @click="handleDownloadSearch" style="margin-left: 12px;">筛选</el-button>
          </div>
          <div class="header-right">
            <el-button @click="exportDownloadLogs">导出Excel</el-button>
          </div>
        </div>
        <el-table :data="downloadLogs" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="userName" label="用户名" width="120" />
          <el-table-column prop="mediaName" label="素材名称" min-width="200" />
          <el-table-column prop="ip" label="IP地址" width="150" />
          <el-table-column prop="downloadTime" label="下载时间" width="180" />
          <el-table-column prop="userAgent" label="设备信息" width="200">
            <template #default="{ row }">
              <span class="ellipsis">{{ row.userAgent }}</span>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="downloadPagination.pageNum"
            v-model:page-size="downloadPagination.pageSize"
            :total="downloadPagination.total"
            layout="total, prev, pager, next"
            @current-change="handleDownloadSearch"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="操作日志" name="operation">
        <div class="tab-header">
          <div class="header-left">
            <el-input 
              v-model="opSearchKeyword" 
              placeholder="搜索用户名或操作内容"
              size="default"
              style="width: 250px;"
              @keyup.enter="handleOperationSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-select v-model="filterOperation" placeholder="操作类型" clearable style="width: 150px; margin-left: 12px;">
              <el-option label="上传" value="upload" />
              <el-option label="删除" value="delete" />
              <el-option label="编辑" value="edit" />
              <el-option label="审核" value="audit" />
              <el-option label="规则配置" value="rule" />
              <el-option label="用户管理" value="user" />
            </el-select>
            <el-date-picker 
              v-model="opDateRange" 
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
              style="width: 300px; margin-left: 12px;"
            />
            <el-button type="primary" @click="handleOperationSearch" style="margin-left: 12px;">筛选</el-button>
          </div>
          <div class="header-right">
            <el-button @click="exportOperationLogs">导出Excel</el-button>
          </div>
        </div>
        <el-table :data="operationLogs" border>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="userName" label="用户名" width="120" />
          <el-table-column prop="operation" label="操作类型" width="120">
            <template #default="{ row }">
              <el-tag :type="getOperationType(row.operation)">{{ getOperationText(row.operation) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="target" label="操作对象" min-width="150" />
          <el-table-column prop="detail" label="操作详情" min-width="200">
            <template #default="{ row }">
              <span class="ellipsis">{{ row.detail }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="ip" label="IP地址" width="150" />
          <el-table-column prop="operationTime" label="操作时间" width="180" />
        </el-table>
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="operationPagination.pageNum"
            v-model:page-size="operationPagination.pageSize"
            :total="operationPagination.total"
            layout="total, prev, pager, next"
            @current-change="handleOperationSearch"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getDownloadLogs, getOperationLogs } from '@/api/admin'

const activeTab = ref('download')

const searchKeyword = ref('')
const dateRange = ref([])

const opSearchKeyword = ref('')
const filterOperation = ref('')
const opDateRange = ref([])

const downloadPagination = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const operationPagination = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const downloadLogs = ref([])
const operationLogs = ref([])
const loading = ref(false)

onMounted(() => {
  fetchDownloadLogs()
  fetchOperationLogs()
})

async function fetchDownloadLogs() {
  loading.value = true
  try {
    const params = {
      keyword: searchKeyword.value,
      pageNum: downloadPagination.pageNum,
      pageSize: downloadPagination.pageSize
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    const res = await getDownloadLogs(params)
    downloadLogs.value = res.data.list || []
    downloadPagination.total = res.data.total || 0
  } catch (error) {
    downloadLogs.value = []
    downloadPagination.total = 0
  } finally {
    loading.value = false
  }
}

async function fetchOperationLogs() {
  loading.value = true
  try {
    const params = {
      keyword: opSearchKeyword.value,
      operation: filterOperation.value,
      pageNum: operationPagination.pageNum,
      pageSize: operationPagination.pageSize
    }
    if (opDateRange.value && opDateRange.value.length === 2) {
      params.startDate = opDateRange.value[0]
      params.endDate = opDateRange.value[1]
    }
    const res = await getOperationLogs(params)
    operationLogs.value = res.data.list || []
    operationPagination.total = res.data.total || 0
  } catch (error) {
    operationLogs.value = []
    operationPagination.total = 0
  } finally {
    loading.value = false
  }
}

function handleDownloadSearch() {
  downloadPagination.pageNum = 1
  fetchDownloadLogs()
}

function handleOperationSearch() {
  operationPagination.pageNum = 1
  fetchOperationLogs()
}

function exportDownloadLogs() {
  ElMessage.info('导出功能开发中')
}

function exportOperationLogs() {
  ElMessage.info('导出功能开发中')
}

function getOperationType(operation) {
  const types = {
    upload: 'success',
    delete: 'danger',
    edit: 'warning',
    audit: 'primary',
    rule: 'info',
    user: 'info'
  }
  return types[operation] || 'info'
}

function getOperationText(operation) {
  const texts = {
    upload: '上传',
    delete: '删除',
    edit: '编辑',
    audit: '审核',
    rule: '规则配置',
    user: '用户管理'
  }
  return texts[operation] || operation
}

const mockDownloadLogs = [
  { id: 1, userName: '张三', mediaName: '2024年春季运动会开幕式', ip: '192.168.1.100', downloadTime: '2024-05-15 10:30:00', userAgent: 'Mozilla/5.0 Windows' },
  { id: 2, userName: '李四', mediaName: '校园樱花盛开', ip: '192.168.1.101', downloadTime: '2024-05-15 11:20:00', userAgent: 'Mozilla/5.0 MacOS' },
  { id: 3, userName: '王五', mediaName: '新生开学典礼', ip: '192.168.1.102', downloadTime: '2024-05-15 14:15:00', userAgent: 'Mozilla/5.0 Android' },
  { id: 4, userName: '赵六', mediaName: '实验室科研成果展示', ip: '192.168.1.103', downloadTime: '2024-05-15 15:30:00', userAgent: 'Mozilla/5.0 iOS' },
  { id: 5, userName: '钱七', mediaName: '奖学金颁奖典礼', ip: '192.168.1.104', downloadTime: '2024-05-15 16:45:00', userAgent: 'Mozilla/5.0 Windows' },
  { id: 6, userName: '张三', mediaName: '校园音乐节现场', ip: '192.168.1.100', downloadTime: '2024-05-15 17:00:00', userAgent: 'Mozilla/5.0 Windows' }
]

const mockOperationLogs = [
  { id: 1, userName: '张三', operation: 'upload', target: '运动会开幕式.mp4', detail: '上传素材：运动会开幕式.mp4，大小：100MB', ip: '192.168.1.100', operationTime: '2024-05-15 09:00:00' },
  { id: 2, userName: '管理员', operation: 'audit', target: '运动会开幕式.mp4', detail: '审核通过素材：运动会开幕式.mp4', ip: '192.168.1.1', operationTime: '2024-05-15 09:30:00' },
  { id: 3, userName: '李四', operation: 'edit', target: '校园樱花盛开.jpg', detail: '编辑素材信息，修改描述', ip: '192.168.1.101', operationTime: '2024-05-15 10:00:00' },
  { id: 4, userName: '管理员', operation: 'delete', target: '旧素材.jpg', detail: '删除素材：旧素材.jpg', ip: '192.168.1.1', operationTime: '2024-05-15 11:00:00' },
  { id: 5, userName: '管理员', operation: 'rule', target: '运动会规则', detail: '新增规则：运动会素材自动分类', ip: '192.168.1.1', operationTime: '2024-05-15 14:00:00' },
  { id: 6, userName: '管理员', operation: 'user', target: '李四', detail: '重置用户李四的密码', ip: '192.168.1.1', operationTime: '2024-05-15 15:00:00' }
]
</script>

<style scoped>
.audit-page {
  padding: 0;
}

.tab-header {
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

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

@media (max-width: 768px) {
  .tab-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
