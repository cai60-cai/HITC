import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getNoticeList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/notice/getNoticeList',
    method: 'post',
    data
  })
}

export function getNoticeInfo(id) {
  return request({
    url: URL_PREFIX + '/admin/notice/getNoticeInfo',
    method: 'get',
    params: {
      id
    }
  })
}

export function saveOrUpdateNotice(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/notice/saveOrUpdateNotice',
    method: 'post',
    data
  })
}

export function deleteNotice(id) {
  return request({
    url: URL_PREFIX + `/admin/notice/deleteNotice/${id}`,
    method: 'post'
  })
}
