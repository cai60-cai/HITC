(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-177eac65"],{1151:function(e,t,r){},"51d2":function(e,t,r){"use strict";r("1151")},"70eb":function(e,t,r){"use strict";r.r(t);var n=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app-container"},[r("el-button",{attrs:{type:"primary"},on:{click:e.handleAddRole}},[e._v("创建新角色")]),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],staticStyle:{width:"100%","margin-top":"30px"},attrs:{data:e.rolesList,border:""}},[r("el-table-column",{attrs:{align:"center",label:"角色id",width:"220"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.roleId)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"角色名称",width:"220"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.roleName)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"角色描述",width:"220"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.roleDesc)+" ")]}}])}),r("el-table-column",{attrs:{align:"center",label:"操作",fixed:"right"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-button",{attrs:{type:"primary",size:"small"},on:{click:function(r){return e.handleEdit(t)}}},[e._v("编辑")]),r("el-button",{attrs:{type:"danger",size:"small"},on:{click:function(r){return e.handleDelete(t)}}},[e._v("删除")])]}}])})],1),r("el-dialog",{attrs:{visible:e.dialogVisible,title:"edit"===e.dialogType?"编辑角色":"修改角色"},on:{"update:visible":function(t){e.dialogVisible=t}}},[r("el-form",{attrs:{model:e.role,"label-width":"80px","label-position":"left"}},[r("el-form-item",{attrs:{label:"角色名称"}},[r("el-input",{attrs:{placeholder:"角色名"},model:{value:e.role.roleName,callback:function(t){e.$set(e.role,"roleName",t)},expression:"role.roleName"}})],1),r("el-form-item",{attrs:{label:"角色描述"}},[r("el-input",{attrs:{placeholder:"角色描述"},model:{value:e.role.roleDesc,callback:function(t){e.$set(e.role,"roleDesc",t)},expression:"role.roleDesc"}})],1),r("el-form-item",{attrs:{label:"菜单权限"}},[r("el-tree",{ref:"tree",staticClass:"permission-tree",attrs:{"check-strictly":e.checkStrictly,data:e.routesData,props:e.defaultProps,"show-checkbox":"","node-key":"path"}})],1)],1),r("div",{staticStyle:{"text-align":"right"}},[r("el-button",{attrs:{type:"danger"},on:{click:function(t){e.dialogVisible=!1}}},[e._v("取消")]),r("el-button",{attrs:{type:"primary"},on:{click:e.confirmRole}},[e._v("确认")])],1)],1)],1)},a=[],l=r("5530"),i=r("2909"),o=r("b85c"),s=r("c7eb"),c=r("1da1"),d=(r("99af"),r("4de4"),r("caad"),r("14d9"),r("a434"),r("d3b7"),r("2532"),r("159b"),r("df7c")),u=r.n(d),h=r("ed08"),f=r("cc5e"),b={roleId:0,roleName:"",roleDesc:"",routes:[]},p={name:"Role",data:function(){return{role:Object.assign({},b),routes:[],rolesList:[],listLoading:!0,dialogVisible:!1,dialogType:"new",checkStrictly:!1,defaultProps:{children:"children",label:"title"}}},computed:{routesData:function(){return this.routes}},created:function(){this.getRoutes(),this.getRoles()},methods:{getRoutes:function(){var e=this;return Object(c["a"])(Object(s["a"])().mark((function t(){var r,n;return Object(s["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,Object(f["f"])();case 2:r=t.sent,e.serviceRoutes=r.data,n=e.generateRoutes(r.data),e.routes=n;case 6:case"end":return t.stop()}}),t)})))()},getRoles:function(){var e=this;this.listLoading=!0,Object(f["e"])().then((function(t){e.listLoading=!1,e.rolesList=t.data}))},generateRoutes:function(e){var t,r=arguments.length>1&&void 0!==arguments[1]?arguments[1]:"/",n=[],a=Object(o["a"])(e);try{for(a.s();!(t=a.n()).done;){var l=t.value;if(!l.hidden){var i={path:u.a.resolve(r,l.path),title:l.meta&&l.meta.title};l.children&&(i.children=this.generateRoutes(l.children,i.path)),n.push(i)}}}catch(s){a.e(s)}finally{a.f()}return n},generateArr:function(e){var t=this,r=[];return e.forEach((function(e){if(r.push(e),e.children){var n=t.generateArr(e.children);n.length>0&&(r=[].concat(Object(i["a"])(r),Object(i["a"])(n)))}})),r},handleAddRole:function(){this.role=Object.assign({},b),this.$refs.tree&&this.$refs.tree.setCheckedNodes([]),this.dialogType="new",this.dialogVisible=!0},handleEdit:function(e){var t=this;this.dialogType="edit",this.dialogVisible=!0,this.checkStrictly=!0,this.role=Object(h["c"])(e.row),this.$nextTick((function(){var e=t.generateRoutes(t.role.routes),r=t.generateArr(e);t.$refs.tree.setCheckedNodes(r),t.checkStrictly=!1}))},handleDelete:function(e){var t=this,r=e.$index,n=e.row;this.$confirm("确定要删除这个角色吗?","Warning",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(Object(c["a"])(Object(s["a"])().mark((function e(){var a,l;return Object(s["a"])().wrap((function(e){while(1)switch(e.prev=e.next){case 0:return a=[],a.push(n.roleId),l={ids:a},e.next=5,Object(f["b"])(l);case 5:t.rolesList.splice(r,1),t.$message({type:"success",message:"角色删除成功!"});case 7:case"end":return e.stop()}}),e)})))).catch((function(e){console.error(e)}))},generateTree:function(e){var t,r=arguments.length>1&&void 0!==arguments[1]?arguments[1]:"/",n=arguments.length>2?arguments[2]:void 0,a=[],l=Object(o["a"])(e);try{for(l.s();!(t=l.n()).done;){var i=t.value,s=u.a.resolve(r,i.path);i.children&&(i.children=this.generateTree(i.children,s,n)),(n.includes(s)||i.children&&i.children.length>=1)&&a.push(i)}}catch(c){l.e(c)}finally{l.f()}return a},confirmRole:function(){var e=this;return Object(c["a"])(Object(s["a"])().mark((function t(){var r,n,a,l,i,o,c,d,u;return Object(s["a"])().wrap((function(t){while(1)switch(t.prev=t.next){case 0:if(r="edit"===e.dialogType,n=e.$refs.tree.getCheckedKeys(),e.role.routes=e.generateTree(Object(h["c"])(e.serviceRoutes),"/",n),!r){t.next=16;break}return t.next=6,Object(f["g"])(e.role);case 6:a=0;case 7:if(!(a<e.rolesList.length)){t.next=14;break}if(e.rolesList[a].key!==e.role.key){t.next=11;break}return e.rolesList.splice(a,1,Object.assign({},e.role)),t.abrupt("break",14);case 11:a++,t.next=7;break;case 14:t.next=23;break;case 16:return e.role.roleId=void 0,t.next=19,Object(f["a"])(e.role);case 19:l=t.sent,i=l.data,e.role.roleId=i.roleId,e.rolesList.push(e.role);case 23:o=e.role,c=o.roleDesc,d=o.roleId,u=o.roleName,e.dialogVisible=!1,e.$notify({title:"Success",dangerouslyUseHTMLString:!0,message:"\n            <div>Role Id: ".concat(d,"</div>\n            <div>Role Name: ").concat(u,"</div>\n            <div>Description: ").concat(c,"</div>\n          "),type:"success"}),e.getRoles();case 27:case"end":return t.stop()}}),t)})))()},onlyOneShowingChild:function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:[],t=arguments.length>1?arguments[1]:void 0,r=null,n=e.filter((function(e){return!e.hidden}));return 1===n.length?(r=n[0],r.path=u.a.resolve(t.path,r.path),r):0===n.length&&(r=Object(l["a"])(Object(l["a"])({},t),{},{noShowingChildren:!0}),r)}}},g=p,v=(r("51d2"),r("2877")),m=Object(v["a"])(g,n,a,!1,null,"2b51285e",null);t["default"]=m.exports}}]);