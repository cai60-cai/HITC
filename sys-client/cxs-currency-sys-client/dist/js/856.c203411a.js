"use strict";(self["webpackChunkcxs_currency_sys_client"]=self["webpackChunkcxs_currency_sys_client"]||[]).push([[856],{38856:function(t,e,i){i.r(e),i.d(e,{default:function(){return h}});var n=function(){var t=this,e=t._self._c;return e("div",{staticClass:"all"},[e("div",{staticClass:"point-setting"},[e("h3",[t._v("展示积分余额")]),e("el-row",{staticClass:"data-row",attrs:{gutter:20}},[e("el-col",{attrs:{span:12}},[e("div",{staticClass:"grid-content bg-purple"},[e("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#cccccc","active-text":"展示","inactive-text":"隐藏"},on:{change:t.settingsPointChangeHandle},model:{value:t.settingInfo.showPoint,callback:function(e){t.$set(t.settingInfo,"showPoint",e)},expression:"settingInfo.showPoint"}})],1)])],1)],1),e("div",{staticClass:"point-setting"},[e("h3",[t._v("接收邮件通知")]),e("el-row",{staticClass:"data-row",attrs:{gutter:20}},[e("el-col",{attrs:{span:12}},[e("div",{staticClass:"grid-content bg-purple"},[e("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#cccccc","active-text":"接收","inactive-text":"不接受"},on:{change:t.settingsEmailChangeHandle},model:{value:t.settingInfo.receiveEmailNotice,callback:function(e){t.$set(t.settingInfo,"receiveEmailNotice",e)},expression:"settingInfo.receiveEmailNotice"}})],1)])],1)],1),e("div",{staticClass:"reward-setting"},[e("h3",[t._v("其他用户转发权限")]),e("el-row",{staticStyle:{display:"flex","align-items":"center",height:"50px"},attrs:{gutter:20}},[e("el-col",{attrs:{span:12}},[e("div",{staticClass:"grid-content bg-purple"},[e("el-switch",{attrs:{"active-color":"#13ce66","inactive-color":"#cccccc","active-text":"开启","inactive-text":"关闭"},on:{change:t.settingsRewardChangeHandle},model:{value:t.settingInfo.openReward,callback:function(e){t.$set(t.settingInfo,"openReward",e)},expression:"settingInfo.openReward"}})],1)])],1)],1)])},a=[],s=i(52004),o=i(43603),c=i(92549),r={name:"Setting",data(){return{value:1,baseUrl:s.f,settingInfo:{},rewardInfo:{weixinImg:"",weixinBorderColor:"",zhifubaoImg:"",zhifubaoBorderColor:""}}},computed:{header(){return{access_token:(0,o.gf)()}}},mounted(){this.init()},methods:{init(){(0,c.LJ)().then((t=>{const{data:e}=t;this.settingInfo=e,e&&(this.rewardInfo=e.rewardInfo)}))},updateRewardInfo(){(0,c.IM)(this.rewardInfo).then((t=>{this.$message.success("打赏信息保存成功")}))},operationPointSetting(){(0,c.Uo)(this.settingInfo).then((t=>{this.init()}))},operationReceiveEmailNoticeSetting(){(0,c.Fs)(this.settingInfo).then((t=>{this.init()}))},operationRewardSetting(){(0,c.xw)(this.settingInfo).then((t=>{this.init()}))},getRewardInfo(){(0,c.LJ)().then((t=>{const{data:e}=t;e&&(this.rewardInfo=e.rewardInfo)}))},weixinHandleAvatarSuccess(t,e){this.rewardInfo.weixinImg=t.data,this.$forceUpdate()},zhifubaoHandleAvatarSuccess(t,e){this.rewardInfo.zhifubaoImg=t.data,this.$forceUpdate()},beforeAvatarUpload(t){const e="image/jpeg"===t.type,i="image/png"===t.type,n=t.size/1024/1024<2;return e||i||this.$message.error("上传头像图片只能是 jpg或png 格式!"),n||this.$message.error("上传头像图片大小不能超过 2MB!"),e&&n||i&&n},settingsPointChangeHandle(){this.operationPointSetting()},settingsEmailChangeHandle(){this.operationReceiveEmailNoticeSetting()},settingsRewardChangeHandle(){this.operationRewardSetting(),this.getRewardInfo()}}},l=r,g=i(81656),d=(0,g.A)(l,n,a,!1,null,"3b0d9aaa",null),h=d.exports}}]);