import request from '@/utils/request'

export function fetchUsers(data) {
  return request({
    url: '/api/users/query',
    method: 'get',
    data
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
