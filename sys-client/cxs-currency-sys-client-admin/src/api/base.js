import request from '@/utils/request'
import { URL_PREFIX } from '@/api/config'
export function fileUpload(file) {
  const data = new FormData()
  data.append('file', file)
  return request({
    url: URL_PREFIX + '/base/file/upload',
    method: 'post',
    header: {
      'Content-type': 'multipart/form-data'
    },
    data
  })
}
