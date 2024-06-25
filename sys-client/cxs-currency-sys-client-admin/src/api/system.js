import request from '@/utils/request'
import { URL_PREFIX } from '@/api/config'

export function getSystemInfoInfo() {
  return request({
    url: URL_PREFIX + `/admin/system-info/getSystemInfoInfo`,
    method: 'post'
  })
}

export function updateSystemInfo(data = {}) {
  return request({
    url: URL_PREFIX + `/admin/system-info/updateSystemInfo`,
    method: 'post',
    data
  })
}
