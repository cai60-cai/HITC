import request from "@/api/request";
import {URL_PREFIX} from "@/api/config";

/**
 * 新增举报
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function addReport(data = {}) {
    return request({
        url: URL_PREFIX + '/report/addReport',
        method: 'post',
        data
    })
}

/**
 * 删除举报
 * @param data
 * @returns {Promise<AxiosResponse<any>>}
 */
export function delReport(id) {
    return request({
        url: `${URL_PREFIX}/report/delReport/${id}` ,
        method: 'delete',
    })
}
