import request from '@/api/request'
import { URL_PREFIX } from '@/api/config'
export function getTechnologyType() {
  return request({
    url: URL_PREFIX + '/main/type/getTechnologyType',
    method: 'get'
  })
}

export function getTagList() {
  return request({
    url: URL_PREFIX + '/main/tag/getTagList',
    method: 'get'
  })
}

export function getExternalLinkList() {
  return request({
    url: URL_PREFIX + '/main/link/getExternalLinkList',
    method: 'get'
  })
}

export function getLinkList() {
  return request({
    url: URL_PREFIX + '/main/nav/getLinkList',
    method: 'get'
  })
}

export function getAboutSysInfo() {
  return request({
    url: URL_PREFIX + '/main/system/getAboutSysInfo',
    method: 'get'
  })
}

export function getUserRewardInfo(articleId) {
  return request({
    url: URL_PREFIX + '/main/reward/getUserRewardInfo',
    method: 'get',
    params: {
      articleId
    }
  })
}
export function getHotArticleList() {
  return request({
    url: URL_PREFIX + '/main/article/getHotArticleList',
    method: 'get'
  })
}

export function getSystemRecommendArticleList(data = {pageNum: -1}) {
  return request({
    url: URL_PREFIX + '/main/article/getSystemRecommendArticleList',
    method: 'post',
    data
  })
}

export function getPushNoticeList(type = 0) {
  return request({
    url: URL_PREFIX + '/main/notice/getPushNoticeList',
    method: 'get',
    params: {
      type
    }
  })
}

export function getNoticeInfo(id) {
  return request({
    url: URL_PREFIX + '/main/notice/getNoticeInfo',
    method: 'get',
    params: {
      id
    }
  })
}

export function getSystemInfo() {
  return request({
    url: URL_PREFIX + '/main/system/getSystemInfo',
    method: 'get'
  })
}

export function questionList() {
  return request({
    url: URL_PREFIX + '/main/question/questionList',
    method: 'get'
  })
}

export function questionInfo(id) {
  return request({
    url: `${URL_PREFIX}/main/question/questionInfo/${id}`,
    method: 'get'
  })
}

export function sendEmailCode(data = {}) {
  return request({
    url: URL_PREFIX + '/main/user/sendEmailCode',
    method: 'post',
    data
  })
}

export function register(data = {}) {
  return request({
    url: URL_PREFIX + '/main/user/register',
    method: 'post',
    data
  })
}

/**
 * 获取评论列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getArticleCommentList(data = {}) {
  return request({
    url: URL_PREFIX + '/main/comment/getArticleCommentList',
    method: 'post',
    data
  })
}

/**
 * 获取积分类型
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getPointRechargeTypeList() {
  return request({
    url: URL_PREFIX + '/main/rechangeType/getPointRechargeTypeList',
    method: 'get'
  })
}
