import { Message } from 'element-ui'

const state = {
    breadcrumbAppend: [],
    showBreadcrumb : false
}

const mutations = {
    SET_BREADCRUMBLIST: (state, breadcrumbAppend) => {
        state.breadcrumbAppend = breadcrumbAppend
    },
    SET_SHOWBREADCRUMB: (state, flag) => {
        state.showBreadcrumb = flag
    },
}

const actions = {
    setbreadcrumbAppend ({ commit }, breadcrumbAppend) {
        commit('SET_BREADCRUMBLIST', breadcrumbAppend)
    },
    setShowBreadcrumb ({ commit }, flag) {
        commit('SET_SHOWBREADCRUMB', flag)
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
