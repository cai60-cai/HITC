const getters = {
  showBreadcrumb: state => state.header.showBreadcrumb,
  breadcrumbAppend: state => state.header.breadcrumbAppend,
  token: state => state.auth.token,
  user: state => state.auth.user,
  isLogin: state => state.auth.isLogin,
  loginFlag: state => state.auth.loginFlag,
  sysInfo: state => state.main.sysInfo,
  tagList: state => state.main.tagList,
  typeList: state => state.main.typeList,
  typeListArr: state => state.main.typeListArr,
  hotSearchArticleList: state => state.main.hotSearchArticleList,
  sysRecommendArticleList: state => state.main.sysRecommendArticleList,
  simpleNoticeList: state => state.main.simpleNoticeList,
  socketConnFlag: state => state.main.socketConnFlag,
  sysMessageList: state => state.main.sysMessageList,
  sysMessageBean: state => state.main.sysMessageBean,
  unReadMessageNum: state => state.main.unReadMessageNum
}
export default getters
