import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getTagList() {
  return request({
    url: URL_PREFIX + '/admin/tag/getTagList',
    method: 'get'
  })
}

export function saveOrUpdateTag(data) {
  return request({
    url: URL_PREFIX + '/admin/tag/saveOrUpdateTag',
    method: 'post',
    data
  })
}

export function deleteTag(id) {
  return request({
    url: URL_PREFIX + `/admin/tag/deleteTag/${id}`,
    method: 'delete'
  })
}
