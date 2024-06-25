import request from '@/utils/request'
import { URL_PREFIX } from './config'
export function login(data) {
  return request({
    url: URL_PREFIX + '/auth/admin/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: URL_PREFIX + '/admin/auth/getUserInfo',
    method: 'post'
  })
}

export function logout() {
  return request({
    url: URL_PREFIX + '/user/logout',
    method: 'get'
  })
}

export function getUserList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/getUserList',
    method: 'post',
    data
  })
}

export function authRoleToUser(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/role/authRoleToUser',
    method: 'post',
    data
  })
}

export function updateStatus(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/updateStatus',
    method: 'post',
    data
  })
}

export function createUser(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/createUser',
    method: 'post',
    data
  })
}

export function getUserHasRoleId(userId) {
  return request({
    url: URL_PREFIX + '/admin/role/getRoleListByUserId',
    method: 'get',
    params: {
      userId
    }
  })
}

export function deleteUser(userId) {
  return request({
    url: URL_PREFIX + `/admin/user/deleteUser/${userId}`,
    method: 'delete'
  })
}

export function resetUserPwd(userId) {
  return request({
    url: URL_PREFIX + '/admin/user/resetUserPwd',
    method: 'get',
    params: {
      userId
    }
  })
}

export function checkUserPwd(password) {
  return request({
    url: URL_PREFIX + '/admin/user/checkUserPwd',
    method: 'post',
    params: {
      password
    }
  })
}

export function updateUserPwd(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/updateUserPwd',
    method: 'post',
    data
  })
}

export function adminGetSimpleUserList(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/adminGetSimpleUserList',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: URL_PREFIX + '/admin/user/getUserInfo',
    method: 'get'
  })
}

export function updateUserInfo(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/updateUserInfo',
    method: 'post',
    data
  })
}

export function getUserAuth(userId) {
  return request({
    url: URL_PREFIX + '/admin/user/getUserAuth',
    method: 'get',
    params: {
      userId
    }
  })
}

export function updateUserAuth(data = {}) {
  return request({
    url: URL_PREFIX + '/admin/user/updateUserAuth',
    method: 'post',
    data
  })
}
