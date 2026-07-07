import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getMockResponse } from '@/mock'

const USE_MOCK = true

function parseRequestData(data) {
  if (typeof data === 'string') {
    try {
      return JSON.parse(data)
    } catch {
      return data
    }
  }
  return data
}

const mockAdapter = (config) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      const mockRes = getMockResponse(
        config.url,
        config.method,
        parseRequestData(config.data),
        config.params
      )
      resolve({
        data: mockRes,
        status: 200,
        statusText: 'OK',
        headers: {},
        config,
        request: {}
      })
    }, 300)
  })
}

const service = axios.create({
  baseURL: '/api/v1',
  timeout: 30000,
  adapter: USE_MOCK ? mockAdapter : undefined
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
  return service(config)
}

export default service
