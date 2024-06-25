import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function adminGetReportList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/report/adminGetReportList',
    method: 'post',
    data
  })
}

export function handleReport(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/report/adminHandleReport',
    method: 'post',
    data
  })
}

export function removeReport(id) {
  return request({
    url: URL_PREFIX + `/admin/report/removeReport/${id}`,
    method: 'delete'
  })
}

export function adminGetReportInfo(id) {
  return request({
    url: URL_PREFIX + `/admin/report/adminGetReportInfo/${id}`,
    method: 'get'
  })
}

export function adminGetHandleType(handleType) {
  return request({
    url: URL_PREFIX + `/admin/report/adminGetHandleType`,
    method: 'get',
    params: {
      handleType
    }
  })
}
