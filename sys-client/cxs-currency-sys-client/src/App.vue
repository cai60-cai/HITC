<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

import '@/assets/icon/iconfont.css'
export default {
  name: 'App'
}
</script>


<style lang="less">
body::-webkit-scrollbar {
  width: 5px;          /* 纵向滚动条 宽度 */
  background: #cccccc;   /* 整体背景 */
  border-radius: 10px;  /* 整体 圆角 */
}

body::-webkit-scrollbar-thumb {
  border-radius: 3px;
  height: 20px;    /* 滚动条滑块长度 */
  -webkit-box-shadow: inset005pxrgba(0,0,0,0.2);
  background:rgba(0,0,0,0.2);
}

.scroll::-webkit-scrollbar {
  width: 5px;          /* 纵向滚动条 宽度 */
  background: #cccccc;   /* 整体背景 */
  border-radius: 10px;  /* 整体 圆角 */
}

.scroll::-webkit-scrollbar-thumb {
  border-radius: 3px;
  height: 20px;    /* 滚动条滑块长度 */
  -webkit-box-shadow: inset005pxrgba(0,0,0,0.2);
  background:rgba(0,0,0,0.2);
}

address, cite, dfn, em, var, i {
  font-style: normal;
}

/* 将斜体扶正 */
b, strong {
  font-weight: normal;
}

/* 将粗体扶细 */
code, kbd, pre, samp, tt {
  font-family: "Courier New", Courier, monospace;
}

/* 统一等宽字体 */
small {
  font-size: 13px;
}

/* 小于 12px 的中文很难阅读，让 small 正常化 */

/* 重置文本格式元素 */
a {
  text-decoration: none;
  color: #666;
}


/* 重置表单元素 */
legend {
  color: #000;
}

/* for ie6 */
fieldset, img {
  border: none;
}

button, input, select, textarea {
  font-size: 100%; /* 使得表单元素在 ie 下能继承字体大小 */
}

/* 重置表格元素 */
table {
  border-collapse: collapse;
  border-spacing: 0;
}

/* 重置 hr */
hr {
  border: none;
  height: 1px;
}

/* 让非ie浏览器默认也显示垂直滚动条，防止因滚动条引起的闪烁 */
html {
  overflow-y: scroll;
}

.clear::after {
  content: "";
  display: block;
  clear: both;
}

.cursor {
  cursor: pointer;
}

html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn, em, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup, tt, var, b, u, i, center, dl, dt, dd, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas, details, embed, figure, figcaption, footer, header, hgroup, menu, nav, output, ruby, section, summary, time, mark, audio, video {
  border: 0;
  font-family: inherit;
  font-size: 100%;
  font-style: inherit;
  margin: 0;
  outline: 0;
  padding: 0;
  vertical-align: baseline;
  list-style: none;
}

html {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  font-size: 62.5%;
  -webkit-text-size-adjust: 100%;
  -ms-text-size-adjust: 100%;
}

*,
*:before,
*:after {
  -webkit-box-sizing: inherit;
  -moz-box-sizing: inherit;
  box-sizing: inherit;
}

body,
button,
input,
select,
textarea {
  font: 14px "Microsoft YaHei", Helvetica, Arial, Lucida Grande, Tahoma, sans-serif;
  color: #444;
  line-height: 180%;
  background: #f1f1f1;
  outline-style: none;
}

/* 清除内外边距 */
body, h1, h2, h3, h4, h5, h6, hr, p, blockquote,
dl, dt, dd, ul, ol, li,
pre,
fieldset, lengend, button, input, textarea,
th, td {
  margin: 0;
  padding: 0;
}

/* 设置默认字体 */
body,
button, input, select, textarea { /* for ie */
  /*font: 12px/1 Tahoma, Helvetica, Arial, "宋体", sans-serif;*/
  font-family: PingFang SC,Hiragino Sans GB,Arial,Microsoft YaHei,Verdana,Roboto,Noto,Helvetica Neue,sans-serif;
  color: #333;
}

.cursor{
  cursor: pointer;
}
.clean::after{
  content: '';
  clear: both;
  display: block;
}

img {
  border-style: none;
  border: 0;
  outline: none;
}

.fixed{
  width: 311px;
  position: fixed;
  top: 0;
  z-index: 1;
}

.strong{
  font-weight: 600;
}

</style>
