import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getExternalFriendLinkList(data) {
  return request({
    url: URL_PREFIX + '/admin/external-link/getExternalFriendLinkList',
    method: 'post',
    data
  })
}

export function saveOrUpdateExternalFriendLink(data) {
  return request({
    url: URL_PREFIX + '/admin/external-link/saveOrUpdateExternalFriendLink',
    method: 'post',
    data
  })
}

export function approveExternalFriendLink(data) {
  return request({
    url: URL_PREFIX + '/admin/external-link/approveExternalFriendLink',
    method: 'post',
    data
  })
}

export function deleteExternalFriendLink(id) {
  return request({
    url: URL_PREFIX + `/admin/external-link/deleteExternalFriendLink/${id}`,
    method: 'delete'
  })
}
