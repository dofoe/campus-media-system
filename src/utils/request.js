import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getMockResponse } from '@/mock'

const USE_MOCK = true

const service = axios.create({
  baseURL: '/api/v1',
  timeout: 30000
})

service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export function request(config) {
  if (USE_MOCK) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        const mockRes = getMockResponse(config.url, config.method, config.data, config.params)
        if (mockRes.code === 200) {
          resolve(mockRes)
        } else {
          ElMessage.error(mockRes.message || '请求失败')
          if (mockRes.code === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
            router.push('/login')
          }
          reject(new Error(mockRes.message || '请求失败'))
        }
      }, 300)
    })
  }
  return service(config)
}

export default service
