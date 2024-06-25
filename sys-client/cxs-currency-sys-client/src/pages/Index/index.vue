<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb :breadcrumb="isBread"></MyBreadcrumb>
    <div id="primary" class="content-area"
         v-loading="lodding"
         element-loading-text="拼命加载中"
         element-loading-spinner="el-icon-loading">
      <main id="main" class="site-main" role="main">
        <ArticleItem :origin="1" v-for="(item, index) in articleList" :key="'a_' + index" :article="item"></ArticleItem>
      </main>
    </div>
    <div id="sidebar" class="widget-area all-sidebar">
      <AboutSystem></AboutSystem>
      <ArticleSide title="官方推荐" :list="sysRecommendArticleList"></ArticleSide>
      <HotSearchSide v-if="hotSearchArticleList && hotSearchArticleList.length > 0" title="推荐搜索" :class="{'fixed' : fiexd}" :list="hotSearchArticleList"></HotSearchSide>
      <MyTags :handle="changeTag"></MyTags>
    </div>
  </div>
</template>

<script>
import ArticleItem from '@/components/article-item'
import MyPagination from '@/components/article-item/pagination'
import MyTags from '@/components/tags'
import {getArticleList} from "@/api/article";
import {getCache, setCache} from "@/utils/cache";
import {ARTICLE_INDEX_LIST_KEY} from "@/utils/cache_constent";
import ArticleSide from '@/components/side/article-side'
import HotSearchSide from '@/components/side/hot-search-side'
import {mapGetters} from "vuex";
import {URL_PREFIX} from "@/api/config";
import AboutSystem from '@/components/side/about-system'

const defaultPage = {
  pageNum: 1,
  pageSize: 5
}
export default {
  name: "index",
  components: {
    ArticleItem,
    MyPagination,
    MyTags,
    ArticleSide,
    HotSearchSide,
    AboutSystem
  },
  data() {
    return {
      baseUrl: URL_PREFIX,
      isBread: false,
      flag: false,
      articleList: [],
      count: 0,
      pages: 0,
      fiexd: false,
      oldScrollTop: 0,
      noPage: false,
      lodding: false,
      oneFlag: true,
      pageQuery: {
        tagId: undefined,
        typeId: undefined,
        pageNum: 1,
        pageSize: 5
      },
      // 列表加载中效果
      listLoading: false
    }
  },
  mounted() {
    this.init();
    this.$nextTick(() => {
      //监听这个dom的scroll事件
      window.addEventListener("scroll", this.handleScroll);
    });
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  computed: {
    ...mapGetters(['sysInfo', 'hotSearchArticleList', 'sysRecommendArticleList'])
  },
  watch: {
    pageQuery: {
      deep: true,
      handler(newValue, oldValue) {
        this.getArticleDataListPage()
      }
    }
  },
  methods: {
    init() {
      this.getArticleDataList()
    },
    getArticleDataList() {
      this.lodding = true
      getArticleList(defaultPage).then(res => {
        this.lodding = false
        this.count = res.data.total
        this.articleList = res.data.data
        this.pages = res.data.pages
      }).catch(err => {
        this.lodding = false
        const data = getCache(ARTICLE_INDEX_LIST_KEY)
        this.count = data.total
        this.articleList = data.data
        this.pages = data.pages
      })
    },
    // 翻页
    getArticleDataListPage() {
      this.lodding = true
      getArticleList(this.pageQuery).then(res => {
        this.lodding = false
        this.count = res.data.total
        this.articleList = this.articleList.concat(res.data.data)
        this.pages = res.data.pages
        if (!res.data.data || res.data.data.length === 0) {
          this.noPage = true
        }
      }).catch(err => {
        console.log(err);
        this.getArticleDataList()
      })
    },
    queryChange(pageNum) {
      this.pageQuery.pageNum = pageNum
    },
    handleCurrentChange(pageNum) {
      this.pageQuery.pageNum = pageNum
    },
    pageHandle() {
      if (this.pageQuery.pageNum < this.pages && !this.noPage && !this.lodding) {
        this.pageQuery.pageNum = this.pageQuery.pageNum + 1
      }
    },
    handleScroll() {
      let height = 0;
      if (this.$device.windows) {
        height = 300
      } else {
        height = 1000
      }
      let sidebar = document.getElementById("sidebar")
      const sidebarHeight = sidebar.offsetHeight
      // 滚动条距文档顶部的距离
      let scrollTop =
          window.pageYOffset ||
          document.documentElement.scrollTop ||
          document.body.scrollTop;
      // 滚动条滚动的距离
      let scrollStep = scrollTop - this.oldScrollTop;
      this.oldScrollTop = scrollTop;
      if (scrollTop > sidebarHeight) {
        this.fiexd = true
      } else {
        this.fiexd = false
      }

      //变量windowHeight是可视区的高度
      let windowHeight =
          document.documentElement.clientHeight || document.body.clientHeight;
      //变量scrollHeight是滚动条的总高度
      let scrollHeight =
          document.documentElement.scrollHeight || document.body.scrollHeight;

      //滚动条到底部的条件
      if (scrollTop + windowHeight > scrollHeight - height) {
        //无感知分页获取数据
        if (scrollStep >= 0 && !this.lodding) {
          this.pageHandle()
        }
      }
    },
    changeTag(item) {
      this.$router.push({name: 'search-detail', query: {tagId: item.tagId}})
    }
  }
}
</script>

<style scoped lang="less">
.mobile-content{
  width: 100%;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px;
  box-sizing: border-box;

  #primary {
    float: left;
    width: 100%;
    min-height: 80vh;
    transition-duration: .5s;
  }
}
.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  #primary {
    float: left;
    width: 70.2%;
    min-height: 80vh;
    transition-duration: .5s;
  }

  #sidebar {
    float: right;
    width: 28.8%;
    position: relative;
    overflow: visible;
    box-sizing: border-box;
  }
}
</style>
