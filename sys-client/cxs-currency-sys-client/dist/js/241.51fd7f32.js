"use strict";(self["webpackChunkcxs_currency_sys_client"]=self["webpackChunkcxs_currency_sys_client"]||[]).push([[241],{61241:function(s,e,t){t.r(e),t.d(e,{default:function(){return g}});var a=function(){var s=this,e=s._self._c;return e("div",{staticClass:"site-content clean content",staticStyle:{transform:"none"}},[s.refresh?e("MyBreadcrumb",{attrs:{breadcrumb:s.isBread}}):s._e(),e("div",{staticClass:"content-area",attrs:{id:"primary"}},[e("div",{staticClass:"title"},[e("el-link",{on:{click:function(e){return s.sendMessageRead(2)}}},[s._v("全部已读")])],1),e("div",{staticClass:"message-all"},[e("el-row",{staticClass:"data-row",attrs:{gutter:20}},[e("el-col",{attrs:{span:2}},[e("div",{staticClass:"grid-content bg-purple"},[s._v("id")])]),e("el-col",{attrs:{span:14}},[e("div",{staticClass:"grid-content bg-purple"},[s._v("消息内容")])]),e("el-col",{attrs:{span:4}},[e("div",{staticClass:"grid-content bg-purple"},[s._v("推送时间")])]),e("el-col",{attrs:{span:4}},[e("div",{staticClass:"grid-content bg-purple"},[s._v("操作")])])],1),s._l(s.sysMessageList,(function(t,a){return s.sysMessageBean?e("el-row",{key:t.id,staticClass:"data-item",attrs:{gutter:20}},[e("el-col",{attrs:{span:2}},[e("div",{staticClass:"grid-content bg-purple"},[s._v(s._s(t.id))])]),e("el-col",{attrs:{span:14}},[e("div",{staticClass:"grid-content bg-purple content-msg"},[s._v(s._s(t.messageContent))])]),e("el-col",{attrs:{span:4}},[e("div",{staticClass:"grid-content bg-purple"},[s._v(s._s(t.createTime))])]),e("el-col",{attrs:{span:4}},[t.isRead?e("div",{staticClass:"grid-content bg-purple"},[s._v("已读")]):e("div",{staticClass:"grid-content bg-purple"},[e("el-link",{attrs:{underline:!1,type:"primary"},on:{click:function(e){return s.sendMessageRead(1,t.id)}}},[s._v("设为已读")])],1)])],1):s._e()})),s.sysMessageBean.pages&&s.sysMessageBean.pages>1?e("el-pagination",{staticClass:"page",attrs:{layout:"prev, pager, next","page-size":s.sysMessageBean.pageSize,total:s.sysMessageBean.total},on:{"current-change":s.handleCurrentChange}}):s._e()],2)])],1)},i=[],n=(t(28743),t(93518)),r=t(92549),c={name:"Sysmsg",data(){return{isBread:!0,refresh:!0,params:{pageNum:1,pageSize:10}}},watch:{"$route.path":{immediate:!0,handler:function(s,e){this.refresh=!1,this.$store.dispatch("header/setbreadcrumbAppend",[]),this.$nextTick((()=>{this.refresh=!0}))}}},mounted(){this.init()},computed:{...(0,n.L8)(["sysMessageBean","sysMessageList"])},methods:{init(){this.$store.dispatch("main/setSysMessageList",this.params)},handleCurrentChange(s){this.params.pageNum=s,this.init()},sendMessageRead(s,e){const t={messageIdList:[]};1==s?t.messageIdList.push(e):this.sysMessageList.forEach((s=>{s.isRead||t.messageIdList.push(s.id)})),t.messageIdList.length>0&&(0,r.R_)(t).then((s=>{this.$store.dispatch("main/setSysMessageList",this.params)}))}}},l=c,d=t(81656),p=(0,d.A)(l,a,i,!1,null,"504323d0",null),g=p.exports}}]);