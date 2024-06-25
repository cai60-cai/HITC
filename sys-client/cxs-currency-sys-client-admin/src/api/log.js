import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getLogList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/log/getLogList',
    method: 'post',
    data
  })
}
