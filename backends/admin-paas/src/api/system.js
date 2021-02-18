import request from '@/utils/request'

export function fetchSystemConfigs(data) {
  return request({
    url: '/api/system/config/query',
    method: 'get',
    params : data
  })
}

export function createUser(data) {
  return request({
    url: '/api/'
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-element-admin/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}
