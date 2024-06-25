<template>
  <div id="asideHotArticle" class="aside-box sidebar" v-if="list && list.length > 0">
    <h3 class="aside-title">{{ title }}</h3>
    <div class="item-rank"></div>
    <div class="aside-content">
      <ul class="hotArticle-list">
        <li v-for="item in list" :key="item.articleId">
          <div @click.stop="$router.push({name: 'article-detail', params: {id: item.articleId}})">
            【 {{ item.typeName }} 】{{ item.articleTitle }}
            <img :src="readCountWhite" alt="">
            <span class="read">{{ item.readCount }}</span>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import readCountWhite from '@/assets/readCountWhite.png'
export default {
  name: "HotArticleSide",
  props: {
    title: {
      type: String,
      default: ''
    },
    list:{
      type: Array,
      default: () => []
    }
  },
  data(){
    return {
      readCountWhite:readCountWhite
    }
  }
}
</script>

<style scoped lang="less">
ol, ul, li {
  list-style: none;
}
#asideHotArticle{
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

  div.aside-content {
    padding: 12px 16px 16px 16px;
    overflow: hidden;

    *{
      word-wrap: break-word;
    }

    .hotArticle-list {
      margin-top: -8px;

      li {
        margin-top: 8px;

        div{
          cursor: pointer;
          display: block;
          word-wrap: break-word;
          color: #555666;

          img {
            width: 14px;
            height: 14px;
            vertical-align: -3px;
            margin-right: 3px;
            margin-left: 4px;
          }

          .read {
            font-size: 12px;
            color: #999aaa;
            line-height: 24px;
          }
        }
      }
    }
  }
}

.aside-box{
  margin-bottom: 8px;
  background-color: #fff;
  border-radius: 2px;
}
</style>
