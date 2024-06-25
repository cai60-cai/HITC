import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getAdminTechnologyTypeList() {
  return request({
    url: URL_PREFIX + '/admin/technology/getAdminTechnologyTypeList',
    method: 'post'
  })
}

export function createOrUpdateType(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/technology/createOrUpdateType',
    method: 'post',
    data
  })
}

export function deleteAdminTechnologyType(id) {
  return request({
    url: URL_PREFIX + `/admin/technology/deleteAdminTechnologyType/${id}`,
    method: 'delete'
  })
}
