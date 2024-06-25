import request from '@/utils/request'
import { URL_PREFIX } from './config'

/**
 * 新增or修改问题
 * @param data
 * @returns {AxiosPromise}
 */
export function saveOrUpdateQuestion(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/question/saveOrUpdateQuestion',
    method: 'post',
    data
  })
}

/**
 * 获取问题列表
 * @param data
 * @returns {AxiosPromise}
 */
export function getQuestionList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/question/getQuestionList',
    method: 'post',
    data
  })
}

/**
 * 获取问题详情
 * @param id
 * @returns {AxiosPromise}
 */
export function getQuestionInfo(id) {
  return request({
    url: `${URL_PREFIX}/admin/question/getQuestionInfo/${id}`,
    method: 'get'
  })
}

/**
 * 删除问题
 * @param id
 * @returns {AxiosPromise}
 */
export function removeQuestion(id) {
  return request({
    url: URL_PREFIX + `/admin/question/removeQuestion/${id}`,
    method: 'delete'
  })
}
