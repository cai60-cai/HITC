(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-86b27480"],{"00c7":function(e,t,o){"use strict";o("fca1")},"033b":function(e,t,o){},"9ed6":function(e,t,o){"use strict";o.r(t);var n=function(){var e=this,t=e.$createElement,o=e._self._c||t;return o("div",{staticClass:"login-container"},[o("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:e.loginForm,rules:e.loginRules,autocomplete:"on","label-position":"left"}},[o("div",{staticClass:"title-container"},[o("h3",{staticClass:"title"},[e._v(e._s(e.title)+"登录")])]),o("el-form-item",{attrs:{prop:"username"}},[o("span",{staticClass:"svg-container"},[o("svg-icon",{attrs:{"icon-class":"user"}})],1),o("el-input",{ref:"username",attrs:{placeholder:"用户名",name:"username",type:"text",tabindex:"1",autocomplete:"on"},model:{value:e.loginForm.username,callback:function(t){e.$set(e.loginForm,"username",t)},expression:"loginForm.username"}})],1),o("el-tooltip",{attrs:{content:"Caps lock is On",placement:"right",manual:""},model:{value:e.capsTooltip,callback:function(t){e.capsTooltip=t},expression:"capsTooltip"}},[o("el-form-item",{attrs:{prop:"password"}},[o("span",{staticClass:"svg-container"},[o("svg-icon",{attrs:{"icon-class":"password"}})],1),o("el-input",{key:e.passwordType,ref:"password",attrs:{type:e.passwordType,placeholder:"账户密码",name:"password",tabindex:"2",autocomplete:"on"},on:{blur:function(t){e.capsTooltip=!1}},nativeOn:{keyup:[function(t){return e.checkCapslock(t)},function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleLogin(t)}]},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}}),o("span",{staticClass:"show-pwd",on:{click:e.showPwd}},[o("svg-icon",{attrs:{"icon-class":"password"===e.passwordType?"eye":"eye-open"}})],1)],1)],1),o("el-button",{staticStyle:{width:"100%","margin-bottom":"30px"},attrs:{loading:e.loading,type:"primary"},nativeOn:{click:function(t){return t.preventDefault(),e.handleLogin(t)}}},[e._v("登录")])],1)],1)},s=[],r=o("5530"),a=(o("d9e2"),o("14d9"),o("13d5"),o("b64b"),o("d3b7"),o("83d6")),i=o.n(a),c=o("0627"),l={name:"Login",components:{},data:function(){var e=i.a.title,t=function(e,t,o){t?o():o(new Error("用户名岂能为空"))},o=function(e,t,o){t.length<1?o(new Error("用户密码岂能为空")):o()};return{title:e,loginForm:{username:"",password:""},loginRules:{username:[{required:!0,trigger:"blur",validator:t}],password:[{required:!0,trigger:"blur",validator:o}]},passwordType:"password",capsTooltip:!1,loading:!1,showDialog:!1,redirect:void 0,otherQuery:{}}},watch:{$route:{handler:function(e){var t=e.query;t&&(this.redirect=t.redirect,this.otherQuery=this.getOtherQuery(t))},immediate:!0}},created:function(){},mounted:function(){""===this.loginForm.username?this.$refs.username.focus():""===this.loginForm.password&&this.$refs.password.focus()},destroyed:function(){},methods:{checkCapslock:function(e){var t=e.key;this.capsTooltip=t&&1===t.length&&t>="A"&&t<="Z"},showPwd:function(){var e=this;"password"===this.passwordType?this.passwordType="":this.passwordType="password",this.$nextTick((function(){e.$refs.password.focus()}))},handleLogin:function(){var e=this;this.$refs.loginForm.validate((function(t){if(!t)return console.log("error submit!!"),!1;e.loading=!0;var o=e.loginForm.password,n=Object(r["a"])(Object(r["a"])({},e.loginForm),{},{password:c["a"].encrypt(o)});e.$store.dispatch("user/login",n).then((function(){e.$router.push({path:e.redirect||"/",query:e.otherQuery}),e.loading=!1})).catch((function(){e.loading=!1})),setTimeout((function(){e.loading=!1}),2e3)}))},getOtherQuery:function(e){return Object.keys(e).reduce((function(t,o){return"redirect"!==o&&(t[o]=e[o]),t}),{})}}},u=l,p=(o("aedc"),o("00c7"),o("2877")),d=Object(p["a"])(u,n,s,!1,null,"129a4e16",null);t["default"]=d.exports},aedc:function(e,t,o){"use strict";o("033b")},fca1:function(e,t,o){}}]);