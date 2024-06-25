import request from '@/api/request'
import { URL_PREFIX } from '@/api/config'
export function fileUpload(file) {
    let data = new FormData()
    data.append("file", file)
    return request({
        url: URL_PREFIX + '/base/file/upload',
        method: 'post',
        header: {
            'Content-type' : 'multipart/form-data'
        },
        data
    })
}

export function fileDownload(fileId) {
    return request({
        url: URL_PREFIX + '/base/file/download',
        method: 'post',
        responseType: 'blob',
        params: {
            fileId
        }
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
