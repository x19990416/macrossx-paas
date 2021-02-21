import request from '@/utils/request'

export function fetchSystem(data) {
  return request({
    url: '/api/system/config/query',
    method: 'get',
    params: data
  })
}

export function createSystem(data) {
  return request({
    url: '/api/system/config/create',
    method: 'post',
    data
  })
}

export function updateSystem(data) {
  return request({
    url: '/api/system/config/update',
    method: 'post',
    data
  })
}

export function fetchModule(data) {
  return request({
    url: '/api/system/module/query',
    method: 'get',
    params: data
  })
}
export function createModule(data) {
  return request({
    url: '/api/system/module/create',
    method: 'post',
    data
  })
}

export function updateModule(data) {
  return request({
    url: '/api/system/module/update',
    method: 'post',
    data
  })
}
