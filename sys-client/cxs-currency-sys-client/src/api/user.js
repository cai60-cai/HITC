import request from '@/api/request'
import { URL_PREFIX } from '@/api/config'
export function login(data = {}) {
    return request({
        url: URL_PREFIX + '/auth/login',
        method: 'post',
        data
    })
}

export function getValidateCode() {
    return request({
        url: URL_PREFIX + '/auth/getValidateCode',
        method: 'get'
    })
}

export function getInfo() {
    return request({
        url: URL_PREFIX + '/user/getUserInfo',
        method: 'get'
    })
}

export function logout() {
    return request({
        url: URL_PREFIX + '/user/logout',
        method: 'get'
    })
}

/**
 * 获取用户登录日志
 * @param pageNum
 * @param pageSize
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getLoginLog({pageNum, pageSize}) {
    return request({
        url: URL_PREFIX + '/user/getLoginLog',
        method: 'get',
        params: {
            pageNum,
            pageSize
        }
    })
}

/**
 * 获取用户个人资料
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getPersonal() {
    return request({
        url: URL_PREFIX + '/user/getPersonal',
        method: 'get'
    })
}

/**
 * 查询用户名是否存在处理器
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function checkUserNameExist(data) {
    return request({
        url: URL_PREFIX + '/main/user/checkUserNameExist',
        method: 'post',
        data
    })
}
/**
 * 邮箱是否绑定用户超过3个
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function checkEmailBindStatus(data) {
    return request({
        url: URL_PREFIX + '/main/user/checkEmailBindStatus',
        method: 'post',
        data
    })
}

/**
 * 修改用户个人信息
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function updateSelfInfo(data) {
    return request({
        url: URL_PREFIX + '/user/updateSelfInfo',
        method: 'post',
        data
    })
}

/**
 * 检查旧密码
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function checkOldPassword(data) {
    return request({
        url: URL_PREFIX + '/user/checkOldPassword',
        method: 'post',
        data
    })
}

/**
 * 修改密码
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function updatePassword(data) {
    return request({
        url: URL_PREFIX + '/user/updatePassword',
        method: 'post',
        data
    })
}

/**
 * 获取用户个人设置
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getSettingInfo() {
    return request({
        url: URL_PREFIX + '/user/getSettingInfo',
        method: 'get'
    })
}

/**
 * 用户积分功能设置操作
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function operaPointSetting(data = {}) {
    return request({
        url: URL_PREFIX + '/user/operaPointSetting',
        method: 'post',
        data
    })
}

/**
 * 用户打赏功能设置操作
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function operaRewardSetting(data = {}) {
    return request({
        url: URL_PREFIX + '/user/operaRewardSetting',
        method: 'post',
        data
    })
}

export function operaReceiveEmailNoticeSetting(data = {}) {
    return request({
        url: URL_PREFIX + '/user/operaReceiveEmailNoticeSetting',
        method: 'post',
        data
    })
}

/**
 * 更新打赏信息
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function updateRewardImgInfo(data = {}) {
    return request({
        url: URL_PREFIX + '/user/updateRewardImgInfo',
        method: 'post',
        data
    })
}

/**
 * 获取用户收藏列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getArticleCollList(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getArticleCollList',
        method: 'post',
        data
    })
}

/**
 * 删除用户收藏列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function removeArticleColl(id) {
    return request({
        url: URL_PREFIX + '/user/removeArticleColl',
        method: 'get',
        params:{
            id
        }
    })
}

/**
 * 获取用户发布列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getArticlePublishList(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getArticlePublishList',
        method: 'post',
        data
    })
}

/**
 * 获取用户评论列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getMyCommentList(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getMyCommentList',
        method: 'post',
        data
    })
}

/**
 * 获取用户积分交易流水
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getPointTradeFlow(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getPointTradeFlow',
        method: 'post',
        data
    })
}
/**
 * 用户签到
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function userSign() {
    return request({
        url: URL_PREFIX + '/user/sign',
        method: 'get',
    })
}

/**
 * 获取用户举报列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getReportList(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getReportList',
        method: 'post',
        data
    })
}

/**
 * 获取用户举报详情
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getReportInfo(id) {
    return request({
        url: `${URL_PREFIX}/user/getReportInfo/${id}`,
        method: 'get'
    })
}

/**
 * 用户中心删除文章
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function removeMyArticle(articleId) {
    return request({
        url: `${URL_PREFIX}/user/removeMyArticle`,
        method: 'get',
        params:{
            articleId
        }
    })
}

/**
 * 获取用户反馈列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getFeedbackList(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getFeedbackList',
        method: 'post',
        data
    })
}

/**
 * 获取用户订单列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getUserOrderList(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getUserOrderList',
        method: 'post',
        data
    })
}

/**
 * 获取系统消息列表
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getSystemMessage(data = {}) {
    return request({
        url: URL_PREFIX + '/user/getSystemMessage',
        method: 'post',
        data
    })
}

/**
 * 设置系统消息已读
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function readSysMessage(data = {}) {
    return request({
        url: URL_PREFIX + '/user/readSysMessage',
        method: 'post',
        data
    })
}

/**
 * 获取用户反馈详情
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function getFeedbackInfo(id) {
    return request({
        url: `${URL_PREFIX}/user/getFeedbackInfo/${id}`,
        method: 'get'
    })
}
