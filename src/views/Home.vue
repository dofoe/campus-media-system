<template>
  <div class="home-page">
    <div class="search-section">
      <div class="search-box-wrapper">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索素材（支持文件名、标签、描述）"
          size="large"
          class="search-input"
          @keyup.enter="handleSearch"
          @input="handleInput"
        >
          <template #prefix>
            <el-icon :size="20"><Search /></el-icon>
          </template>
          <template #suffix>
            <el-button type="primary" size="large" @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
        <div v-if="showHistory" class="search-history">
          <div class="history-header">
            <span>搜索历史</span>
            <span class="clear-history" @click="clearHistory">清空</span>
          </div>
          <div class="history-list">
            <el-tag 
              v-for="(item, index) in searchHistory" 
              :key="index"
              closable
              @close="removeHistory(item)"
              @click="selectHistory(item)"
            >
              {{ item }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>
    <div class="filter-section">
      <div class="category-tree">
        <el-tree 
          :data="categories" 
          :props="treeProps"
          :default-expand-all="true"
          node-key="id"
          :highlight-current="true"
          @node-click="handleCategoryClick"
          class="category-tree-inner"
        />
      </div>
      <div class="filter-panel">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="部门">
            <el-select v-model="filterForm.dept" placeholder="请选择部门" clearable>
              <el-option v-for="dept in departments" :key="dept.id" :label="dept.name" :value="dept.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker 
              v-model="filterForm.dateRange" 
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="文件格式">
            <el-select v-model="filterForm.format" placeholder="请选择格式" clearable>
              <el-option label="图片" value="image" />
              <el-option label="视频" value="video" />
              <el-option label="文档" value="document" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">筛选</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="media-grid">
      <div 
        v-for="media in mediaList" 
        :key="media.id" 
        class="media-card"
        @click="goDetail(media.id)"
      >
        <div class="media-thumb">
          <img :src="media.thumbnailUrl" :alt="media.fileName" />
          <div v-if="media.type === 'video'" class="video-badge">
            <el-icon :size="24"><VideoPlay /></el-icon>
          </div>
          <div class="media-overlay">
            <el-button size="small" icon="View" @click.stop="previewMedia(media)">预览</el-button>
            <el-button size="small" icon="Download" @click.stop="downloadMedia(media)">下载</el-button>
          </div>
        </div>
        <div class="media-info">
          <div class="media-name">{{ highlightKeyword(media.fileName) }}</div>
          <div class="media-meta">
            <span>{{ media.uploader }}</span>
            <span>{{ formatDate(media.uploadTime) }}</span>
          </div>
          <div class="media-tags">
            <el-tag 
              v-for="tag in media.tags.slice(0, 3)" 
              :key="tag" 
              size="small"
              type="info"
            >
              {{ highlightKeyword(tag) }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>
    <div v-if="mediaList.length === 0" class="empty-state">
      <el-empty description="暂无素材，点击上传按钮添加" />
    </div>
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[12, 24, 48, 96]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, VideoPlay } from '@element-plus/icons-vue'
import { useMediaStore } from '@/store/media'
import { searchMedia, getDownloadUrl } from '@/api/media'

const router = useRouter()
const mediaStore = useMediaStore()

const searchKeyword = ref('')
const showHistory = ref(false)
const searchHistory = ref(mediaStore.searchHistory)

const categories = ref([
  { id: 0, label: '全部素材', children: [
    { id: 1, label: '校园活动' },
    { id: 2, label: '教学教务' },
    { id: 3, label: '荣誉表彰' },
    { id: 4, label: '校园风景' },
    { id: 5, label: '师生风采' }
  ]},
  { id: 10, label: '我的上传' }
])

const treeProps = {
  children: 'children',
  label: 'label'
}

const departments = ref([
  { id: 1, name: '宣传部' },
  { id: 2, name: '教务处' },
  { id: 3, name: '学生处' },
  { id: 4, name: '计算机学院' },
  { id: 5, name: '文学院' },
  { id: 6, name: '商学院' }
])

const filterForm = reactive({
  dept: '',
  dateRange: [],
  format: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 12,
  total: 0
})

const mediaList = ref([])

onMounted(() => {
  fetchMedia()
})

async function fetchMedia() {
  try {
    const params = {
      keyword: searchKeyword.value,
      categoryId: mediaStore.selectedCategory,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...filterForm
    }
    if (filterForm.dateRange && filterForm.dateRange.length === 2) {
      params.startDate = filterForm.dateRange[0]
      params.endDate = filterForm.dateRange[1]
    }
    const res = await searchMedia(params)
    mediaList.value = res.data.list || mockMediaList
    pagination.total = res.data.total || mockMediaList.length
  } catch (error) {
    mediaList.value = mockMediaList
    pagination.total = mockMediaList.length
  }
}

function handleSearch() {
  pagination.pageNum = 1
  showHistory.value = false
  if (searchKeyword.value.trim()) {
    mediaStore.addSearchHistory(searchKeyword.value.trim())
    searchHistory.value = mediaStore.searchHistory
  }
  fetchMedia()
}

function handleInput() {
  showHistory.value = !!searchKeyword.value
}

function clearHistory() {
  mediaStore.clearSearchHistory()
  searchHistory.value = []
}

function removeHistory(item) {
  searchHistory.value = searchHistory.value.filter(h => h !== item)
}

function selectHistory(item) {
  searchKeyword.value = item
  showHistory.value = false
  handleSearch()
}

function handleCategoryClick(data) {
  mediaStore.setSelectedCategory(data.id)
  handleSearch()
}

function resetFilter() {
  filterForm.dept = ''
  filterForm.dateRange = []
  filterForm.format = ''
  handleSearch()
}

function highlightKeyword(text) {
  if (!searchKeyword.value) return text
  const reg = new RegExp(`(${searchKeyword.value})`, 'gi')
  return text.replace(reg, '<span class="search-highlight">$1</span>')
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

function goDetail(id) {
  router.push(`/media/${id}`)
}

function previewMedia(media) {
  goDetail(media.id)
}

async function downloadMedia(media) {
  try {
    const res = await getDownloadUrl(media.id)
    if (res.data && res.data.signedUrl) {
      const link = document.createElement('a')
      link.href = res.data.signedUrl
      link.download = media.fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      ElMessage.success('下载开始')
    }
  } catch (error) {
    ElMessage.error('获取下载链接失败')
  }
}

const mockMediaList = [
  { id: 1, fileName: '2024年春季运动会开幕式', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20sports%20meeting%20opening%20ceremony%20students%20in%20uniform&image_size=square', type: 'video', uploader: '张三', uploadTime: '2024-05-15', tags: ['运动会', '开幕式', '红幅'] },
  { id: 2, fileName: '校园樱花盛开', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=beautiful%20cherry%20blossom%20in%20campus%20spring&image_size=square', type: 'image', uploader: '李四', uploadTime: '2024-04-10', tags: ['校园风景', '樱花', '春天'] },
  { id: 3, fileName: '新生开学典礼', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20freshman%20opening%20ceremony%20auditorium&image_size=square', type: 'video', uploader: '王五', uploadTime: '2024-09-01', tags: ['开学典礼', '新生', '校园活动'] },
  { id: 4, fileName: '实验室科研成果展示', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=scientific%20laboratory%20research%20equipment%20students&image_size=square', type: 'image', uploader: '赵六', uploadTime: '2024-06-15', tags: ['实验室', '科研', '教学教务'] },
  { id: 5, fileName: '奖学金颁奖典礼', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=scholarship%20award%20ceremony%20students%20receiving%20trophies&image_size=square', type: 'video', uploader: '钱七', uploadTime: '2024-12-20', tags: ['奖学金', '表彰', '荣誉'] },
  { id: 6, fileName: '校园音乐节现场', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20music%20festival%20stage%20concert%20students&image_size=square', type: 'image', uploader: '孙八', uploadTime: '2024-05-20', tags: ['音乐节', '活动', '学生'] },
  { id: 7, fileName: '毕业典礼合影', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20graduation%20ceremony%20students%20in%20gowns&image_size=square', type: 'image', uploader: '周九', uploadTime: '2024-06-25', tags: ['毕业', '典礼', '合影'] },
  { id: 8, fileName: '篮球比赛决赛', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=college%20basketball%20game%20final%20students%20playing&image_size=square', type: 'video', uploader: '吴十', uploadTime: '2024-11-10', tags: ['篮球', '比赛', '体育'] },
  { id: 9, fileName: '图书馆一角', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20library%20interior%20students%20studying&image_size=square', type: 'image', uploader: '郑十一', uploadTime: '2024-03-15', tags: ['图书馆', '学习', '校园'] },
  { id: 10, fileName: '元旦晚会精彩瞬间', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=university%20new%20year%20party%20stage%20performance&image_size=square', type: 'video', uploader: '王十二', uploadTime: '2024-01-01', tags: ['元旦', '晚会', '表演'] },
  { id: 11, fileName: '校园招聘会现场', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=campus%20job%20fair%20students%20recruiters%20booths&image_size=square', type: 'image', uploader: '李十三', uploadTime: '2024-10-20', tags: ['招聘会', '就业', '校园活动'] },
  { id: 12, fileName: '教职工运动会', thumbnailUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=teacher%20sports%20meeting%20faculty%20competition&image_size=square', type: 'video', uploader: '张十四', uploadTime: '2024-09-25', tags: ['教职工', '运动会', '体育'] }
]
</script>

<style scoped>
.home-page {
  padding: 0;
}

.search-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px;
  border-radius: 16px;
  margin-bottom: 20px;
}

.search-box-wrapper {
  max-width: 800px;
  margin: 0 auto;
  position: relative;
}

.search-input {
  border-radius: 12px;
}

.search-history {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  margin-top: 8px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  padding: 12px;
  z-index: 100;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.clear-history {
  cursor: pointer;
  color: #999;
  font-size: 12px;
}

.clear-history:hover {
  color: #409eff;
}

.history-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-section {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.category-tree {
  width: 200px;
  flex-shrink: 0;
}

.category-tree-inner {
  background: white;
  border-radius: 8px;
  padding: 12px;
}

.filter-panel {
  flex: 1;
}

.filter-form {
  background: white;
  padding: 16px;
  border-radius: 8px;
}

.media-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.media-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
}

.media-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.media-thumb {
  position: relative;
  width: 100%;
  padding-top: 75%;
  overflow: hidden;
}

.media-thumb img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-badge {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 6px;
  border-radius: 50%;
}

.media-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.media-card:hover .media-overlay {
  opacity: 1;
}

.media-info {
  padding: 12px;
}

.media-name {
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.media-meta {
  font-size: 12px;
  color: #999;
  display: flex;
  gap: 12px;
  margin-bottom: 8px;
}

.media-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.empty-state {
  padding: 60px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.search-highlight {
  color: #409eff;
  font-weight: 500;
}

@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
  }
  
  .category-tree {
    width: 100%;
  }
  
  .media-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 12px;
  }
}
</style>
