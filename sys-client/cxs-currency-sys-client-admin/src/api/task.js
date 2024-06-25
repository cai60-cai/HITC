import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function getTaskList(keyword) {
  return request({
    url: URL_PREFIX + '/admin/task/getTaskList',
    method: 'post',
    params: {
      keyword
    }
  })
}

export function saveOrUpdateTask(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/task/saveOrUpdateTask',
    method: 'post',
    data
  })
}

export function taskOperaHandle(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/task/taskOperaHandle',
    method: 'post',
    data
  })
}

export function removeTask(taskId) {
  return request({
    url: URL_PREFIX + `/admin/task/removeTask/${taskId}`,
    method: 'delete'
  })
}

export function executeTask(taskId) {
  return request({
    url: URL_PREFIX + `/admin/task/executeTask/${taskId}`,
    method: 'get'
  })
}

export function getTaskLog(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/task/getTaskLog',
    method: 'post',
    data
  })
}
