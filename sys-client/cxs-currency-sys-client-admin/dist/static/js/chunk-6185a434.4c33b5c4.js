(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6185a434"],{"16fc":function(t,i,e){"use strict";e.r(i);var n=function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"components-container"},[t._m(0),e("div",{staticClass:"editor-container"},[e("dnd-list",{attrs:{list1:t.list1,list2:t.list2,"list1-title":"List","list2-title":"Article pool"}})],1)])},s=[function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("aside",[t._v("drag-list base on "),e("a",{attrs:{href:"https://github.com/SortableJS/Vue.Draggable",target:"_blank"}},[t._v("Vue.Draggable")])])}],a=(e("a434"),function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"dndList"},[e("div",{staticClass:"dndList-list",style:{width:t.width1}},[e("h3",[t._v(t._s(t.list1Title))]),e("draggable",{staticClass:"dragArea",attrs:{"set-data":t.setData,list:t.list1,group:"article"}},t._l(t.list1,(function(i){return e("div",{key:i.id,staticClass:"list-complete-item"},[e("div",{staticClass:"list-complete-item-handle"},[t._v(" "+t._s(i.id)+"["+t._s(i.author)+"] "+t._s(i.title)+" ")]),e("div",{staticStyle:{position:"absolute",right:"0px"}},[e("span",{staticStyle:{float:"right","margin-top":"-20px","margin-right":"5px"},on:{click:function(e){return t.deleteEle(i)}}},[e("i",{staticClass:"el-icon-delete",staticStyle:{color:"#ff4949"}})])])])})),0)],1),e("div",{staticClass:"dndList-list",style:{width:t.width2}},[e("h3",[t._v(t._s(t.list2Title))]),e("draggable",{staticClass:"dragArea",attrs:{list:t.list2,group:"article"}},t._l(t.list2,(function(i){return e("div",{key:i.id,staticClass:"list-complete-item"},[e("div",{staticClass:"list-complete-item-handle2",on:{click:function(e){return t.pushEle(i)}}},[t._v(" "+t._s(i.id)+" ["+t._s(i.author)+"] "+t._s(i.title)+" ")])])})),0)],1)])}),l=[],r=e("b85c"),c=(e("14d9"),e("d3b7"),e("1980")),d=e.n(c),o={name:"DndList",components:{draggable:d.a},props:{list1:{type:Array,default:function(){return[]}},list2:{type:Array,default:function(){return[]}},list1Title:{type:String,default:"list1"},list2Title:{type:String,default:"list2"},width1:{type:String,default:"48%"},width2:{type:String,default:"48%"}},methods:{isNotInList1:function(t){return this.list1.every((function(i){return t.id!==i.id}))},isNotInList2:function(t){return this.list2.every((function(i){return t.id!==i.id}))},deleteEle:function(t){var i,e=Object(r["a"])(this.list1);try{for(e.s();!(i=e.n()).done;){var n=i.value;if(n.id===t.id){var s=this.list1.indexOf(n);this.list1.splice(s,1);break}}}catch(a){e.e(a)}finally{e.f()}this.isNotInList2(t)&&this.list2.unshift(t)},pushEle:function(t){var i,e=Object(r["a"])(this.list2);try{for(e.s();!(i=e.n()).done;){var n=i.value;if(n.id===t.id){var s=this.list2.indexOf(n);this.list2.splice(s,1);break}}}catch(a){e.e(a)}finally{e.f()}this.isNotInList1(t)&&this.list1.push(t)},setData:function(t){t.setData("Text","")}}},u=o,f=(e("f45b"),e("2877")),h=Object(f["a"])(u,a,l,!1,null,"005c1ad2",null),p=h.exports,v=e("2423"),g={name:"DndListDemo",components:{DndList:p},data:function(){return{list1:[],list2:[]}},created:function(){this.getData()},methods:{getData:function(){var t=this;this.listLoading=!0,Object(v["b"])().then((function(i){t.list1=i.data.items.splice(0,5),t.list2=i.data.items}))}}},m=g,b=Object(f["a"])(m,n,s,!1,null,null,null);i["default"]=b.exports},2423:function(t,i,e){"use strict";e.d(i,"e",(function(){return a})),e.d(i,"d",(function(){return l})),e.d(i,"f",(function(){return r})),e.d(i,"g",(function(){return c})),e.d(i,"c",(function(){return d})),e.d(i,"a",(function(){return o})),e.d(i,"b",(function(){return u}));var n=e("b775"),s=e("da71");function a(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};return Object(n["a"])({url:s["a"]+"/admin/article/getReviewedList",method:"post",data:t})}function l(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};return Object(n["a"])({url:s["a"]+"/admin/article/getArticleList",method:"post",data:t})}function r(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};return Object(n["a"])({url:s["a"]+"/admin/article/reviewedArticle",method:"post",data:t})}function c(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};return Object(n["a"])({url:s["a"]+"/admin/article/updateArticle",method:"post",data:t})}function d(t){return Object(n["a"])({url:s["a"]+"/admin/article/getArticleInfo/".concat(t),method:"get"})}function o(t){return Object(n["a"])({url:s["a"]+"/admin/article/deleteArticle/".concat(t),method:"delete"})}function u(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{};return Object(n["a"])({url:s["a"]+"/admin/article/fetchList",method:"post",data:t})}},"329f":function(t,i,e){},f45b:function(t,i,e){"use strict";e("329f")}}]);