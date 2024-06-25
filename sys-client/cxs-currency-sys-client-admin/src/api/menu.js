import request from '@/utils/request'
import { URL_PREFIX } from './config'

export function getAllMenuTreeList() {
  return request({
    url: URL_PREFIX + '/admin/menu/getAllTreeMenu',
    method: 'get'
  })
}

export function createMenu(data) {
  return request({
    url: URL_PREFIX + '/admin/menu/saveOrUpdateRole',
    method: 'post',
    data
  })
}

export function updateMenu(data) {
  return request({
    url: URL_PREFIX + '/admin/menu/saveOrUpdateRole',
    method: 'post',
    data
  })
}

export function deleteMenus(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/menu/deleteMenus',
    method: 'post',
    data
  })
}

