import request from '@/utils/request'

export function fetchProduct(data) {
  console.log(data)
  return request({
    url: '/api/nf/product/query',
    method: 'get',
    params: data
  })
}

export function fetchIngredient(data) {
  console.log(data)
  return request({
    url: '/api/nf/ingredient/query',
    method: 'get',
    params: data
  })
}
