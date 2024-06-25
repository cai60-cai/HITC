import { Message } from 'element-ui'
import {getInfo, login, logout} from "@/api/user";
import {removeToken, setToken} from "@/utils/auth";
import router from "@/router";
import {TO_SYS_INDEX_PAGE_PRE} from "@/utils/constant";

const state = {
    token: '',
    user: {},
    isLogin: false,
    loginFlag: false
}

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    REMOVE_TOKEN: (state) => {
        state.token = ''
    },
    SET_USER: (state, user) => {
        state.user = user
    },
    SET_LOGIN_STATUS: (state, status) => {
        state.isLogin = status
    },
    SET_LOGIN_FLAG: (state, status) => {
        state.loginFlag = status
    },


}

const actions = {
    setToken ({ commit }, token) {
        commit('SET_TOKEN', token)
    },
    setLoginFlag({ commit }, flag){
        commit('SET_LOGIN_FLAG', flag)
    },
    setLoginStatus({ commit }, flag){
        commit('SET_LOGIN_STATUS', flag)
    },
    login({ commit }, userInfo) {
        const { username, password, code, codeKey } = userInfo
        return new Promise((resolve, reject) => {
            login({ username: username.trim(), password: password, code: code.trim(), codeKey: codeKey.trim() }).then(response => {
                const { code, data, msg } = response
                if (code === 200) {
                    commit('SET_TOKEN', data.tokenInfo.token)
                    commit('SET_LOGIN_STATUS', true)
                    commit('SET_LOGIN_FLAG', false)
                    setToken(data.tokenInfo.token)
                    Message.success('登陆成功')
                    resolve(data)
                } else {
                    Message.error(msg)
                }
            }).catch(error => {
                reject(error)
            })
        })
    },

    // get user info
    getInfo({ commit }) {
        return new Promise((resolve, reject) => {
            getInfo().then(response => {
                const { code, data } = response
                if (code === 200) {
                    commit('SET_USER', data)
                    commit('SET_LOGIN_STATUS', true)
                    resolve(data)
                } else {
                    reject('用户登录信息已失效，请重新登陆')
                }
            }).catch(error => {
                reject(error)
            })
        })
    },

    // user logout
    logout({ commit, state, dispatch }) {
        return new Promise((resolve, reject) => {
            logout().then(() => {
                commit('SET_TOKEN', '')
                commit('SET_USER', {})
                commit('SET_LOGIN_STATUS', false)
                removeToken()
                if (TO_SYS_INDEX_PAGE_PRE.includes(router.currentRoute.path)) {
                    router.replace("/")
                }
                resolve(true)
            }).catch(error => {
                reject(error)
            })
        })
    },
    resetToken({ commit }) {
        return new Promise(resolve => {
            commit('SET_TOKEN', '')
            commit('SET_USER', {})
            removeToken()
            resolve()
        })
    },
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
