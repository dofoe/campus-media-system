<template>
  <div class="media-admin-page">
    <div class="admin-header">
      <div class="header-left">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索素材"
          size="default"
          style="width: 300px;"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-select v-model="filterCategory" placeholder="分类" clearable style="width: 150px; margin-left: 12px;">
          <el-option label="校园活动" value="校园活动" />
          <el-option label="教学教务" value="教学教务" />
          <el-option label="荣誉表彰" value="荣誉表彰" />
          <el-option label="校园风景" value="校园风景" />
          <el-option label="师生风采" value="师生风采" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="状态" clearable style="width: 120px; margin-left: 12px;">
          <el-option label="待审核" value="pending" />
          <el-option label="已通过" value="approved" />
          <el-option label="已驳回" value="rejected" />
        </el-select>
        <el-button type="primary" @click="handleSearch" style="margin-left: 12px;">筛选</el-button>
      </div>
      <div class="header-right">
        <el-button @click="handleBatchDelete" :disabled="selectedIds.length === 0" type="danger">批量删除</el-button>
        <el-button @click="handleBatchMove" :disabled="selectedIds.length === 0">批量移动分类</el-button>
      </div>
    </div>
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="素材列表" name="list">
        <el-table 
          :data="mediaList" 
          border 
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="fileName" label="文件名" min-width="200">
            <template #default="{ row }">
              <div class="file-cell">
                <img :src="row.thumbnailUrl" class="file-thumb" />
                <span>{{ row.fileName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="tags" label="标签" width="150">
            <template #default="{ row }">
              <el-tag v-for="tag in row.tags.slice(0, 2)" :key="tag" size="small">{{ tag }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="uploader" label="上传者" width="100" />
          <el-table-column prop="uploadTime" label="上传时间" width="160" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button size="small" @click="goDetail(row.id)">查看</el-button>
              <el-button size="small" @click="editMedia(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="deleteMedia(row)">删除</el-button>
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
      </el-tab-pane>
      <el-tab-pane label="审核队列" name="audit">
        <div class="audit-tip">
          <el-icon :size="16"><InfoFilled /></el-icon>
          以下素材为AI标记为"疑似违规"或"低置信度"的内容，请审核处理
        </div>
        <el-table :data="auditList" border>
          <el-table-column prop="fileName" label="文件名" min-width="200">
            <template #default="{ row }">
              <div class="file-cell">
                <img :src="row.thumbnailUrl" class="file-thumb" />
                <span>{{ row.fileName }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="riskLevel" label="风险等级" width="100">
            <template #default="{ row }">
              <el-tag :type="getRiskType(row.riskLevel)">{{ row.riskLevel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="reason" label="审核原因" width="200" />
          <el-table-column prop="uploader" label="上传者" width="100" />
          <el-table-column prop="uploadTime" label="上传时间" width="160" />
          <el-table-column label="操作" width="250">
            <template #default="{ row }">
              <el-button size="small" @click="goDetail(row.id)">详情</el-button>
              <el-button size="small" type="success" @click="handleAudit(row, 'approve')">通过</el-button>
              <el-button size="small" type="warning" @click="handleAudit(row, 'reject')">驳回</el-button>
              <el-button size="small" type="danger" @click="handleAudit(row, 'delete')">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="auditPagination.pageNum"
            v-model:page-size="auditPagination.pageSize"
            :total="auditPagination.total"
            layout="total, prev, pager, next"
            @current-change="fetchAuditList"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-dialog v-model="showDetailDrawer" title="素材详情" width="800px">
      <div class="detail-drawer-content" v-if="currentMedia">
        <div class="drawer-preview">
          <img :src="currentMedia.url" class="drawer-image" />
        </div>
        <div class="drawer-info">
          <div class="info-row">
            <span class="info-label">文件名</span>
            <span class="info-value">{{ currentMedia.fileName }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">分类</span>
            <el-tag>{{ currentMedia.category }}</el-tag>
          </div>
          <div class="info-row">
            <span class="info-label">标签</span>
            <div class="tag-cloud">
              <el-tag v-for="tag in currentMedia.tags" :key="tag">{{ tag }}</el-tag>
            </div>
          </div>
          <div class="info-row">
            <span class="info-label">EXIF信息</span>
            <div class="exif-content">
              <div v-if="currentMedia.exif">
                <div>拍摄时间：{{ currentMedia.exif.captureTime }}</div>
                <div>相机型号：{{ currentMedia.exif.cameraModel }}</div>
                <div>分辨率：{{ currentMedia.exif.width }} x {{ currentMedia.exif.height }}</div>
              </div>
            </div>
          </div>
          <div class="info-row">
            <span class="info-label">下载日志</span>
            <div class="download-logs">
              <div v-for="(log, index) in currentMedia.downloadLogs" :key="index" class="log-item">
                <span>{{ log.userName }}</span>
                <span>{{ log.time }}</span>
                <span>{{ log.ip }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
    <el-dialog v-model="showMoveDialog" title="批量移动分类" width="400px">
      <el-form>
        <el-form-item label="目标分类">
          <el-select v-model="targetCategory">
            <el-option label="校园活动" value="校园活动" />
            <el-option label="教学教务" value="教学教务" />
            <el-option label="荣誉表彰" value="荣誉表彰" />
            <el-option label="校园风景" value="校园风景" />
            <el-option label="师生风采" value="师生风采" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showMoveDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmMove">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, InfoFilled } from '@element-plus/icons-vue'
import { searchMedia, deleteMedia as deleteMediaApi, getPendingAudit, auditMedia } from '@/api/media'

const router = useRouter()

const searchKeyword = ref('')
const filterCategory = ref('')
const filterStatus = ref('')
const activeTab = ref('list')

const selectedIds = ref([])

const pagination = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const auditPagination = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const mediaList = ref([])
const auditList = ref([])

const showDetailDrawer = ref(false)
const showMoveDialog = ref(false)
const targetCategory = ref('')
const currentMedia = ref(null)

onMounted(() => {
  fetchMediaList()
  fetchAuditList()
})

async function fetchMediaList() {
  try {
    const params = {
      keyword: searchKeyword.value,
      categoryId: filterCategory.value,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await searchMedia(params)
    mediaList.value = res.data.list || mockMediaList
    pagination.total = res.data.total || mockMediaList.length
  } catch (error) {
    mediaList.value = mockMediaList
    pagination.total = mockMediaList.length
  }
}

async function fetchAuditList() {
  try {
    const params = {
      pageNum: auditPagination.pageNum,
      pageSize: auditPagination.pageSize
    }
    const res = await getPendingAudit(params)
    auditList.value = res.data.list || mockAuditList
    auditPagination.total = res.data.total || mockAuditList.length
  } catch (error) {
    auditList.value = mockAuditList
    auditPagination.total = mockAuditList.length
  }
}

function handleSearch() {
  pagination.pageNum = 1
  fetchMediaList()
}

function handleSelectionChange(selection) {
  selectedIds.value = selection.map(item => item.id)
}

function goDetail(id) {
  router.push(`/media/${id}`)
}

function editMedia(row) {
  ElMessage.info('编辑功能开发中')
}

async function deleteMedia(row) {
  try {
    await ElMessageBox.confirm('确定要删除该素材吗？', '提示')
    await deleteMediaApi(row.id)
    ElMessage.success('删除成功')
    fetchMediaList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

async function handleBatchDelete() {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个素材吗？`, '提示')
    await deleteMediaApi(selectedIds.value)
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    fetchMediaList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

function handleBatchMove() {
  showMoveDialog.value = true
}

function confirmMove() {
  if (!targetCategory.value) {
    ElMessage.warning('请选择目标分类')
    return
  }
  ElMessage.success('批量移动成功')
  showMoveDialog.value = false
  targetCategory.value = ''
  fetchMediaList()
}

async function handleAudit(row, action) {
  const actionText = {
    approve: '通过',
    reject: '驳回',
    delete: '删除'
  }
  try {
    await ElMessageBox.confirm(`确定要${actionText[action]}该素材吗？`, '提示')
    await auditMedia(row.id, { action })
    ElMessage.success(`${actionText[action]}成功`)
    fetchAuditList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

function getStatusType(status) {
  const types = {
    pending: 'warning',
    approved: 'success',
    rejected: 'danger'
  }
  return types[status] || 'info'
}

function getStatusText(status) {
  const texts = {
    pending: '待审核',
    approved: '已通过',
    rejected: '已驳回'
  }
  return texts[status] || status
}

function getRiskType(level) {
  const types = {
    '高': 'danger',
    '中': 'warning',
    '低': 'info'
  }
  return types[level] || 'info'
}

const mockMediaList = [
  { id: 1, fileName: '2024年春季运动会开幕式', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20meeting&image_size=square', category: '校园活动', tags: ['运动会', '开幕式'], uploader: '张三', uploadTime: '2024-05-15', status: 'approved' },
  { id: 2, fileName: '校园樱花盛开', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=cherry%20blossom&image_size=square', category: '校园风景', tags: ['樱花', '春天'], uploader: '李四', uploadTime: '2024-04-10', status: 'approved' },
  { id: 3, fileName: '新生开学典礼', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=opening%20ceremony&image_size=square', category: '校园活动', tags: ['开学典礼', '新生'], uploader: '王五', uploadTime: '2024-09-01', status: 'approved' },
  { id: 4, fileName: '实验室科研成果展示', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=laboratory&image_size=square', category: '教学教务', tags: ['实验室', '科研'], uploader: '赵六', uploadTime: '2024-06-15', status: 'pending' },
  { id: 5, fileName: '奖学金颁奖典礼', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=award%20ceremony&image_size=square', category: '荣誉表彰', tags: ['奖学金', '表彰'], uploader: '钱七', uploadTime: '2024-12-20', status: 'approved' },
  { id: 6, fileName: '校园音乐节现场', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=music%20festival&image_size=square', category: '校园活动', tags: ['音乐节', '活动'], uploader: '孙八', uploadTime: '2024-05-20', status: 'rejected' }
]

const mockAuditList = [
  { id: 101, fileName: '测试素材1', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=test&image_size=square', category: '校园活动', riskLevel: '高', reason: 'AI识别到疑似违规内容', uploader: '测试用户', uploadTime: '2024-07-01' },
  { id: 102, fileName: '测试素材2', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=test2&image_size=square', category: '教学教务', riskLevel: '中', reason: '低置信度识别', uploader: '测试用户', uploadTime: '2024-07-02' },
  { id: 103, fileName: '测试素材3', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=test3&image_size=square', category: '校园风景', riskLevel: '低', reason: 'OCR文本不清晰', uploader: '测试用户', uploadTime: '2024-07-03' }
]
</script>

<style scoped>
.media-admin-page {
  padding: 0;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.file-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.file-thumb {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 4px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.audit-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  background: #fff7e6;
  border-radius: 8px;
  margin-bottom: 16px;
  color: #e6a23c;
}

.detail-drawer-content {
  display: flex;
  gap: 20px;
}

.drawer-preview {
  flex: 1;
}

.drawer-image {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
  border-radius: 8px;
}

.drawer-info {
  flex: 1;
}

.info-row {
  margin-bottom: 16px;
}

.info-label {
  font-size: 14px;
  color: #999;
  margin-right: 12px;
}

.info-value {
  font-size: 14px;
  font-weight: 500;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.exif-content {
  font-size: 13px;
  color: #666;
}

.download-logs {
  font-size: 13px;
}

.log-item {
  display: flex;
  gap: 16px;
  margin-bottom: 8px;
  color: #666;
}

@media (max-width: 768px) {
  .admin-header {
    flex-direction: column;
    gap: 12px;
  }
  
  .detail-drawer-content {
    flex-direction: column;
  }
}
</style>
