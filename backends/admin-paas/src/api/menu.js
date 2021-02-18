import request from '@/utils/request'

export function fetchMenus(data) {
  return request({
    url: '/api/menu/query',
    method: 'get',
    params: data
  })
}

export function fetchRoleMenus(data) {
  return request({
    url: '/api/menu/query/role',
    method: 'get',
    params: data
  })
}

export function createMenu(data) {
  return request({
    url: '/api/menu/create',
    method: 'post',
    params: data
  })
}

export function build(data) {
  return request({
    url: '/api/menu/build',
    method: 'get',
    params: data
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
    params: {token}
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}
