<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo-container" @click="goHome">
        <div class="logo">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect width="48" height="48" rx="12" fill="url(#gradient)"/>
            <path d="M14 20L24 30L34 20" stroke="white" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M18 26L24 32L30 26" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            <defs>
              <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stop-color="#409eff"/>
                <stop offset="100%" stop-color="#67c23a"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <span v-if="!isCollapse" class="logo-text">素材管理系统</span>
      </div>
      <el-menu 
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#0d1117"
        text-color="#c9d1d9"
        active-text-color="#409eff"
        class="aside-menu"
      >
        <template v-for="menu in menuList" :key="menu.path">
          <el-menu-item v-if="!menu.children" :index="menu.path">
            <template #icon>
              <el-icon :size="20"><component :is="menu.icon" /></el-icon>
            </template>
            <span>{{ menu.name }}</span>
          </el-menu-item>
          <el-sub-menu v-else :index="menu.path">
            <template #title>
              <el-icon :size="20"><component :is="menu.icon" /></el-icon>
              <span>{{ menu.name }}</span>
            </template>
            <el-menu-item v-for="child in menu.children" :key="child.path" :index="child.path">
              <span>{{ child.name }}</span>
            </el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon 
            class="collapse-btn" 
            :size="24" 
            @click="toggleCollapse"
          >
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon :size="18"><User /></el-icon>
              <span>{{ userInfo.name || '用户' }}</span>
              <el-icon :size="16"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  House, 
  UploadFilled, 
  Grid, 
  Setting, 
  UserFilled, 
  Document,
  Fold, 
  Expand, 
  User, 
  ArrowDown 
} from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isCollapse = ref(false)
const userInfo = computed(() => userStore.userInfo)

const menuList = ref([
  { path: '/home', name: '首页', icon: House },
  { path: '/upload', name: '上传素材', icon: UploadFilled },
  { 
    path: '/dashboard', 
    name: '管理后台', 
    icon: Grid,
    children: [
      { path: '/dashboard', name: '数据概览' },
      { path: '/admin/media', name: '素材管理' },
      { path: '/admin/rules', name: '规则配置' },
      { path: '/admin/users', name: '用户管理' },
      { path: '/admin/audit', name: '审计日志' }
    ]
  }
])

const activeMenu = computed(() => route.path)

function toggleCollapse() {
  isCollapse.value = !isCollapse.value
}

function goHome() {
  router.push('/home')
}

function handleCommand(command) {
  if (command === 'logout') {
    userStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } else if (command === 'profile') {
    ElMessage.info('个人中心功能开发中')
  }
}

onMounted(() => {
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  z-index: 100;
  transition: width 0.3s ease;
}

.logo-container {
  display: flex;
  align-items: center;
  padding: 20px;
  gap: 12px;
  cursor: pointer;
}

.logo {
  width: 40px;
  height: 40px;
  flex-shrink: 0;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #ffffff;
  white-space: nowrap;
}

.aside-menu {
  height: calc(100vh - 80px);
  overflow-y: auto;
}

.header {
  position: fixed;
  right: 0;
  top: 0;
  left: 220px;
  height: 60px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 99;
  transition: left 0.3s ease;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.collapse-btn {
  cursor: pointer;
  color: #666;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.2s;
}

.collapse-btn:hover {
  background-color: #f0f0f0;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 20px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f0f0f0;
}

.main {
  margin-left: 220px;
  margin-top: 60px;
  min-height: calc(100vh - 60px);
  padding: 24px;
  transition: margin-left 0.3s ease;
}

:deep(.el-menu-item.is-active) {
  background-color: rgba(64, 158, 255, 0.15) !important;
}

:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.05) !important;
}

:deep(.el-sub-menu__title:hover) {
  background-color: rgba(255, 255, 255, 0.05) !important;
}

:deep(.el-sub-menu.is-active .el-sub-menu__title) {
  background-color: rgba(64, 158, 255, 0.15) !important;
}

.aside.collapsed {
  width: 64px;
}

.aside.collapsed ~ .el-container .header {
  left: 64px;
}

.aside.collapsed ~ .el-container .main {
  margin-left: 64px;
}
</style>
