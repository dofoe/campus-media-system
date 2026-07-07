import { request } from '@/utils/request'

export function initUpload(data) {
  return request({
    url: '/media/upload/init',
    method: 'post',
    data
  })
}

export function completeUpload(data) {
  return request({
    url: '/media/upload/complete',
    method: 'post',
    data
  })
}

export function searchMedia(params) {
  return request({
    url: '/media/search',
    method: 'get',
    params
  })
}

export function getMediaDetail(id) {
  return request({
    url: `/media/${id}`,
    method: 'get'
  })
}

export function getDownloadUrl(id) {
  return request({
    url: `/media/${id}/download`,
    method: 'post'
  })
}

export function deleteMedia(id) {
  return request({
    url: `/media/${id}`,
    method: 'delete'
  })
}

export function updateMedia(id, data) {
  return request({
    url: `/media/${id}`,
    method: 'put',
    data
  })
}

export function batchDeleteMedia(ids) {
  return request({
    url: '/media/batch',
    method: 'delete',
    data: { ids }
  })
}

export function getPendingAudit(params) {
  return request({
    url: '/media/audit/pending',
    method: 'get',
    params
  })
}

export function auditMedia(id, data) {
  return request({
    url: `/media/audit/${id}`,
    method: 'post',
    data
  })
}
