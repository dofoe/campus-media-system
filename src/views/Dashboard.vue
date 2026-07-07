<template>
  <div class="dashboard-page">
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon upload-icon">
          <el-icon :size="28"><Upload /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.todayUpload }}</div>
          <div class="stat-label">今日上传</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon download-icon">
          <el-icon :size="28"><Download /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.todayDownload }}</div>
          <div class="stat-label">今日下载</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon storage-icon">
          <el-icon :size="28"><Cpu /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.storageUsed }}</div>
          <div class="stat-label">存储占用</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon total-icon">
          <el-icon :size="28"><Folder /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-number">{{ stats.totalMedia }}</div>
          <div class="stat-label">素材总数</div>
        </div>
      </div>
    </div>
    <div class="charts-row">
      <div class="chart-card">
        <div class="chart-header">
          <h3 class="chart-title">上传趋势</h3>
          <el-select v-model="chartPeriod" class="period-select" size="small">
            <el-option label="近7天" value="7" />
            <el-option label="近30天" value="30" />
            <el-option label="近90天" value="90" />
          </el-select>
        </div>
        <div class="chart-content">
          <div class="bar-chart">
            <div 
              v-for="(item, index) in uploadTrend" 
              :key="index" 
              class="bar-item"
            >
              <div class="bar" :style="{ height: item.value + '%' }">
                <div class="bar-tooltip">{{ item.count }} 个</div>
              </div>
              <div class="bar-label">{{ item.label }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="chart-card">
        <div class="chart-header">
          <h3 class="chart-title">分类分布</h3>
        </div>
        <div class="chart-content">
          <div class="pie-chart">
            <div class="pie-container">
              <div class="pie" :style="pieStyle"></div>
              <div class="pie-center">
                <div class="pie-total">{{ stats.totalMedia }}</div>
                <div class="pie-label">素材总数</div>
              </div>
            </div>
            <div class="pie-legend">
              <div 
                v-for="(item, index) in categoryDist" 
                :key="index" 
                class="legend-item"
              >
                <span class="legend-color" :style="{ background: item.color }"></span>
                <span class="legend-label">{{ item.name }}</span>
                <span class="legend-value">{{ item.count }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom-row">
      <div class="table-card">
        <div class="table-header">
          <h3 class="table-title">热门素材 Top 10</h3>
        </div>
        <el-table :data="hotMedia" border>
          <el-table-column prop="rank" label="排名" width="60" />
          <el-table-column prop="fileName" label="素材名称" />
          <el-table-column prop="downloadCount" label="下载次数" width="100" />
          <el-table-column prop="category" label="分类" width="120" />
          <el-table-column prop="uploadTime" label="上传时间" width="150" />
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button size="small" @click="goDetail(row.id)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="table-card">
        <div class="table-header">
          <h3 class="table-title">活跃部门</h3>
        </div>
        <el-table :data="activeDepts" border>
          <el-table-column prop="rank" label="排名" width="60" />
          <el-table-column prop="name" label="部门名称" />
          <el-table-column prop="uploadCount" label="上传数" width="100" />
          <el-table-column prop="downloadCount" label="下载数" width="100" />
          <el-table-column prop="contribution" label="贡献占比" width="100">
            <template #default="{ row }">
              <el-progress :percentage="row.contribution" :stroke-width="6" />
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { UploadFilled, Download, Cpu, Folder } from '@element-plus/icons-vue'
import { getDashboard } from '@/api/admin'

const router = useRouter()

const stats = ref({
  todayUpload: 0,
  todayDownload: 0,
  storageUsed: '0 GB',
  totalMedia: 0
})

const chartPeriod = ref('7')
const uploadTrend = ref([])
const categoryDist = ref([])
const hotMedia = ref([])
const activeDepts = ref([])

const pieStyle = computed(() => {
  let gradient = 'conic-gradient('
  let start = 0
  categoryDist.value.forEach(item => {
    const angle = (item.count / stats.value.totalMedia) * 360
    gradient += `${item.color} ${start}deg ${start + angle}deg, `
    start += angle
  })
  gradient = gradient.slice(0, -2) + ')'
  return { background: gradient }
})

onMounted(() => {
  fetchDashboard()
})

async function fetchDashboard() {
  try {
    const res = await getDashboard()
    if (res.data) {
      stats.value = res.data.stats
      uploadTrend.value = res.data.uploadTrend
      categoryDist.value = res.data.categoryDist
      hotMedia.value = res.data.hotMedia
      activeDepts.value = res.data.activeDepts
    }
  } catch (error) {
    stats.value = mockStats
    uploadTrend.value = mockUploadTrend
    categoryDist.value = mockCategoryDist
    hotMedia.value = mockHotMedia
    activeDepts.value = mockActiveDepts
  }
}

function goDetail(id) {
  router.push(`/media/${id}`)
}

const mockStats = {
  todayUpload: 23,
  todayDownload: 156,
  storageUsed: '2.3 GB',
  totalMedia: 1258
}

const mockUploadTrend = [
  { label: '周一', count: 35, value: 70 },
  { label: '周二', count: 28, value: 56 },
  { label: '周三', count: 45, value: 90 },
  { label: '周四', count: 32, value: 64 },
  { label: '周五', count: 50, value: 100 },
  { label: '周六', count: 18, value: 36 },
  { label: '周日', count: 23, value: 46 }
]

const mockCategoryDist = [
  { name: '校园活动', count: 420, color: '#409eff' },
  { name: '教学教务', count: 310, color: '#67c23a' },
  { name: '荣誉表彰', count: 200, color: '#e6a23c' },
  { name: '校园风景', count: 250, color: '#f56c6c' },
  { name: '师生风采', count: 78, color: '#909399' }
]

const mockHotMedia = [
  { rank: 1, id: 1, fileName: '2024年春季运动会开幕式', downloadCount: 156, category: '校园活动', uploadTime: '2024-05-15' },
  { rank: 2, id: 2, fileName: '新生开学典礼', downloadCount: 142, category: '校园活动', uploadTime: '2024-09-01' },
  { rank: 3, id: 3, fileName: '奖学金颁奖典礼', downloadCount: 128, category: '荣誉表彰', uploadTime: '2024-12-20' },
  { rank: 4, id: 4, fileName: '校园樱花盛开', downloadCount: 115, category: '校园风景', uploadTime: '2024-04-10' },
  { rank: 5, id: 5, fileName: '元旦晚会精彩瞬间', downloadCount: 98, category: '校园活动', uploadTime: '2024-01-01' },
  { rank: 6, id: 6, fileName: '毕业典礼合影', downloadCount: 86, category: '校园活动', uploadTime: '2024-06-25' },
  { rank: 7, id: 7, fileName: '篮球比赛决赛', downloadCount: 75, category: '校园活动', uploadTime: '2024-11-10' },
  { rank: 8, id: 8, fileName: '实验室科研成果展示', downloadCount: 68, category: '教学教务', uploadTime: '2024-06-15' },
  { rank: 9, id: 9, fileName: '校园音乐节现场', downloadCount: 62, category: '校园活动', uploadTime: '2024-05-20' },
  { rank: 10, id: 10, fileName: '图书馆一角', downloadCount: 55, category: '校园风景', uploadTime: '2024-03-15' }
]

const mockActiveDepts = [
  { rank: 1, name: '宣传部', uploadCount: 320, downloadCount: 450, contribution: 35 },
  { rank: 2, name: '教务处', uploadCount: 280, downloadCount: 380, contribution: 28 },
  { rank: 3, name: '计算机学院', uploadCount: 180, downloadCount: 260, contribution: 18 },
  { rank: 4, name: '学生处', uploadCount: 150, downloadCount: 220, contribution: 12 },
  { rank: 5, name: '文学院', uploadCount: 128, downloadCount: 180, contribution: 7 }
]
</script>

<style scoped>
.dashboard-page {
  padding: 0;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-icon {
  background: linear-gradient(135deg, #ecf5ff 0%, #d6e4ff 100%);
  color: #409eff;
}

.download-icon {
  background: linear-gradient(135deg, #f0f9eb 0%, #d9f7be 100%);
  color: #67c23a;
}

.storage-icon {
  background: linear-gradient(135deg, #fff7e6 0%, #ffe5b4 100%);
  color: #e6a23c;
}

.total-icon {
  background: linear-gradient(135deg, #fef0f0 0%, #fbc4c4 100%);
  color: #f56c6c;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a2e;
}

.stat-label {
  font-size: 14px;
  color: #8c8c8c;
  margin-top: 4px;
}

.charts-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.chart-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
}

.period-select {
  width: 120px;
}

.bar-chart {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 200px;
  padding-top: 20px;
}

.bar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.bar {
  width: 40px;
  background: linear-gradient(180deg, #409eff 0%, #66b1ff 100%);
  border-radius: 6px 6px 0 0;
  position: relative;
  transition: height 0.3s ease;
  min-height: 10px;
}

.bar:hover {
  background: linear-gradient(180deg, #66b1ff 0%, #89c4ff 100%);
}

.bar-tooltip {
  position: absolute;
  top: -30px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.2s;
  white-space: nowrap;
}

.bar:hover .bar-tooltip {
  opacity: 1;
}

.bar-label {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}

.pie-chart {
  display: flex;
  align-items: center;
  gap: 30px;
}

.pie-container {
  position: relative;
  width: 160px;
  height: 160px;
}

.pie {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.pie-center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80px;
  height: 80px;
  background: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.pie-total {
  font-size: 20px;
  font-weight: 700;
}

.pie-label {
  font-size: 12px;
  color: #999;
}

.pie-legend {
  flex: 1;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 3px;
}

.legend-label {
  flex: 1;
  font-size: 14px;
}

.legend-value {
  font-size: 14px;
  font-weight: 500;
}

.bottom-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.table-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.table-header {
  margin-bottom: 20px;
}

.table-title {
  font-size: 16px;
  font-weight: 600;
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .charts-row {
    grid-template-columns: 1fr;
  }
  
  .bottom-row {
    grid-template-columns: 1fr;
  }
  
  .pie-chart {
    flex-direction: column;
  }
}
</style>
