import request from '@/utils/request'

export function login(username, password, code, uuid) {
  return request({
    url: 'api/auth/login/pwd',
    method: 'post',
    data: {
      username,
      password,
      code,
      uuid
    }
  })
}

export function getInfo() {
  return request({
    url: 'api/user/info',
    method: 'get'
  })
}

export function getCodeImg() {
  return request({
    url: 'api/auth/code',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'delete'
  })
}
