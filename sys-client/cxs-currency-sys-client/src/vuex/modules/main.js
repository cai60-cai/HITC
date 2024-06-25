import {
    getAboutSysInfo,
    getHotArticleList, getPushNoticeList,
    getSystemRecommendArticleList,
    getTagList,
    getTechnologyType
} from "@/api/main";
import {getSystemMessage} from "@/api/user";

const state = {
    breadcrumbSelf: [],
    sysInfo: {},
    tagList: [],
    typeList: [],
    typeListArr: [],
    hotSearchArticleList: [],
    sysRecommendArticleList: [],
    simpleNoticeList: [],
    socketConnFlag: false,
    sysMessageList: [],
    sysMessageBean: {},
    unReadMessageNum: 0
}

const mutations = {
    SET_BREADCRUMBLIST: (state, breadcrumbSelf) => {
        state.breadcrumbSelf = breadcrumbSelf
    },
    ADD_BREADCRUMBLIST: (state, breadcrumbSelf) => {
        state.breadcrumbSelf = state.breadcrumbSelf.concat(breadcrumbSelf)
    },
    SET_SYSINFO: (state, sysInfo) => {
        state.sysInfo = sysInfo;
    },
    SET_TAGLIST: (state, tagList) => {
        state.tagList = tagList;
    },
    SET_TYPE_LIST: (state, typeList) => {
        state.typeList = typeList;
    },
    SET_TYPELIST_ARR: (state, typeListArr) => {
        state.typeListArr = typeListArr;
    },
    SET_HOT_SEARCH_ARTICLE_LIST: (state, hotSearchArticleList) => {
        state.hotSearchArticleList = hotSearchArticleList;
    },
    SET_SYS_RECOMMEND_ARTICLE_LIST: (state, sysRecommendArticleList) => {
        state.sysRecommendArticleList = sysRecommendArticleList;
    },
    SET_SIMPLE_NOTICE_LIST: (state, simpleNoticeList) => {
        state.simpleNoticeList = simpleNoticeList;
    },
    SET_SOCKET_CONN_FLAG: (state, flag) => {
        state.socketConnFlag = flag;
    },
    SET_SYS_MESSAGE_LIST: (state, bean) => {
        state.sysMessageBean = bean;
        state.sysMessageList = bean.data;
        state.unReadMessageNum = bean.other.unReadNum || 0;
    },
    SET_ADD_UNREADMESSAGENUM: (state, data) => {
        state.unReadMessageNum = state.unReadMessageNum + 1
    },
}

const handleTreeList = (treeList, list) => {
    if (!treeList || !treeList.length) {
        return
    }
    for (let i = 0; i < treeList.length; i++) {
        let currentRow = treeList[i];
        let newRow = JSON.parse(JSON.stringify(currentRow));
        newRow.children = undefined;
        list.push(newRow);
        handleTreeList(currentRow.children, list)
    }
}

const actions = {
    setAddUnReadMessageNum ({ commit }, data) {
        commit('SET_ADD_UNREADMESSAGENUM', data)
    },
    setSocketConnFlag ({ commit }, data) {
        commit('SET_SOCKET_CONN_FLAG', data)
    },
    setBreadcrumbList ({ commit }, breadcrumbSelf) {
        commit('SET_BREADCRUMBLIST', breadcrumbSelf)
    },
    addBreadcrumbList ({ commit }, data) {
        commit('ADD_BREADCRUMBLIST', data)
    },
    setSysInfo ({ commit }, data) {
        getAboutSysInfo().then(res => {
            commit('SET_SYSINFO', res.data)
        })
    },
    setTagList ({ commit }, data) {
        getTagList().then(res => {
            commit('SET_TAGLIST', res.data)
        })
    },
    setTypeList ({ commit }, data) {
        getTechnologyType().then(res => {
            commit('SET_TYPE_LIST', res.data)
            let list = [];
            handleTreeList(res.data, list);
            commit('SET_TYPELIST_ARR', list)
        })
    },
    setHotSearchArticleList({ commit }, data) {
        getHotArticleList().then(res => {
            commit('SET_HOT_SEARCH_ARTICLE_LIST', res.data)
        })
    },
    setSystemRecommendArticleList({ commit }, data) {
        getSystemRecommendArticleList().then(res => {
            commit('SET_SYS_RECOMMEND_ARTICLE_LIST', res.data)
        })
    },
    setSimpleNoticeList({ commit }, data) {
        getPushNoticeList(1).then(res => {
            commit('SET_SIMPLE_NOTICE_LIST', res.data)
        })
    },
    setSysMessageList({ commit }, data) {
        getSystemMessage(data).then(res => {
            commit('SET_SYS_MESSAGE_LIST', res.data)
        })
    },
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
