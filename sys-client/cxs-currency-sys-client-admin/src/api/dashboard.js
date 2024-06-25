import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getSystemStatisticsInfo() {
  return request({
    url: URL_PREFIX + '/admin/dashboard/getSystemStatisticsInfo',
    method: 'post'
  })
}

export function getSystemRangeStatisticsInfo() {
  return request({
    url: URL_PREFIX + '/admin/dashboard/getSystemRangeStatisticsInfo',
    method: 'post'
  })
}
