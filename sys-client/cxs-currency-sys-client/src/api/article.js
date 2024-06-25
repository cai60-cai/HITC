import { URL_PREFIX } from '@/api/config'
import request from "@/api/request";
export function saveOrUpdateDraft(data = {}) {
    return request({
        url: URL_PREFIX + '/article/saveOrUpdateDraft',
        method: 'post',
        data
    })
}

export function getArticleDraft() {
    return request({
        url: URL_PREFIX + '/article/getArticleDraft',
        method: 'get'
    })
}

export function deleteDraft() {
    return request({
        url: URL_PREFIX + '/article/deleteDraft',
        method: 'get'
    })
}

export function saveOrUpdateArticle(data = {}) {
    let formData = new FormData()
    console.log(typeof data.file);
    for (let articleFormKey in data) {
        if (articleFormKey === 'file') {
            if (data[articleFormKey] && Object.keys(data[articleFormKey]).length > 0) {
                formData.append(articleFormKey, data[articleFormKey].raw)
            }
        } else if (articleFormKey === 'tags') {
            if (data[articleFormKey] && data[articleFormKey].length > 0) {
                const tagStr = data[articleFormKey].map(r => r.tagId).join(",")
                formData.append(articleFormKey, tagStr)
            }
        } else {
            formData.append(articleFormKey, data[articleFormKey])
        }
    }
    return request({
        url: URL_PREFIX + '/article/saveOrUpdateArticle',
        method: 'post',
        header: {
            'Content-type' : 'multipart/form-data'
        },
        data: formData
    })
}

export function getArticleList(data = {}) {
    return request({
        url: URL_PREFIX + '/main/article/getArticleList',
        method: 'post',
        data
    })
}

export function getArticleMainList(data = {}) {
    return request({
        url: URL_PREFIX + '/main/article/getArticleMainList',
        method: 'post',
        data
    })
}

export function getArticleInfo(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/getArticleInfo',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function getUpdateArticleInfo(articleId) {
    return request({
        url: `${URL_PREFIX}/article/getUpdateArticleInfo/${articleId}`,
        method: 'get',
    })
}

export function getArticleFileInfo(articleId) {
    return request({
        url: `${URL_PREFIX}/article/getArticleFileInfo/${articleId}`,
        method: 'get',
    })
}

export function readArticle(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/readArticle',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function likeArticle(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/likeArticle',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function cancelLikeArticle(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/cancelLikeArticle',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function collectionArticle(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/collectionArticle',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function cancelCollectionArticle(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/cancelCollectionArticle',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function markArticleSearchLog(articleId) {
    return request({
        url: URL_PREFIX + '/main/article/markArticleSearchLog',
        method: 'get',
        params: {
            articleId
        }
    })
}

export function downloadFile(data) {
    return request({
        url: URL_PREFIX + '/article/downloadFile',
        method: 'post',
        responseType: 'blob',
        headers:{ 'Content-Type': 'application/json; application/octet-stream'},
        data
    })
}

export function downloadBeforeValid(data) {
    return request({
        url: URL_PREFIX + '/article/downloadBeforeValid',
        method: 'post',
        data
    })
}

export function downloadFile1(obj, fileInfo) {
    let downloadFileApi = async (url, formData, options) => {
        await request({
            url: url,
            method: 'post',
            responseType: 'arraybuffer',
            params: {
                articleId: obj.articleId,
                fileId: obj.fileId
            }
        }).then(resp => download(resp, options))
    }

    let getFile = async (url, options) => {
        await request.get(url, {responseType: 'blob'}).then(resp => download(resp, options))
    }

    let download = (resp, options) => {
        let blob = new Blob([resp.data], {type: options.fileType ? options.fileType : 'application/octet-binary'})
        //创建下载的链接
        let href = window.URL.createObjectURL(blob)
        downloadBlob(href, options.fileName)
    }

    let downloadBlob = (blobUrl, fileName, revokeObjectURL) => {
        let downloadElement = document.createElement('a')
        downloadElement.href = blobUrl
        //下载后文件名
        downloadElement.download = fileName
        document.body.appendChild(downloadElement)
        //点击下载
        downloadElement.click()
        //下载完成移除元素
        document.body.removeChild(downloadElement)
        if (revokeObjectURL == null || revokeObjectURL) {
            //释放掉blob对象
            window.URL.revokeObjectURL(blobUrl)
        }
    }
    let getDownloadFileUrl = async (url, fileType) => {
        let blob
        await request.get(url, {responseType: 'blob'}).then(resp => {
            blob = new Blob([resp.data], {type: fileType ? fileType : 'application/octet-binary'});
        })
        return window.URL.createObjectURL(blob);
    }

    let getDownloadFileUrlByPost = async (url, data, fileType) => {
        let blob
        await request.post(url, data, {responseType: 'blob'}).then(resp => {
            blob = new Blob([resp.data], {type: fileType ? fileType : 'application/octet-binary'});
        })
        return window.URL.createObjectURL(blob);
    }

    let getDownloadFileBlob = async (url, fileType) => {
        let blob
        await request.get(url, {responseType: 'blob'}).then(resp => {
            blob = new Blob([resp.data], {type: fileType ? fileType : 'application/octet-binary'});
        })
        return blob;
    }
    return downloadFileApi(URL_PREFIX + '/article/downloadFile1', obj, fileInfo.fileName)
}

export function downloadFileBase64(data) {
    return request({
        url: URL_PREFIX + '/article/downloadFileBase64',
        method: 'post',
        data
    })
}
