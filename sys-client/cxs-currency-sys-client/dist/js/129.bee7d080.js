"use strict";(self["webpackChunkcxs_currency_sys_client"]=self["webpackChunkcxs_currency_sys_client"]||[]).push([[129],{53391:function(e,a,t){t.d(a,{A:function(){return c}});t(28743);var r=function(){var e=this,a=e._self._c;return a("div",{staticClass:"aside-box sidebar",attrs:{id:"asideHotArticle"}},[a("h3",{staticClass:"aside-title"},[e._v(e._s(e.title))]),a("div",{staticClass:"item-rank"}),a("div",{staticClass:"aside-content"},[a("ul",{staticClass:"hotArticle-list"},e._l(e.list,(function(t){return a("li",{key:t.id},[a("span",{staticClass:"substr",on:{click:function(a){return e.$router.push({name:"relaQuestion",params:{id:t.id}})}}},[a("span",[e._v(e._s(t.question))])])])})),0)])])},s=[],l={name:"FallbackSide",props:{title:{type:String,default:""},list:{type:Array,default:()=>[]}},data(){return{}}},i=l,o=t(81656),n=(0,o.A)(i,r,s,!1,null,"63b1927d",null),c=n.exports},89129:function(e,a,t){t.r(a),t.d(a,{default:function(){return b}});var r=function(){var e=this,a=e._self._c;return a("div",{staticClass:"site-content clean content",staticStyle:{transform:"none"}},[a("MyBreadcrumb",{attrs:{breadcrumb:!0}}),a("div",{staticClass:"content-area",attrs:{id:"primary"}},[a("el-form",{ref:"ruleForm",staticClass:"demo-ruleForm",attrs:{model:e.ruleForm,"status-icon":"",rules:e.rules,"label-width":"100px"}},[a("el-form-item",{attrs:{label:"反馈类型",prop:"feedbackType"}},[e.$device.windows?a("el-radio-group",{model:{value:e.ruleForm.feedbackType,callback:function(a){e.$set(e.ruleForm,"feedbackType",a)},expression:"ruleForm.feedbackType"}},e._l(e.feedbackType,(function(t,r){return a("el-radio",{key:r,attrs:{label:t}},[e._v(e._s(t))])})),1):a("el-select",{model:{value:e.ruleForm.feedbackType,callback:function(a){e.$set(e.ruleForm,"feedbackType",a)},expression:"ruleForm.feedbackType"}},e._l(e.feedbackType,(function(e){return a("el-option",{key:e,attrs:{label:e,value:e}})})),1)],1),a("el-form-item",{attrs:{label:"反馈图片",prop:"avatar"}},[a("div",{staticStyle:{display:"flex","justify-content":"space-between"}},[a("el-upload",{staticClass:"avatar-uploader",attrs:{action:e.baseUrl+"/base/file/upload",headers:e.header,"show-file-list":!1,"on-success":e.handleAvatarSuccess1,"before-upload":e.beforeAvatarUpload}},[e.image1?a("img",{staticClass:"avatar",attrs:{src:e.baseUrl+e.image1}}):a("i",{staticClass:"el-icon-plus avatar-uploader-icon"})]),a("el-upload",{staticClass:"avatar-uploader",attrs:{action:e.baseUrl+"/base/file/upload",headers:e.header,"show-file-list":!1,"on-success":e.handleAvatarSuccess2,"before-upload":e.beforeAvatarUpload}},[e.image2?a("img",{staticClass:"avatar",attrs:{src:e.baseUrl+e.image2}}):a("i",{staticClass:"el-icon-plus avatar-uploader-icon"})]),a("el-upload",{staticClass:"avatar-uploader",attrs:{action:e.baseUrl+"/base/file/upload",headers:e.header,"show-file-list":!1,"on-success":e.handleAvatarSuccess3,"before-upload":e.beforeAvatarUpload}},[e.image3?a("img",{staticClass:"avatar",attrs:{src:e.baseUrl+e.image3}}):a("i",{staticClass:"el-icon-plus avatar-uploader-icon"})])],1)]),a("el-form-item",{attrs:{label:"接收邮箱",prop:"feedbackEmail"}},[a("el-input",{attrs:{placeholder:"请输入接收邮箱,不填默认为资料邮箱","show-word-limit":"",autocomplete:"off",maxlength:"50"},model:{value:e.ruleForm.feedbackEmail,callback:function(a){e.$set(e.ruleForm,"feedbackEmail",a)},expression:"ruleForm.feedbackEmail"}})],1),a("el-form-item",{attrs:{label:"反馈内容",prop:"feedbackContent"}},[a("el-input",{attrs:{type:"textarea",rows:7,resize:"none",placeholder:"请输入反馈内容","show-word-limit":"",autocomplete:"off",maxlength:"1000"},model:{value:e.ruleForm.feedbackContent,callback:function(a){e.$set(e.ruleForm,"feedbackContent",a)},expression:"ruleForm.feedbackContent"}})],1),a("el-form-item",{staticStyle:{"text-align":"right"}},[a("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(a){return e.submitForm("ruleForm")}}},[e._v("提交")]),a("el-button",{attrs:{size:"small"},on:{click:function(a){return e.resetForm("ruleForm")}}},[e._v("重置")])],1)],1)],1),a("div",{staticClass:"widget-area all-sidebar",attrs:{id:"sidebar"}},[a("FallbackSide",{attrs:{title:"系统相关问题",list:e.questionList}})],1)],1)},s=[],l=(t(28743),t(53391)),i=t(52004),o=t(43603),n=t(76193),c=t(80503),u=t(17221),d={name:"Fallback",components:{FallbackSide:l.A},data(){var e=(e,a,t)=>{if(!a)return t(new Error("反馈类型为必选项"));t()},a=(e,a,t)=>{""===a?t(new Error("反馈内容为必填项")):(a.length>1e3&&t(new Error("反馈内容不能大于1000字符")),t())},t=(e,a,t)=>{const r=/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;a&&""!==a?r.test(a)?t():t(new Error("邮箱格式不正确")):t()};return{questionList:[],feedbackType:n.wj,ruleForm:{feedbackType:"",feedbackContent:"",feedbackImages:[],feedbackEmail:""},image1:"",image2:"",image3:"",baseUrl:i.f,rules:{feedbackType:[{validator:e,trigger:"blur"}],feedbackContent:[{validator:a,trigger:"blur"}],feedbackEmail:[{validator:t,trigger:"blur"}]}}},computed:{header(){return{access_token:(0,o.gf)()}}},mounted(){this.init()},methods:{init(){(0,c.jN)().then((e=>{this.questionList=e.data}))},handleAvatarSuccess1(e,a){this.image1=e.data},handleAvatarSuccess2(e,a){this.image2=e.data},handleAvatarSuccess3(e,a){this.image3=e.data},beforeAvatarUpload(e){const a="image/jpeg"===e.type,t="image/png"===e.type,r=e.size/1024/1024<2;return a||t||this.$message.error("上传头像图片只能是 jpg或png 格式!"),r||this.$message.error("上传头像图片大小不能超过 2MB!"),a&&r||t&&r},submitForm(e){this.$refs[e].validate((e=>{let a=[];if(this.image1&&this.image1.length>0&&a.push(this.image1),this.image2&&this.image2.length>0&&a.push(this.image2),this.image3&&this.image3.length>0&&a.push(this.image3),this.ruleForm.feedbackImages=a,!e)return console.log("error submit!!"),!1;console.log(this.ruleForm),(0,u.p)(this.ruleForm).then((e=>{this.$message.success("处理成功"),this.image1="",this.image2="",this.image3="",this.ruleForm={}}))}))},resetForm(e){this.$refs[e].resetFields()}}},m=d,f=t(81656),p=(0,f.A)(m,r,s,!1,null,"1237df52",null),b=p.exports},17221:function(e,a,t){t.d(a,{L:function(){return i},p:function(){return l}});var r=t(7277),s=t(52004);function l(e={}){return(0,r.A)({url:s.f+"/feedback/addFeedback",method:"post",data:e})}function i(e){return(0,r.A)({url:`${s.f}/feedback/delFeedback/${e}`,method:"delete"})}}}]);