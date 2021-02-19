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
