<template>
  <div class="widget">
    <h3 class="aside-title widget-title">所有标签</h3>
    <div class="item-rank"></div>
    <div class="tagcloud aside-content clean">
      <span class="tag-link-129 tag-link-position-1" v-for="item in tagList" :key="item.tagId" @click="clickTagHandle(item)">
        {{ item.tagName }}
      </span>
      <div class="clear"></div>
    </div>
    <div class="clear"></div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";


  export default {
   name: "MyTags",
    props: {
      handle: {
        type: Function,
        default: undefined
      }
    },
    data(){
     return {

     }
    },
    mounted() {
     this.init()
    },
    computed: {
     ...mapGetters(['tagList'])
    },
    methods:{
     init(){

     },
     clickTagHandle(item){
       this.handle(item)
     }
    }
  }
</script>

<style scoped lang="less">
.widget {
  background: #fff;
  margin: 0 0 10px 0;
  border: 1px solid #ddd;
  border-radius: 2px;
  box-shadow: 0 1px 1px rgb(0 0 0 / 4%);

  h3.aside-title {
    background-color: white;
    color: #3d3d3d;
    padding: 0 16px;
    height: 38px;
    line-height: 38px;
    position: relative;
    font-size: 14px;
    font-weight: bold;
  }

  .item-rank {
    height: 1px;
    background-color: #f5f6f7;
    width: 268px;
    margin: auto;
  }

  ul {
    padding: 15px;

    li {
      width: 99%;
      line-height: 28px;
      white-space: nowrap;
      word-wrap: normal;
      text-overflow: ellipsis;
      overflow: hidden;
    }
  }
}

.php_text .widget-text {
  padding: 3px;
}

.tagcloud {
  padding: 12px 16px 16px 16px;

  span {
    font-size: 13px;
    float: left;
    margin: 4px;
    padding: 0 7px;
    line-height: 26px;
    text-align: center;
    border: 1px solid #ddd;
    border-radius: 2px;
    box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
  }

  span:hover {
    background: #409eff;
    color: #fff !important;
    border: 1px solid #409eff;
    transition: all 0.1s ease-in 0s;
    cursor: pointer;
  }

  span:visited {
    color: #555;
  }
}
</style>
