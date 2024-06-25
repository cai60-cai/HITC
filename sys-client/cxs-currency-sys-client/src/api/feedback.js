import request from "@/api/request";
import {URL_PREFIX} from "@/api/config";

/**
 * 新增意见反馈
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function addFeedback(data = {}) {
    return request({
        url: URL_PREFIX + '/feedback/addFeedback',
        method: 'post',
        data
    })
}

/**
 * 删除举报
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function delFeedback(id) {
    return request({
        url: `${URL_PREFIX}/feedback/delFeedback/${id}` ,
        method: 'delete',
    })
}
