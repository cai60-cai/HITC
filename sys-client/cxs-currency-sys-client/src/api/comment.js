import request from "@/api/request";
import {URL_PREFIX} from "@/api/config";

/**
 * 删除评论
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function deleteComment(commentId) {
    return request({
        url: URL_PREFIX + '/article-comment/deleteComment',
        method: 'get',
        params:{commentId}
    })
}

/**
 * 发布评论
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function publishComment(data = {}) {
    return request({
        url: URL_PREFIX + '/article-comment/publishComment',
        method: 'post',
        data
    })
}

/**
 * 点赞评论
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function likeComment(data = {}) {
    return request({
        url: URL_PREFIX + '/article-comment/likeComment',
        method: 'post',
        data
    })
}
/**
 * 取消点赞评论
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function unLikeComment(data = {}) {
    return request({
        url: URL_PREFIX + '/article-comment/unLikeComment',
        method: 'post',
        data
    })
}

/**
 * 操作评论
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function operaComment(data = {}) {
    return request({
        url: URL_PREFIX + '/article-comment/operaComment',
        method: 'post',
        data
    })
}
