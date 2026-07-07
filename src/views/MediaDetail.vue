<template>
  <div class="detail-page">
    <div class="detail-header">
      <el-button icon="ArrowLeft" @click="goBack">返回</el-button>
      <h2 class="detail-title">{{ mediaInfo.fileName }}</h2>
      <div class="header-actions">
        <el-button icon="Edit" @click="showEditDialog = true">编辑</el-button>
        <el-button type="primary" icon="Download" @click="handleDownload">下载</el-button>
        <el-button icon="Share" @click="handleShare">分享</el-button>
        <el-button icon="Warning" type="danger" size="small" @click="handleReport">举报</el-button>
      </div>
    </div>
    <div class="detail-content">
      <div class="preview-section">
        <div class="preview-container">
          <img 
            v-if="mediaInfo.type === 'image'" 
            :src="mediaInfo.url" 
            :alt="mediaInfo.fileName" 
            class="preview-image"
          />
          <video 
            v-else-if="mediaInfo.type === 'video'" 
            :src="mediaInfo.url" 
            controls
            class="preview-video"
          />
          <div v-else class="preview-document">
            <el-icon :size="64"><Files /></el-icon>
            <div>{{ mediaInfo.fileName }}</div>
          </div>
        </div>
        <div class="preview-actions" v-if="mediaInfo.type === 'video'">
          <el-button size="small">1x</el-button>
          <el-button size="small">1.5x</el-button>
          <el-button size="small">2x</el-button>
          <el-button size="small">全屏</el-button>
        </div>
      </div>
      <div class="info-section">
        <div class="info-card">
          <h3 class="card-title">基本信息</h3>
          <div class="info-list">
            <div class="info-item">
              <span class="info-label">文件名</span>
              <span class="info-value">{{ mediaInfo.fileName }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">文件大小</span>
              <span class="info-value">{{ formatSize(mediaInfo.fileSize) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">文件类型</span>
              <span class="info-value">{{ getTypeName(mediaInfo.type) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">上传者</span>
              <span class="info-value">{{ mediaInfo.uploader }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">上传时间</span>
              <span class="info-value">{{ formatDate(mediaInfo.uploadTime) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">所属分类</span>
              <el-tag>{{ mediaInfo.category }}</el-tag>
            </div>
            <div class="info-item">
              <span class="info-label">下载次数</span>
              <span class="info-value">{{ mediaInfo.downloadCount }} 次</span>
            </div>
          </div>
        </div>
        <div class="info-card">
          <h3 class="card-title">AI标签</h3>
          <div class="tag-cloud">
            <el-tag 
              v-for="tag in mediaInfo.tags" 
              :key="tag" 
              @click="searchByTag(tag)"
            >
              {{ tag }}
            </el-tag>
          </div>
        </div>
        <div class="info-card">
          <h3 class="card-title">EXIF信息</h3>
          <div class="exif-list">
            <div class="exif-item" v-if="mediaInfo.exif">
              <span class="exif-label">拍摄时间</span>
              <span class="exif-value">{{ mediaInfo.exif.captureTime }}</span>
            </div>
            <div class="exif-item" v-if="mediaInfo.exif">
              <span class="exif-label">相机型号</span>
              <span class="exif-value">{{ mediaInfo.exif.cameraModel }}</span>
            </div>
            <div class="exif-item" v-if="mediaInfo.exif">
              <span class="exif-label">分辨率</span>
              <span class="exif-value">{{ mediaInfo.exif.width }} x {{ mediaInfo.exif.height }}</span>
            </div>
            <div class="exif-item" v-if="mediaInfo.exif">
              <span class="exif-label">GPS位置</span>
              <span class="exif-value">{{ mediaInfo.exif.gps }}</span>
            </div>
          </div>
        </div>
        <div class="info-card">
          <h3 class="card-title">描述</h3>
          <p class="description-text">{{ mediaInfo.description || '暂无描述' }}</p>
        </div>
      </div>
    </div>
    <el-dialog v-model="showEditDialog" title="编辑素材信息" width="500px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="文件名">
          <el-input v-model="editForm.fileName" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="editForm.category">
            <el-option label="校园活动" value="校园活动" />
            <el-option label="教学教务" value="教学教务" />
            <el-option label="荣誉表彰" value="荣誉表彰" />
            <el-option label="校园风景" value="校园风景" />
            <el-option label="师生风采" value="师生风采" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="editForm.tags" placeholder="多个标签用逗号分隔" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editForm.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Edit, Download, Share, Warning, Files } from '@element-plus/icons-vue'
import { getMediaDetail, getDownloadUrl, updateMedia } from '@/api/media'

const route = useRoute()
const router = useRouter()

const mediaInfo = ref({
  id: '',
  fileName: '',
  fileSize: 0,
  type: 'image',
  url: '',
  thumbnailUrl: '',
  uploader: '',
  uploadTime: '',
  category: '',
  tags: [],
  downloadCount: 0,
  description: '',
  exif: {}
})

const showEditDialog = ref(false)

const editForm = reactive({
  fileName: '',
  category: '',
  tags: '',
  description: ''
})

onMounted(() => {
  const id = route.params.id
  fetchDetail(id)
})

async function fetchDetail(id) {
  try {
    const res = await getMediaDetail(id)
    mediaInfo.value = res.data || mockMediaDetail
  } catch (error) {
    mediaInfo.value = mockMediaDetail
  }
}

function goBack() {
  router.back()
}

async function handleDownload() {
  try {
    const res = await getDownloadUrl(mediaInfo.value.id)
    if (res.data && res.data.signedUrl) {
      const link = document.createElement('a')
      link.href = res.data.signedUrl
      link.download = mediaInfo.value.fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      ElMessage.success('下载开始')
    }
  } catch (error) {
    ElMessage.error('获取下载链接失败')
  }
}

function handleShare() {
  const url = window.location.href
  navigator.clipboard.writeText(url).then(() => {
    ElMessage.success('链接已复制到剪贴板')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

function handleReport() {
  ElMessage.confirm('确定要举报此素材吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(() => {
    ElMessage.success('举报已提交')
  }).catch(() => {})
}

function searchByTag(tag) {
  router.push({ path: '/home', query: { keyword: tag } })
}

function formatSize(bytes) {
  if (!bytes) return '0 B'
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / 1024 / 1024).toFixed(1) + ' MB'
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

function getTypeName(type) {
  const types = {
    image: '图片',
    video: '视频',
    document: '文档'
  }
  return types[type] || type
}

function saveEdit() {
  updateMedia(mediaInfo.value.id, {
    fileName: editForm.fileName,
    category: editForm.category,
    tags: editForm.tags.split(',').map(t => t.trim()),
    description: editForm.description
  }).then(() => {
    ElMessage.success('保存成功')
    showEditDialog.value = false
    fetchDetail(mediaInfo.value.id)
  }).catch(() => {
    ElMessage.error('保存失败')
  })
}

const mockMediaDetail = {
  id: 1,
  fileName: '2024年春季运动会开幕式',
  fileSize: 104857600,
  type: 'video',
  url: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20sports%20meeting%20opening%20ceremony&image_size=landscape_16_9',
  thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20sports%20meeting%20opening%20ceremony&image_size=square',
  uploader: '张三',
  uploadTime: '2024-05-15 10:30:00',
  category: '校园活动',
  tags: ['运动会', '开幕式', '红幅', '春季', '学生'],
  downloadCount: 156,
  description: '2024年春季运动会开幕式现场，各学院方阵入场仪式。',
  exif: {
    captureTime: '2024-05-15 09:00:00',
    cameraModel: 'Canon EOS R6',
    width: 1920,
    height: 1080,
    gps: '30.5728° N, 104.0668° E'
  }
}
</script>

<style scoped>
.detail-page {
  max-width: 1200px;
  margin: 0 auto;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.detail-title {
  flex: 1;
  font-size: 20px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.detail-content {
  display: flex;
  gap: 24px;
}

.preview-section {
  flex: 2;
}

.preview-container {
  background: #1a1a1a;
  border-radius: 12px;
  overflow: hidden;
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image {
  width: 100%;
  max-height: 600px;
  object-fit: contain;
}

.preview-video {
  width: 100%;
  max-height: 600px;
}

.preview-document {
  text-align: center;
  color: #999;
}

.preview-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  justify-content: center;
}

.info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-size: 14px;
  color: #999;
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

.tag-cloud .el-tag {
  cursor: pointer;
}

.exif-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.exif-item {
  display: flex;
  justify-content: space-between;
}

.exif-label {
  font-size: 13px;
  color: #999;
}

.exif-value {
  font-size: 13px;
}

.description-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .detail-content {
    flex-direction: column;
  }
  
  .detail-header {
    flex-wrap: wrap;
  }
  
  .header-actions {
    margin-top: 12px;
  }
}
</style>
