import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getFeedBackList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/feedback/getFeedBackList',
    method: 'post',
    data
  })
}

export function handleFeedBack(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/feedback/handleFeedBack',
    method: 'post',
    data
  })
}

export function removePointRechargeType(id) {
  return request({
    url: URL_PREFIX + `/admin/rechangeType/removePointRechargeType/${id}`,
    method: 'delete'
  })
}

export function removeFeedBack(id) {
  return request({
    url: URL_PREFIX + `/admin/feedback/removeFeedBack/${id}`,
    method: 'delete'
  })
}

export function adminGetFeedBackInfo(id) {
  return request({
    url: URL_PREFIX + `/admin/feedback/adminGetFeedBackInfo/${id}`,
    method: 'get'
  })
}
