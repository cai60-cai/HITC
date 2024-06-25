import request from '@/utils/request'
import { URL_PREFIX } from './config'

export function getMenusTree() {
  return request({
    url: URL_PREFIX + '/admin/menu/getTreeMenuByToken',
    method: 'get'
  })
}

export function getRoutes() {
  return request({
    url: URL_PREFIX + '/admin/menu/getAllTreeMenu',
    method: 'get'
  })
}

export function getRoles() {
  return request({
    url: URL_PREFIX + '/admin/role/getRoleListAndTreeMenu',
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: URL_PREFIX + `/admin/role/saveOrUpdateRole`,
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: URL_PREFIX + `/admin/role/saveOrUpdateRole`,
    method: 'post',
    data
  })
}

export function deleteRole(data) {
  return request({
    url: URL_PREFIX + `/admin/role/deleteRoles`,
    method: 'post',
    data
  })
}

export function getRoleList(data = { keyWord: '' }) {
  return request({
    url: URL_PREFIX + `/admin/role/getRoleList`,
    method: 'post',
    data
  })
}
