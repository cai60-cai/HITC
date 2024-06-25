export function getCache(key) {
    let str = localStorage.getItem(key)
    if (str) {
        return JSON.parse(str)
    } else {
        return null
    }
}

export function setCache(key, value) {
    return localStorage.setItem(key, JSON.stringify(value))
}

export function removeCache(key) {
    return localStorage.removeItem(key)
}
