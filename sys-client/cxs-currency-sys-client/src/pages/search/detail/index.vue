<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb :breadcrumb="true"></MyBreadcrumb>
    <div id="primary" class="content-area"
         v-loading="lodding"
         element-loading-text="拼命加载中"
         element-loading-spinner="el-icon-loading">
      <div class="toolbar">
        <div class="toolbar-left">
          当前展示
          <span v-if="pageQuery.typeId && otherResult.type" style="color: red">
            {{ otherResult.type.typeName }}
          </span>
          <span v-if="pageQuery.tagId && otherResult.tag" style="color: red">
            {{ otherResult.tag.tagName }}
          </span>
          <span style="color: red" v-else>
            {{ keyword }}
          </span>
          的搜索结果，共 {{ count }} 条数据
          <span v-if="checkedTags && checkedTags.length > 0">
            搜索标签:
            <el-tag
                v-for="tag in checkedTags"
                :key="tag.tagId"
                size="small"
                style="margin:3px">
                {{tag.tagName}}
              </el-tag>
          </span>
        </div>
      </div>
      <main v-if="articleList && articleList.length > 0" class="site-main" role="main">
        <ArticleItem :origin="2" v-for="(item, index) in articleList" :key="'a_' + index" :article="item"></ArticleItem>
      </main>
      <main v-else class="site-main" role="main">
        <el-empty description="暂无更多匹配条件的结果"></el-empty>
      </main>
    </div>
    <div id="sidebar" class="widget-area all-sidebar">
      <aside class="widget widget_search">
        <div class="searchbar">
          <el-form :inline="true" id="searchform1" class="demo-form-inline" >
            <div style="margin-bottom: 14px">
              <el-select v-model="pageQuery.sort" placeholder="排序方式" style="width: 100%">
                <el-option
                    v-for="item in sortList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </div>
<!--            <div class="box">-->
<!--              <el-tag-->
<!--                  v-for="tag in checkedTag"-->
<!--                  :key="tag.tagId"-->
<!--                  size="small"-->
<!--                  style="margin:3px"-->
<!--                  @close="handleClose(tag)"-->
<!--                  closable>-->
<!--                {{tag.tagName}}-->
<!--              </el-tag>-->
<!--            </div>-->
            <div class="search">
              <el-input v-model="pageQuery.keyWord" placeholder="请输入内容" style="width: 70%"></el-input>
              <el-button type="primary" style="width: 28%" @click="keyWordSearchSubmit()">搜索</el-button>
            </div>
          </el-form>
        </div>
        <div class="clear"></div>
      </aside>
      <MyTags :handle="clickTags"></MyTags>
      <ArticleSide title="热门文章" :class="{'fixed' : fiexd}" :list="sysRecommendArticleList"></ArticleSide>
      <HotSearchSide title="推荐搜索" :list="hotSearchArticleList"></HotSearchSide>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import ArticleItem from '@/components/article-item'
  import MyTags from '@/components/tags'
  import {getArticleList} from "@/api/article";
  import ArticleSide from '@/components/side/article-side'
  import HotSearchSide from '@/components/side/hot-search-side'

  const DEFAULT_PAGENUM = 1;
  const DEFAULT_PAGESIZE = 5;
  export default {
    name: "SearchDetail",
    components:{
      ArticleItem,
      MyTags,
      ArticleSide,
      HotSearchSide
    },
    watch:{
      "$route.query": {
        // 处理函数（不能修改名字） handler('新的值'，'旧的值')
        handler(new_value, old_value) {
          // 执行代码部分
          if (new_value.keyword !== '' && new_value.keyword !== old_value.keyword) {
            this.$store.dispatch('header/setbreadcrumbAppend', [new_value.keyword])
            this.pageQuery.keyWord = new_value.keyword
            this.getArticleDataList(1)
          }
          if (new_value.typeId && new_value.typeId !== old_value.typeId) {
            // this.$store.dispatch('header/setbreadcrumbAppend', [new_value.keyword])
            this.pageQuery.typeId = new_value.typeId
            this.getArticleDataList(1)
          }
          if (new_value.tagId && new_value.tagId !== old_value.tagId) {
            // this.$store.dispatch('header/setbreadcrumbAppend', [new_value.keyword])
            this.pageQuery.tagId = new_value.tagId
            this.getArticleDataList(1)
          }
        },
      },
      "$route.params": {
        // 处理函数（不能修改名字） handler('新的值'，'旧的值')
        handler(new_value, old_value) {
          // 执行代码部分
          if (new_value.typeId && new_value.typeId !== old_value.typeId) {
            this.pageQuery.typeId = new_value.typeId
            this.$store.dispatch('header/setbreadcrumbAppend', this.getTypeArray())
            this.getArticleDataList(1)
          }
        },
      }
    },
    data(){
      return {
        breadcrumbList: [],
        checkedTag: [],
        keyword:'',
        otherResult:{},
        lodding: false,
        noPage: false,
        pageQuery:{
          keyWord: '',
          tags: [],
          typeId: undefined,
          tagId: undefined,
          sort: 2,
          pageNum: 1,
          pageSize: 5
        },
        sortList: [
          {
            id: 1,
            name: '阅读数优先'
          },
          {
            id: 2,
            name: '发布时间优先'
          },
          // {
          //   id: 3,
          //   name: '阅读数优先'
          // }
        ],
        // 无感知分页
        fiexd: false,
        pages:0,
        count: 0,
        oldScrollTop: 0,
        articleList:[]
      }
    },
    computed: {
      ...mapGetters(['tagList','typeList','typeListArr','hotSearchArticleList', 'sysRecommendArticleList']),
      checkedTags(){
        let temp = [];
        if (this.otherResult.tags && this.otherResult.tags.length > 0) {
          temp = this.tagList.map(t => {
            if (this.otherResult.tags.indexOf(t.tagId) !== -1) {
              return t;
            }
          }).filter(r => r)
          return temp;
        }
      }
    },
    mounted() {
      this.init()
      this.$nextTick(() => {
        //监听这个dom的scroll事件
        window.addEventListener("scroll", this.handleScroll);
      });
    },
    beforeDestroy() {
      window.removeEventListener("scroll", this.handleScroll);
    },
    methods: {
      getTypeArray(){
        let arr = []
        const find = this.typeListArr.find(l => l.id === this.pageQuery.typeId)
        arr.push(find ? find.typeName : '')
        return arr
      },
      init(){
        this.pageQuery.keyWord = this.$route.query.keyword || ''
        this.pageQuery.typeId = this.$route.params.typeId ? Number.parseInt(this.$route.params.typeId) : undefined
        this.pageQuery.tagId = this.$route.query.tagId ? Number.parseInt(this.$route.query.tagId) : undefined
        if (this.pageQuery.typeId && this.pageQuery.typeId !== 0) {
          this.$store.dispatch('header/setbreadcrumbAppend', this.getTypeArray())
        } else {
          this.$store.dispatch('header/setbreadcrumbAppend', [this.$route.query.keyword])
        }
        this.getArticleDataList(1)
      },

      clickTags(val){
        if (this.checkedTag.indexOf(val) !== -1) {
          this.checkedTag.splice(this.checkedTag.indexOf(val), 1);
          this.pageQuery.tags.splice(this.pageQuery.tags.indexOf(val.tagId), 1 )
        } else {
          if (this.checkedTag && this.checkedTag.length >= 5) {
            this.$message.warning('最多可同时搜索5个标签')
          } else {
            this.checkedTag.push(val)
            this.pageQuery.tags.push(val.tagId)
          }
        }
      },

      handleClose(val) {
        this.checkedTag.splice(this.checkedTag.indexOf(val), 1);
        this.pageQuery.tags.splice(this.pageQuery.tags.indexOf(val.tagId), 1 );
      },

      keyWordSearchSubmit(){
        this.pageQuery = {...this.pageQuery, pageNum: DEFAULT_PAGENUM, pageSize: DEFAULT_PAGESIZE}
        if (this.pageQuery.keyWord === '') {
          this.$store.dispatch('header/setbreadcrumbAppend', this.getTypeArray())
        } else {
          this.$store.dispatch('header/setbreadcrumbAppend', [this.pageQuery.keyWord])
        }
        // this.$router.replace({name: 'page-refresh', query: {keyword: this.pageQuery.keyWord}})
        this.getArticleDataList(1)
      },
      pageHandle() {
        if (this.pageQuery.pageNum < this.pages && !this.noPage && !this.lodding) {
          this.pageQuery.pageNum = this.pageQuery.pageNum + 1
          this.getArticleDataList(0)
        }
      },
      handleScroll() {
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
        if (scrollTop + windowHeight > scrollHeight - 300) {
          //无感知分页获取数据
          if (scrollStep >= 0) {
            this.pageHandle()
          }
        }
      },
      getArticleDataList(val) {
        this.lodding = true
        if (val === 1) {
          getArticleList(this.pageQuery).then(res => {
            this.lodding = false
            document.documentElement.scrollTop = 0
            this.count = res.data.total
            this.keyword = res.data.keyword
            this.otherResult = res.data.other
            const sortList = res.data.data.sort(this.sortArticle(this.pageQuery.sort, 'desc'))
            this.articleList = sortList
            this.pages = res.data.pages
            if(!res.data.data || res.data.data.length === 0) {
              this.noPage = true
            }

            if(this.pageQuery.typeId && this.pageQuery.typeId !== 0) {
              if (res.data.other.type) {
                this.$store.dispatch('header/setbreadcrumbAppend', [res.data.other.type.typeName])
              }
            }else if(this.pageQuery.tagId && this.pageQuery.tagId !== 0) {
              if (res.data.other.tag) {
                this.$store.dispatch('header/setbreadcrumbAppend', [res.data.other.tag.tagName])
              }
            }
          })
        } else {
          getArticleList(this.pageQuery).then(res => {
            this.lodding = false
            this.count = res.data.total
            this.keyword = res.data.keyword
            this.otherResult = res.data.other
            const sortList = this.articleList.concat(res.data.data).sort(this.sortArticle(this.pageQuery.sort, 'desc'))
            this.articleList = sortList
            this.pages = res.data.pages
            if(!res.data.data || res.data.data.length === 0) {
              this.noPage = true
            }
          })
        }
      },
      sortArticle(prop, flag){
        return (a ,b) => {
          if (prop === 3) {
            if (flag === 'desc') {
              return b.articleRate - a.articleRate;
            } else {
              return a.articleRate - b.articleRate;
            }
          } else if(prop === 2) {
            if (flag === 'desc') {
              return new Date(b.createTime) - new Date(a.createTime);
            } else {
              return new Date(a.createTime) - new Date(b.createTime);
            }
          } else if(prop === 3) {
            if (flag === 'desc') {
              return b.readNum - a.readNum;
            } else {
              return a.readNum - b.readNum;
            }
          }
        }
      }
    }
  }
</script>

<style scoped lang="less">
.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  #primary {
    float: left;
    width: 70.2%;
    transition-duration: .5s;
  }

  #sidebar {
    float: right;
    width: 28.8%;
    position: relative;
    overflow: visible;
    box-sizing: border-box;

    h3 {
      background: #f8f8f8;
      height: 40px;
      line-height: 40px;
      border-bottom: 1px solid #ddd;
    }

    .widget {
      background: #fff;
      margin: 0 0 10px 0;
      border: 1px solid #ddd;
      border-radius: 2px;
      box-shadow: 0 1px 1px rgb(0 0 0 / 4%);

      h3 {
        background: #f8f8f8;
        height: 40px;
        line-height: 40px;
        border-bottom: 1px solid #ddd;

        i {
          float: left;
          width: 40px;
          height: 40px;
          font-size: 18px;
          color: #009688;
          line-height: 40px;
          text-align: center;
          margin: 0 15px 0 0;
          padding: 1px 0;
          border-right: 1px solid #ddd;
          border-radius: 2px 0 0 0;
        }
      }

      #feed_widget {
        .feed-about {
          position: relative;
          font-size: 18px;
          display: block;

          .about-main {
            font-size: 14px;
            padding: 10px 15px 0 15px;

            .about-img {
              img {
                float: left;
                width: 120px;
                height: auto;
                margin: 5px 10px 0 0;
                padding: 2px;
                border-radius: 2px;
                border: 1px solid #ddd;
              }
            }

            .about-me {
              padding: 5px 0;

              .about-name {
                font-size: 15px;
                font-weight: 700;
                margin-bottom: 5px;
              }
            }
          }

          .clear {
            clear: both;
            display: block;
          }
        }

        a {
          color: #999;
          width: 40px;
          height: 40px;
          display: block;
          text-align: center;
          margin: 0 auto;
          border-radius: 4px;
          border: 1px solid #ddd;
          cursor: pointer;
        }

        a:hover {
          color: #fff;
        }

        span {
          display: block;
        }

        .weixin a:hover {
          background: #409eff;
          border: 1px solid #409eff;
        }

        .tqq a:hover {
          background: #4e91d1;
          border: 1px solid #4e91d1;
        }

        .tsina a:hover {
          background: #c40000;
          border: 1px solid #c40000;
        }

        .feed a:hover {
          background: #d28300;
          border: 1px solid #d28300;
        }

        li {
          float: left;
          width: 28.8%;
          height: 40px;
          line-height: 38px;
        }

        ul {
          margin: 0 10px;
          overflow: hidden;
          padding: 15px;

          li {
            white-space: nowrap;
            word-wrap: normal;
            text-overflow: ellipsis;
            overflow: hidden;
          }
        }

        .about-inf {
          text-align: center;
          background: #f8f8f8;
          float: left;
          font-size: 14px;
          width: 100%;
          padding: 0 10px;
          border-top: 1px solid #ddd;

          span {
            float: left;
            width: 50%;
            padding: 10px 0 10px 0;
            height: 40px;
          }

          .about-pn {
            border-right: 1px solid #ddd;
          }
        }

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

      .searchbar{
        width: 90%;
        margin: 10px auto 0;

        div.box{
          border: 1px solid #DCDFE6;
          width: 100%;
          min-height: 40px;
          margin-bottom: 14px;
          cursor: pointer;
        }

        div.sort{
          .f-sort{
            float: left;
            margin-right: 13px;

            a {
              border-color: #e4393c;
              background: #e4393c;
              color: #FFF;
              float: left;
              padding: 0 9px;
              height: 23px;
              border: 1px solid #CCC;
              line-height: 23px;
              margin-right: -1px;
              background: #FFF;
              color: #333;

              .fs-tit {
                display: inline-block;
                vertical-align: top;
                *cursor: pointer;
              }
            }
          }
        }

        div.search{
          display: flex;
          justify-content: space-between;
          align-items: center;
        }

        div.box:hover{
          border-color: #C0C4CC;
        }

        span {
          input {
            float: left;
            width: 70.2%;
            height: 37px;
            line-height: 37px;
            font: 14px "Microsoft YaHei", Helvetica;
            padding: 2px 10px;
            background: #ebebeb;
            border: 1px solid #ebebeb;
            border-radius: 2px 0 0 2px;
            -webkit-appearance: none;
          }

          button {
            overflow: visible;
            position: relative;
            border: 0;
            cursor: pointer;
            height: 37px;
            width: 28.8%;
            color: #fff;
            background: #409eff;
            border-radius: 0 2px 2px 0;
          }
        }

        #searchform1{
          padding: 15px;
        }
      }
    }

    .php_text .widget-text {
      padding: 3px;
    }

    .tagcloud {
      padding: 5px 0 5px 2px;

      a {
        float: left;
        margin: 4px;
        padding: 0 7px;
        line-height: 26px;
        text-align: center;
        border: 1px solid #ddd;
        border-radius: 2px;
        box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
      }

      a:hover {
        background: #409eff;
        color: #fff !important;
        border: 1px solid #409eff;
        transition: all 0.1s ease-in 0s;
      }

      a:visited {
        color: #555;
      }
    }
  }
}
</style>
