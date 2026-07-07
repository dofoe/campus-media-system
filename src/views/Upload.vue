<template>
  <div class="upload-page">
    <div class="upload-section">
      <div class="upload-dragger">
        <el-upload
          ref="uploadRef"
          class="upload-area"
          drag
          :auto-upload="false"
          :before-upload="beforeUpload"
          :on-change="onFileChange"
          :on-remove="onRemove"
          multiple
        >
          <el-icon :size="48" class="upload-icon"><UploadFilled /></el-icon>
          <div class="upload-text">
            <div>拖拽文件或文件夹到此处</div>
            <div class="upload-hint">或点击选择文件</div>
          </div>
          <div class="upload-limit">
            支持图片、视频、文档格式，单文件最大500MB
          </div>
        </el-upload>
      </div>
    </div>
    <div class="upload-queue" v-if="uploadList.length > 0">
      <div class="queue-header">
        <span class="queue-title">上传队列</span>
        <span class="queue-count">{{ completedCount }}/{{ uploadList.length }} 完成</span>
      </div>
      <div class="queue-list">
        <div 
          v-for="(item, index) in uploadList" 
          :key="index" 
          class="queue-item"
        >
          <div class="queue-item-left">
            <div class="file-icon">
              <el-icon :size="24">
                <Picture v-if="item.type === 'image'" />
                <VideoPlay v-else-if="item.type === 'video'" />
                <Files v-else />
              </el-icon>
            </div>
            <div class="file-info">
              <div class="file-name">{{ item.fileName }}</div>
              <div class="file-size">{{ formatSize(item.fileSize) }}</div>
            </div>
          </div>
          <div class="queue-item-right">
            <div v-if="item.status === 'queued'" class="status-tag">排队中</div>
            <div v-else-if="item.status === 'uploading'" class="status-tag uploading">
              <el-progress :percentage="item.progress" :stroke-width="6" />
            </div>
            <div v-else-if="item.status === 'processing'" class="status-tag processing">
              <el-icon :size="16" class="loading-icon"><Loading /></el-icon>
              正在AI识别...
            </div>
            <div v-else-if="item.status === 'success'" class="status-tag success">
              <el-icon :size="16"><Check /></el-icon>
              成功
            </div>
            <div v-else-if="item.status === 'failed'" class="status-tag failed">
              <el-icon :size="16"><Close /></el-icon>
              失败
            </div>
            <el-button 
              v-if="item.status === 'failed'" 
              size="small" 
              type="primary"
              @click="retryUpload(index)"
            >
              重试
            </el-button>
            <el-button 
              v-if="item.status !== 'uploading'" 
              size="small" 
              @click="onRemove(item)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="completedList.length > 0" class="complete-section">
      <div class="complete-header">
        <span class="complete-title">已完成上传</span>
      </div>
      <div class="complete-list">
        <div 
          v-for="(item, index) in completedList" 
          :key="index" 
          class="complete-item"
        >
          <img :src="item.thumbnail" :alt="item.fileName" class="complete-thumb" />
          <div class="complete-info">
            <div class="complete-name">{{ item.fileName }}</div>
            <div class="complete-meta">
              <span>分类：{{ item.category || '未分类' }}</span>
              <span>标签：{{ item.tags?.join(', ') || '无' }}</span>
            </div>
            <div class="complete-actions">
              <el-button size="small" @click="goDetail(item.id)">查看详情</el-button>
              <el-button size="small" @click="editMedia(item)">编辑信息</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { UploadFilled, Picture, VideoPlay, Files, Loading, Check, Close } from '@element-plus/icons-vue'
import { initUpload, completeUpload } from '@/api/media'

const router = useRouter()

const uploadRef = ref(null)
const uploadList = ref([])
const completedList = ref([])

const completedCount = ref(0)

function beforeUpload(file) {
  const fileSize = file.size / 1024 / 1024
  if (fileSize > 500) {
    ElMessage.error('单个文件不能超过500MB')
    return false
  }
  return true
}

function onFileChange(file, fileList) {
  const newFile = {
    id: Date.now() + Math.random(),
    file: file.raw,
    fileName: file.name,
    fileSize: file.size,
    type: getFileType(file.name),
    status: 'queued',
    progress: 0,
    uploadId: '',
    storageKey: ''
  }
  uploadList.value.push(newFile)
  startUpload(uploadList.value.length - 1)
}

function onRemove(file) {
  const index = uploadList.value.findIndex(item => item.id === file.id)
  if (index > -1) {
    uploadList.value.splice(index, 1)
  }
}

async function startUpload(index) {
  const item = uploadList.value[index]
  if (!item) return
  
  item.status = 'uploading'
  
  try {
    const initRes = await initUpload({
      fileName: item.fileName,
      fileSize: item.fileSize,
      mimeType: item.file.type || ''
    })
    
    item.uploadId = initRes.data?.uploadId || ''
    item.storageKey = initRes.data?.storageKey || ''
    
    simulateUpload(index)
  } catch (error) {
    item.status = 'failed'
    ElMessage.error(`上传初始化失败：${item.fileName}`)
  }
}

function simulateUpload(index) {
  const item = uploadList.value[index]
  if (!item) return
  
  let progress = 0
  const interval = setInterval(() => {
    progress += Math.random() * 20
    if (progress >= 100) {
      progress = 100
      clearInterval(interval)
      item.progress = 100
      handleUploadComplete(index)
    } else {
      item.progress = Math.floor(progress)
    }
  }, 300)
}

async function handleUploadComplete(index) {
  const item = uploadList.value[index]
  item.status = 'processing'
  
  try {
    await completeUpload({
      uploadId: item.uploadId,
      storageKey: item.storageKey
    })
    
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    item.status = 'success'
    completedCount.value++
    
    completedList.value.push({
      id: item.id,
      fileName: item.fileName,
      thumbnail: generateThumbnail(item),
      category: '校园活动',
      tags: ['AI标签1', 'AI标签2']
    })
    
    ElMessage.success(`${item.fileName} 上传成功`)
  } catch (error) {
    item.status = 'failed'
    ElMessage.error(`${item.fileName} 处理失败`)
  }
}

function retryUpload(index) {
  startUpload(index)
}

function generateThumbnail(item) {
  const prompts = {
    image: 'beautiful campus scenery photography',
    video: 'video thumbnail campus event',
    document: 'document cover design'
  }
  return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(prompts[item.type] || 'media')}&image_size=square`
}

function getFileType(fileName) {
  const ext = fileName.split('.').pop().toLowerCase()
  const imageExts = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
  const videoExts = ['mp4', 'avi', 'mov', 'wmv', 'flv']
  
  if (imageExts.includes(ext)) return 'image'
  if (videoExts.includes(ext)) return 'video'
  return 'document'
}

function formatSize(bytes) {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / 1024 / 1024).toFixed(1) + ' MB'
}

function goDetail(id) {
  router.push(`/media/${id}`)
}

function editMedia(item) {
  ElMessage.info('编辑功能开发中')
}
</script>

<style scoped>
.upload-page {
  max-width: 900px;
  margin: 0 auto;
}

.upload-section {
  margin-bottom: 30px;
}

.upload-dragger {
  background: white;
  border-radius: 16px;
  padding: 20px;
}

.upload-area {
  border-radius: 12px;
}

.upload-icon {
  color: #409eff;
}

.upload-text {
  margin-top: 16px;
  font-size: 16px;
  color: #333;
}

.upload-hint {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.upload-limit {
  margin-top: 12px;
  font-size: 12px;
  color: #999;
}

.upload-queue {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 30px;
}

.queue-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.queue-title {
  font-size: 16px;
  font-weight: 600;
}

.queue-count {
  font-size: 14px;
  color: #666;
}

.queue-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.queue-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.queue-item-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.file-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ecf5ff;
  border-radius: 8px;
}

.file-info {
  min-width: 200px;
}

.file-name {
  font-size: 14px;
  font-weight: 500;
}

.file-size {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.queue-item-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-tag {
  font-size: 12px;
  padding: 4px 12px;
  border-radius: 4px;
}

.status-tag.uploading {
  width: 150px;
}

.status-tag.processing {
  color: #e6a23c;
}

.status-tag.success {
  color: #67c23a;
}

.status-tag.failed {
  color: #f56c6c;
}

.loading-icon {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.complete-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
}

.complete-header {
  margin-bottom: 20px;
}

.complete-title {
  font-size: 16px;
  font-weight: 600;
}

.complete-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.complete-item {
  display: flex;
  gap: 16px;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.complete-thumb {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
}

.complete-info {
  flex: 1;
}

.complete-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 8px;
}

.complete-meta {
  font-size: 12px;
  color: #999;
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-bottom: 12px;
}

.complete-actions {
  display: flex;
  gap: 8px;
}

@media (max-width: 768px) {
  .queue-item {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .queue-item-right {
    width: 100%;
    justify-content: space-between;
  }
  
  .complete-item {
    flex-direction: column;
  }
  
  .complete-thumb {
    width: 100%;
    height: 120px;
  }
}
</style>
