import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getReviewedList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/article/getReviewedList',
    method: 'post',
    data
  })
}

export function getArticleList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/article/getArticleList',
    method: 'post',
    data
  })
}

export function reviewedArticle(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/article/reviewedArticle',
    method: 'post',
    data
  })
}

export function updateArticle(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/article/updateArticle',
    method: 'post',
    data
  })
}

export function getArticleInfo(articleId) {
  return request({
    url: URL_PREFIX + `/admin/article/getArticleInfo/${articleId}`,
    method: 'get'
  })
}

export function deleteArticle(articleId) {
  return request({
    url: URL_PREFIX + `/admin/article/deleteArticle/${articleId}`,
    method: 'delete'
  })
}

export function fetchList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/article/fetchList',
    method: 'post',
    data
  })
}
