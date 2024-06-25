<template>
  <article class="post type-post" v-if="article">
    <figure class="thumbnail">
      <a class="cursor" @click="toDetail(article)">
        <el-image style="width: 200px;height: 150px; border: 1px solid #cccccc"
                  :src="article.articleCover ? baseurl + article.articleCover : article.articleCover"
                  class="attachment-content size-content wp-post-image"
                  :alt="article.articleTitle">
          <div slot="error" class="image-slot" style="width: 200px;height: 150px;display: flex;justify-content: center;align-items: center">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </a>
      <span class="cat">
        <a class="cursor" @click="toSearch(article.typeId)" >{{ article.typeName }}</a>
      </span>
    </figure>
    <header class="entry-header">
      <h2 class="entry-title">
        <a class="cursor article-title" rel="bookmark" @click="toDetail(article)">
          {{ article.articleTitle }}
        </a>
      </h2>
    </header>

    <div class="entry-content">
      <div class="archive-content">
        {{ article.articleAbstract }}
      </div>
      <span class="entry-meta">
        <span class="date">
          {{ article.nickName }} 发布于&nbsp;{{ article.createTimeFormat }}&nbsp;&nbsp;
        </span>
        <span class="views">
          <i class="fel-icon-view"></i>
          {{ article.readNum }} <i class="el-icon-view"></i>
        </span>
        <span class="comment">&nbsp;&nbsp;
          <a href="javascript:void(0)" @click.stop.prevent="toComment(article)" class="cursor" rel="external nofollow">
            <i class="el-icon-s-comment"></i>
            发表评论
          </a>
        </span>
      </span>
      <div class="clear"></div>
    </div><!-- .entry-content -->

    <span class="entry-more">
        <a class="cursor" @click="toDetail(article)" :class="{'active-btn':article.readFlag}" rel="bookmark">
            阅读全文
        </a>
    </span>
  </article>
</template>

<script>
import { URL_PREFIX } from '@/api/config'
import errorImg from '@/assets/error.png'
import {mapGetters} from "vuex";
import {markArticleSearchLog, readArticle} from "@/api/article";
export default {
  name: "ArticleItem",
  props: {
    article: {
      type: Object,
      default: () => {}
    },
    origin: {
      type: Number,
      default: 1
    }
  },
  mounted() {

  },
  computed:{
    ...mapGetters(['isLogin'])
  },
  data(){
    return {
      baseurl: URL_PREFIX,
      errorImg: errorImg,
      colors: ['#99A9BF', '#F7BA2A', '#FF9900']
    }
  },
  methods: {
    toDetail(article){
      this.$router.push({name: 'article-detail', params: {id: article.articleId}})
      this.$store.dispatch('main/setBreadcrumbList', [article.articleTitle])
      if (this.origin === 2) {
        markArticleSearchLog(this.article.articleId).then(() => {})
      }
      if (this.isLogin) {
        if (!article.readFlag) {
          // 阅读文章状态
          readArticle(article.articleId)
        }
      }
    },
    toComment(article){
      this.$router.push({name: 'article-detail', params: {id: article.articleId}, query: {flag: true}})
      this.$store.dispatch('main/setBreadcrumbList', [article.articleTitle])
      if (this.origin === 2) {
        markArticleSearchLog(this.article.articleId).then(() => {})
      }
      if (this.isLogin) {
        if (!article.readFlag) {
          // 阅读文章状态
          readArticle(article.articleId)
        }
      }
    },
    toSearch(typeId){
      this.$router.push({name: 'search-detail', params: {typeId}})
    }
  }
}
</script>

<style scoped lang="less">
.post {
  position: relative;
  background: #fff;
  margin: 0 0 10px 0;
  padding: 20px;
  border: 1px solid #ddd;
  box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
  border-radius: 2px;

  .thumbnail {
    position: relative;
    float: left;
    max-width: 200px;
    width: 200px;
    height: 150px;
    height: auto;
    clear: both;
    margin: 1px 20px 0 0;
    overflow: hidden;
    transition-duration: .5s;

    a {
      -webkit-tap-highlight-color: rgba(255, 0, 0, 0);

      img {
        float: left;
        width: 200px;
        height: 150px;
        max-width: 100%;
        border: 0;
      }
    }

    a:visited {
      color: #555;
    }

    .cat {
      position: absolute;
      top: 0;
      left: 0;
      box-shadow: 0 1px 1px rgb(0 0 0 / 10%);
      background: 0 0 rgba(216, 0, 0, 0.7);

      a {
        color: #fff;
        line-height: 28px;
        padding: 0 10px;
      }
    }
  }

  .entry-header {
    h2 {
      margin: 0 0 5px 0;

      .article-title{
        display: flex;
        align-items: center;
        justify-content: space-between;
      }

      a {
        font-size: 1.6rem;
        line-height: 20px;
      }
    }
  }

  .entry-content {
    .archive-content {
      color: #777;
      overflow: hidden;
      white-space: normal;
      word-break: break-word;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
    }

    .title-l {
      position: absolute;
      background: #c40000;
      top: 20px;
      left: -1px;
      width: 5px;
      height: 25px;
      box-shadow: 0 1px 1px rgb(0 0 0 / 10%);
    }

    .new-icon {
      font-size: 12px;
      position: absolute;
      background: #c40000;
      top: -1px;
      right: -1px;
      line-height: 20px;
      color: #fff;
      padding: 0 5px;
      border-radius: 0 2px 0 0;
      -webkit-animation: fade-in 1.2s;
    }

    .entry-meta {
      position: absolute;
      bottom: 14px;
      color: #999;
      left: 240px;
      font-size: 14px;

      a {
        color: #409eff;
      }
    }

    .clear {
      clear: both;
      display: block;
    }
  }

  .entry-more {
    .active-btn{
      background-color: #84C0FF;
    }
    a {
      position: absolute;
      bottom: 18px;
      right: -1px;
      background: #409eff;
      color: #fff;
      line-height: 30px;
      padding: 0 12px;
      display: block;
      border-radius: 2px 0 0 2px;
    }
  }
}
</style>