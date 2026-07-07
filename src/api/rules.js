import { request } from '@/utils/request'

export function getRules(params) {
  return request({
    url: '/admin/rules',
    method: 'get',
    params
  })
}

export function createRule(data) {
  return request({
    url: '/admin/rules',
    method: 'post',
    data
  })
}

export function updateRule(id, data) {
  return request({
    url: `/admin/rules/${id}`,
    method: 'put',
    data
  })
}

export function deleteRule(id) {
  return request({
    url: `/admin/rules/${id}`,
    method: 'delete'
  })
}

export function testRule(data) {
  return request({
    url: '/admin/rules/test',
    method: 'post',
    data
  })
}
