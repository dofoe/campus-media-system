import { request } from '@/utils/request'

export function getUsers(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

export function createUser(data) {
  return request({
    url: '/admin/users',
    method: 'post',
    data
  })
}

export function updateUser(id, data) {
  return request({
    url: `/admin/users/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/admin/users/${id}`,
    method: 'delete'
  })
}

export function resetPassword(id, data) {
  return request({
    url: `/admin/users/${id}/password`,
    method: 'post',
    data
  })
}

export function getRoles() {
  return request({
    url: '/admin/roles',
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/admin/roles',
    method: 'post',
    data
  })
}

export function updateRole(id, data) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/admin/roles/${id}`,
    method: 'delete'
  })
}

export function getDownloadLogs(params) {
  return request({
    url: '/admin/audit/download',
    method: 'get',
    params
  })
}

export function getOperationLogs(params) {
  return request({
    url: '/admin/audit/operation',
    method: 'get',
    params
  })
}

export function getDashboard() {
  return request({
    url: '/admin/dashboard',
    method: 'get'
  })
}

export function getCategories() {
  return request({
    url: '/admin/categories',
    method: 'get'
  })
}

export function createCategory(data) {
  return request({
    url: '/admin/categories',
    method: 'post',
    data
  })
}

export function updateCategory(id, data) {
  return request({
    url: `/admin/categories/${id}`,
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/admin/categories/${id}`,
    method: 'delete'
  })
}
