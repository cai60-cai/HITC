import request from '@/api/request'
import { URL_PREFIX } from '@/api/config'
export function pay(data = {id: 1}) {
    return request({
        url: URL_PREFIX + '/rechangeType/trade',
        method: 'post',
        data
    })
}
