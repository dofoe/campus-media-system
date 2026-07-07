import { request } from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getMenus() {
  return request({
    url: '/auth/menus',
    method: 'get'
  })
}
