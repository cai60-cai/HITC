"use strict";(self["webpackChunkcxs_currency_sys_client"]=self["webpackChunkcxs_currency_sys_client"]||[]).push([[522],{38562:function(t,s,i){i.d(s,{A:function(){return d}});i(28743);var a=function(){var t=this,s=t._self._c;return s("div",{staticClass:"widget about"},[s("h3",{staticClass:"aside-title widget-title cursor",on:{click:function(s){return t.$router.push({name:"system-info"})}}},[t._v("关于本站")]),s("div",{staticClass:"item-rank"}),s("div",{attrs:{id:"feed_widget"}},[s("div",{staticClass:"feed-about"},[s("div",{staticClass:"about-main"},[s("div",{staticClass:"about-img"},[t.sysInfo.sysLogo?s("img",{staticClass:"cursor",attrs:{src:"/server/upload/20240608/48782e8c448143bf89d4dafe336f862920240608212937.png",alt:t.sysInfo.sysTitle},on:{click:function(s){return t.$router.push({name:"system-info"})}}}):t._e()]),s("div",{staticClass:"about-me"},[s("div",{staticClass:"about-name cursor",on:{click:function(s){return t.$router.push({name:"system-info"})}}},[t._v(" "+t._s(t.sysInfo.sysTitle))]),s("div",{staticClass:"about-the"},[t._v(t._s(t.sysInfo.sysGraph))])])]),s("div",{staticClass:"clear"})])]),s("div",{staticClass:"data-info d-flex item-tiling"},[s("dl",{staticClass:"text-center",attrs:{id:"fanBox",title:t.sysInfo.articleCount}},[s("dt",[s("span",{staticClass:"count",attrs:{id:"fan"}},[t._v(t._s(t.sysInfo.articleCount))])]),s("dd",[t._v("文章")])]),s("dl",{staticClass:"text-center",attrs:{title:t.sysInfo.originArticleCount}},[s("dt",[s("span",{staticClass:"count"},[t._v(t._s(t.sysInfo.originArticleCount))])]),s("dd",[t._v("原创")])]),s("dl",{staticClass:"text-center",attrs:{title:t.sysInfo.userCount}},[s("dt",[s("span",{staticClass:"count"},[t._v(t._s(t.sysInfo.userCount))])]),s("dd",[t._v("用户")])]),s("dl",{staticClass:"text-center",attrs:{title:t.sysInfo.tagCount}},[s("dt",[s("span",{staticClass:"count"},[t._v(t._s(t.sysInfo.tagCount))])]),s("dd",[t._v("标签")])]),s("dl",{staticClass:"text-center",attrs:{title:t.sysInfo.typeCount}},[s("dt",[s("span",{staticClass:"count"},[t._v(t._s(t.sysInfo.typeCount))])]),s("dd",[t._v("分类")])])]),s("div",{staticClass:"clear"})])},e=[],r=i(52004),n=i(93518),l={name:"AboutSystemSlide",data(){return{baseUrl:r.f}},computed:{...(0,n.L8)(["sysInfo"])}},o=l,c=i(81656),u=(0,c.A)(o,a,e,!1,null,"aaee5410",null),d=u.exports},9908:function(t,s,i){i.d(s,{A:function(){return c}});i(28743);var a=function(){var t=this,s=t._self._c;return s("div",{staticClass:"aside-box sidebar",attrs:{id:"asideHotArticle"}},[s("h3",{staticClass:"aside-title"},[t._v(t._s(t.title))]),s("div",{staticClass:"item-rank"}),s("div",{staticClass:"aside-content"},[s("ul",{staticClass:"hotArticle-list"},t._l(t.list,(function(i,a){return s("li",{key:i.id},[s("span",{staticClass:"substr",on:{click:function(s){return t.$router.push({name:"notice-detail",query:{id:i.id}})}}},[s("span",a<3?[t._v("【最新发布】"+t._s(i.noticeTitle))]:[t._v(t._s(i.noticeTitle))])])])})),0)])])},e=[],r={name:"HotSearchSide",props:{title:{type:String,default:""},list:{type:Array,default:()=>[]}},data(){return{}}},n=r,l=i(81656),o=(0,l.A)(n,a,e,!1,null,"73bf9334",null),c=o.exports},78522:function(t,s,i){i.r(s),i.d(s,{default:function(){return C}});var a=function(){var t=this,s=t._self._c;return s("div",{staticClass:"site-content clean content",staticStyle:{transform:"none"}},[t.refresh?s("MyBreadcrumb",{attrs:{breadcrumb:t.isBread}}):t._e(),s("div",{staticClass:"content-area",attrs:{id:"primary"}},[s("div",{staticClass:"left-nav"},[t.userInfo?s("div",{staticClass:"left-nav-top"},[s("div",{staticClass:"avatar"},[t.user.avatar?s("el-avatar",{staticStyle:{"margin-right":"10px",border:"1px solid #cccccc"},attrs:{size:100,src:t.baseURL+t.user.avatar}}):s("el-avatar",{staticStyle:{"margin-right":"10px",border:"1px solid #cccccc"},attrs:{size:100,src:t.user.avatar}})],1),s("div",{staticClass:"avatar-info"},[s("div",{staticClass:"nickname",attrs:{title:t.userInfo.nickName}},[s("i",{staticClass:"iconfont icon-xingming"}),t._v(t._s(t.userInfo.nickName))]),s("div",{staticClass:"point",attrs:{title:"积分余额"}},[s("i",{staticClass:"iconfont icon-jifen"}),t._v(t._s(t.userInfo.point))])])]):t._e(),s("div",{staticClass:"left-nav-main"},[s("router-link",{staticClass:"link-item",attrs:{to:"/profile/personal"}},[t._v("个人资料")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/setting"}},[t._v("个性化设置")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/updatepwd"}},[t._v("修改密码")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/loginlog"}},[t._v("登录日志查看")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/pushinfo"}},[t._v("我的发布")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/collinfo"}},[t._v("我的收藏")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/commentinfo"}},[t._v("我的评论")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/tradeflow"}},[t._v("积分交易流水")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/feedback"}},[t._v("我的反馈")]),s("router-link",{staticClass:"link-item",attrs:{to:"/profile/report"}},[t._v("我的举报")])],1)]),s("div",{staticClass:"right-view"},[s("router-view")],1)])],1)},e=[],r=i(38562),n=i(9908),l=i(93518),o=i(92549),c=i(52004),u={name:"Profile",components:{AboutSystem:r.A,SysNoticeSide:n.A},data(){return{baseURL:c.f,isBread:!0,refresh:!0,userInfo:{}}},watch:{"$route.path":{immediate:!0,handler:function(t,s){this.refresh=!1,this.$store.dispatch("header/setbreadcrumbAppend",[]),this.$nextTick((()=>{this.refresh=!0}))}}},mounted(){this.init()},methods:{init(){(0,o.xL)().then((t=>{this.userInfo=t.data}))}},computed:{...(0,l.L8)(["user"])}},d=u,f=i(81656),v=(0,f.A)(d,a,e,!1,null,"f351760a",null),C=v.exports}}]);