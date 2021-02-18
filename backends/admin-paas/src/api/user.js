import request from '@/utils/request'

export function fetchUsers(data) {
  return request({
    url: '/api/users/query',
    method: 'get',
    params: data
  })
}

export function createUser(data) {
  return request({
    url: '/api/users/create',
    method: 'post',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: '/api/users/delete',
    method: 'post',
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
