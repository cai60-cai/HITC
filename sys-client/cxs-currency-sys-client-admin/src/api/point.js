import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getPointRechargeTypeList() {
  return request({
    url: URL_PREFIX + '/admin/rechangeType/getPointRechargeTypeList',
    method: 'get'
  })
}

export function saveOrPointRechargeType(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/rechangeType/saveOrPointRechargeType',
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

export function getPointTradeOrderList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/tradeOrder/getPointTradeOrderList',
    method: 'post',
    data
  })
}
