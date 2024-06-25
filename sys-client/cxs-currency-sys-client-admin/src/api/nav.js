import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getLinkList() {
  return request({
    url: URL_PREFIX + '/admin/nav/getLinkList',
    method: 'get'
  })
}

export function createOrUpdateNavLink(data) {
  return request({
    url: URL_PREFIX + '/admin/nav/createOrUpdateNavLink',
    method: 'post',
    data
  })
}

export function navLinkOrderMoveOrToppingOrDel(data) {
  return request({
    url: URL_PREFIX + '/admin/nav/navLinkOrderMoveOrToppingOrDel',
    method: 'post',
    data
  })
}
