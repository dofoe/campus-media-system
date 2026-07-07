<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="logo-section">
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
          <h1 class="title">校园宣传素材智能管理系统</h1>
          <p class="subtitle">智能归类 · 精准检索 · 安全共享</p>
        </div>
        <div class="feature-list">
          <div class="feature-item">
            <span class="feature-icon">📷</span>
            <span>AI智能识别素材内容</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">🔍</span>
            <span>毫秒级全文检索</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">🔒</span>
            <span>精细化权限管理</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">📊</span>
            <span>完整审计追踪</span>
          </div>
        </div>
      </div>
      <div class="login-right">
        <div class="login-form">
          <h2 class="form-title">欢迎登录</h2>
          <el-form :model="loginForm" ref="loginFormRef" :rules="rules" label-position="top">
            <el-form-item label="用户名" prop="username">
              <el-input 
                v-model="loginForm.username" 
                placeholder="请输入工号/学号"
                prefix-icon="User"
                size="large"
              />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                size="large" 
                class="login-btn"
                :loading="loading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
            <div class="form-footer">
              <span>首次登录请联系管理员获取账号</span>
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { login } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

async function handleLogin() {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(loginForm)
        if (res.data) {
          userStore.setToken(res.data.token)
          userStore.setUserInfo(res.data.userInfo)
          ElMessage.success('登录成功')
          router.push('/home')
        }
      } catch (error) {
        ElMessage.error('登录失败，请检查用户名和密码')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-wrapper {
  display: flex;
  width: 900px;
  height: 500px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
  padding: 60px;
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.logo-section {
  margin-bottom: 40px;
}

.logo {
  width: 64px;
  height: 64px;
  margin-bottom: 20px;
}

.title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  color: #ffffff;
}

.subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
}

.feature-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.feature-icon {
  font-size: 20px;
}

.login-right {
  flex: 1;
  padding: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form {
  width: 100%;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 30px;
  color: #1a1a2e;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 12px;
  color: #999;
}

@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
    height: auto;
  }
  
  .login-left {
    padding: 40px;
  }
  
  .feature-list {
    grid-template-columns: 1fr;
  }
}
</style>
