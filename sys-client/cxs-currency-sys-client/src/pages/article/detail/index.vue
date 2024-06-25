<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb :breadcrumb="true"></MyBreadcrumb>
    <div id="primary" class="content-area" :class="{'width-full' : !slider}">
      <div class="content-all" ref="xqBox"
           v-if="articleInfo"
           v-loading="loading"
           element-loading-text="拼命加载中"
           element-loading-spinner="el-icon-loading">
        <div class="content-header">
          <ul class="single-meta">
            <li class="download">
              <a rel="external nofollow" title="举报资源" @click="reportClick">
                <i class="el-icon-warning-outline"></i>
                <span class="comment-count">举报</span>
              </a>
            </li>
            <li class="download" v-if="articleInfo.fileInfo && articleInfo.fileInfo.fileId">
              <a rel="external nofollow" v-anchor="'download'" title="下载资源">
                <i class="el-icon-download"></i>
                <span class="comment-count">下载</span>
              </a>
            </li>
            <li class="comment">
              <a href="javascript:void(0)" rel="external nofollow" @click.prevent="commentClick" title="评论数">
                <i class="el-icon-chat-dot-round"></i>
                <span class="comment-count"> {{ articleInfo.commentNum }}</span>
              </a>
            </li>
            <li class="views" title="阅读数">
              <i class="el-icon-view"></i>
              <span class="articleViewCount"> {{ articleInfo.readNum }} </span>
            </li>
            <li class="r-hide">
              <a href="javascript:void(0)" title="侧边栏" class="slider" @click="slider = !slider">
                <i class="el-icon-caret-left" style="margin-right: -7px"></i>
                <i class="el-icon-caret-right"></i>
              </a>
            </li>
          </ul>
        </div>
        <h2 class="title">{{ articleInfo.articleTitle }}
          <el-tag
              v-if="articleInfo.articleIsSelf === 1"
              type=""
              size="mini"
              style="margin-left: 5px;"
              effect="plain">
            原创
          </el-tag>
          <el-tag
              v-else
              type="info"
              style="margin-left: 5px;"
              size="mini"
              effect="plain">
            非原创
          </el-tag>
        </h2>
        <v-md-editor :value="articleInfo.articleDetail" mode="preview"></v-md-editor>
        <div class="content-footer">
          <div class="reward">
            <div class="social-main">
              <span class="like">
                  <a @click.stop="likeArticleHandle(articleInfo)" data-action="ding" data-id="1" title="点赞"
                     class="favorite">
                      <i class="iconfont icon-dianzan" :class="{'i-active' : articleInfo.likeFlag}"></i>赞
                      <i class="count">{{ articleInfo.likeNum }}</i>
                  </a>
              </span>
              <div class="shang-p">
                <div class="shang-empty"><span></span></div>
                <span class="shang-s">
                  <a style="cursor:pointer" @click.stop="rewardHandle()">转发</a>
                  <share-dialog :visible.sync="shareDialogVisible"></share-dialog>
                </span>
              </div>
              <div class="share-sd">
                <span class="share-s" style="margin-top: 25px!important;">
                    <a @click.stop="collectionArticleHandle(articleInfo)" id="share-s" title="收藏">
                        <i class="iconfont icon-shoucang" :class="{'i-active' : articleInfo.collectionFlag}"></i> 收藏
                      <i class="count">{{ articleInfo.collectionNum }}</i>
                    </a>
                </span>
              </div>
              <div class="clear"></div>
            </div>
          </div>

          <div class="article-file" id="position-download"
               v-if="articleInfo.fileInfo && articleInfo.fileInfo.fileSize !== undefined && articleInfo.fileInfo.fileName">
            <div class="file-info">
              <div>
                文件名：
                <el-link :underline="false" class="item" @click="download(articleInfo)">
                  {{ articleInfo.fileInfo.fileName }}
                </el-link>
                文件大小: {{ Math.floor(articleInfo.fileInfo.fileSize / 1024) }}KB
              </div>
              <el-button type="primary" size="small" icon="el-icon-download" @click="download(articleInfo)">下载文件
              </el-button>
            </div>
          </div>
          <div class="file-structure" v-if="(articleInfo.fileInfo && articleInfo.fileInfo.fileStructure)">
            <el-tree :render-content="renderContent" :default-expand-all="false" :expand-on-click-node="false"
                     :data="articleInfo.fileInfo.fileStructure" :props="{children: 'children',label: 'name'}"></el-tree>
          </div>
        </div>

        <el-divider></el-divider>

        <div class="article-extra">
          <div class="article-type" v-if="articleInfo.type">
            所属分类：
            <span class="item" style="font-weight: 600">{{ parentType }}</span>
            <i class="el-icon-arrow-right"></i>
            <el-link :underline="false" class="item"
                     @click="$router.push({name: 'search-detail', params: {typeId: articleInfo.type.id}})"
                     style="margin-left: 5px">{{ articleInfo.type.typeName }}
            </el-link>
          </div>
          <div class="article-tag" v-if="(articleInfo && articleInfo.tags)">
            所含标签:
            <el-tag style="margin-right:10px" class="cursor" v-for="tag in articleInfo.tags" :key="tag.tagId"
                    @click="$router.push({name:'search-detail', query:{tagId: tag.tagId}})">
              {{ tag.tagName }}
            </el-tag>
          </div>
        </div>

        <div class="divider">

        </div>
        <div class="share-notice">
          <div class="authorbio wow fadeInUp" v-if="articleInfo.articleIsSelf === 1 && articleInfo.userInfo">
            <img v-if="articleInfo.userInfo.avatar && articleInfo.userInfo.avatar.length > 0" :alt="articleInfo.userInfo.nickName" :title="articleInfo.userInfo.nickName" :src="baseUrl + articleInfo.userInfo.avatar" class="avatar avatar-64 photo" height="64" width="64">
            <ul class="postinfo">
              <li></li>
              <li>
                <span class="strong">版权声明：</span>本站原创文章，于{{ articleInfo.createTime }}，由
                <span class="strong">{{ articleInfo.userInfo.nickName }}</span> 发表。
              </li>
              <li class="reprinted">
                <span class="strong">转载请注明：</span>
                <a :href="fullPath" target="_blank" rel="bookmark" :title="'本文固定链接' + fullPath"> {{ articleInfo.articleTitle }} | {{ sysInfo.sysTitle }}</a>
              </li>
            </ul>
            <div class="clear"></div>
          </div>
          <div class="authorbio wow fadeInUp" v-if="articleInfo.articleIsSelf === 0 && articleInfo.userInfo">
            <img v-if="articleInfo.userInfo.avatar && articleInfo.userInfo.avatar.length > 0" :alt="articleInfo.userInfo.nickName" :title="articleInfo.userInfo.nickName" :src="baseUrl + articleInfo.userInfo.avatar" class="avatar avatar-64 photo" height="64" width="64">
            <ul class="postinfo">
              <li></li>
              <li>
                <span class="strong">文章信息：</span>非原创文章，于{{ articleInfo.createTime }}，由
                <span class="strong">{{ articleInfo.userInfo.nickName }}</span> 发表。
              </li>
              <li class="reprinted">
                <span class="strong">原文链接：</span>
                <a :href="articleInfo.originalLink" target="_blank" rel="bookmark" :title="'原文链接:' + articleInfo.originalLink"> {{ articleInfo.articleTitle }}</a>
              </li>
            </ul>
            <div class="clear"></div>
          </div>
        </div>
      </div>
      </div>

      <transition name="sidebar">
        <div id="sidebar" class="widget-area all-sidebar" key="sidebar" ref="sidebar" v-show="slider">
          <UserSide v-if="articleInfo" :user="articleInfo.userInfo"></UserSide>
          <ArticleSide title="官方推荐" :class="{'fixed' : fiexd}" :list="sysRecommendArticleList"></ArticleSide>
          <ArticleSide title="最新发布"></ArticleSide>
          <HotSearchSide v-if="hotSearchArticleList && hotSearchArticleList.length > 0" title="热门文章" :list="hotSearchArticleList"></HotSearchSide>
        </div>
      </transition>

<!--      <el-dialog title="转发" :visible.sync="dialogFormVisible" class="reward-model" :width="$device.windows ? '40%':'300px'">-->
<!--        <div v-if="rewardInfo">-->
<!--          <div class="reward-title">-->
<!--            <el-radio-group v-model="rewardType">-->
<!--              <el-radio :label="1">QQ</el-radio>-->
<!--              <el-radio :label="2">微信</el-radio>-->
<!--            </el-radio-group>-->
<!--          </div>-->
<!--          <div class="reward-img" v-if="rewardType === 1&& rewardInfo.weixinBorderColor && shareInfo.weixinImg"">-->
<!--            <div class="img-boder" :style="{borderColor: rewardInfo.weixinBorderColor}">-->
<!--              <img :src="baseUrl + shareInfo.weixinImg"/>-->
<!--            </div>-->
<!--            <button @click="shareToQQ">分享到 QQ</button>-->
<!--&lt;!&ndash;            <p class="reward-tip" :style="{color: rewardInfo.weixinBorderColor}">打赏</p>&ndash;&gt;-->
<!--          </div>-->

<!--          <div class="reward-img" v-if="rewardType === 2 && rewardInfo.zhifubaoBorderColor && rewardInfo.zhifubaoImg">-->
<!--            <div class="img-boder" :style="{borderColor: rewardInfo.zhifubaoBorderColor}">-->
<!--              <img :src="baseUrl + shareInfo.qqImg"/>-->
<!--            </div>-->
<!--            <p class="reward-tip" :style="{color: rewardInfo.zhifubaoBorderColor}">打赏</p>-->
<!--          </div>-->
<!--        </div>-->
<!--      </el-dialog>-->
    <el-dialog
        title="分享"
        :visible.sync="dialogFormVisible"
        class="reward-model" :width="$device.windows ? '40%':'300px'"
    >
<!--      <div class="share-icons">-->
<!--        <div-->
<!--            v-for="icon in icons"-->
<!--            :key="icon.name"-->
<!--            class="icon-item"-->
<!--            @click="handleClick(icon)"-->
<!--        >-->
<!--          <img :src="icon.src" :alt="icon.name" />-->
<!--          <p>{{ icon.name }}</p>-->
<!--        </div>-->
<!--      </div>-->
      <div class="share-content">
        <p>点击下面的图标分享</p>
        <div class="share-icons">
          <div
              v-for="icon in icons"
              :key="icon.name"
              class="icon-item"
              @click="handleClick(icon)"
          >
            <img :src="icon.src" :alt="icon.name" />
            <p>{{ icon.name }}</p>
          </div>
        </div>
        <div class="share-link">
          <p>分享链接:</p>
          <el-input v-model="shareLink" readonly></el-input>
          <el-button @click="copyLink">复制链接</el-button>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
      </span>
    </el-dialog>
<!--    <el-dialog title="转发" :visible.sync="dialogFormVisible" class="reward-model" :width="$device.windows ? '40%':'300px'">-->
<!--      <div v-if="rewardInfo">-->
<!--        <div class="reward-title">-->
<!--          <el-radio-group v-model="rewardType">-->
<!--            <el-radio :label="1">微信</el-radio>-->
<!--            <el-radio :label="2">QQ</el-radio>-->
<!--          </el-radio-group>-->
<!--        </div>-->
<!--        <div class="reward-img" v-if="rewardType === 1">-->
<!--          <div class="img-boder">-->

<!--            <img src="/images/weixin.jpg"/>-->
<!--          </div>-->

<!--        </div>-->

<!--        <div class="reward-img" v-if="rewardType === 2">-->
<!--          <div class="img-boder" >-->
<!--            <img :src="baseUrl + shareInfo.qqImg"/>-->
<!--          </div>-->

<!--        </div>-->
<!--      </div>-->
<!--    </el-dialog>-->

    <el-dialog title="下载文件" :visible.sync="downloadVisible" class="reward-model" :width="$device.windows ? '40%':'300px'">
      <div v-if="articleInfo && articleInfo.fileInfo">
        你要操作的是下载文件 {{ articleInfo.fileInfo.fileName }}, 文件大小: {{ Math.floor(articleInfo.fileInfo.fileSize / 1024)}} KB <span v-if="articleInfo.articleDownloadPoint >= 0">,本次下载需要消耗 {{articleInfo.articleDownloadPoint}} 积分(如已下载，不会重复扣除积分)</span>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="downloadVisible = false">取 消</el-button>
        <el-button type="primary" @click="downloadFile()">确 定</el-button>
      </div>
    </el-dialog>

      <el-dialog title="资源举报" :visible.sync="reportDialogVisible" class="reward-model" :width="$device.windows ? '50%':'300px'">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" :label-width="$device.windows ? '100px':'70px'" class="demo-ruleForm">
          <el-form-item label="举报类型" prop="reportType">
            <el-radio-group v-model="ruleForm.reportType" v-if="$device.windows">
              <el-radio :label="item" :key="index" v-for="(item, index) in reportTypeList">{{item}}</el-radio>
            </el-radio-group>
            <el-select v-model="ruleForm.reportType" v-else>
              <el-option :label="item" :value="item" v-for="item in reportTypeList" :key="item"/>
            </el-select>
          </el-form-item>
          <el-form-item label="举报图片" prop="avatar">
            <div :class="$device.windows ? 'pc' : 'phone'">
              <el-upload
                  class="avatar-uploader"
                  :action="baseUrl + '/base/file/upload'"
                  :headers="header"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess1"
                  :before-upload="beforeAvatarUpload">
                <img v-if="image1" :src="baseUrl + image1" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <el-upload
                  class="avatar-uploader"
                  :action="baseUrl + '/base/file/upload'"
                  :headers="header"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess2"
                  :before-upload="beforeAvatarUpload">
                <img v-if="image2" :src="baseUrl + image2" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
              <el-upload
                  class="avatar-uploader"
                  :action="baseUrl + '/base/file/upload'"
                  :headers="header"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess3"
                  :before-upload="beforeAvatarUpload">
                <img v-if="image3" :src="baseUrl + image3" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item label="举报内容" prop="reportContent">
            <el-input
                type="textarea"
                :rows="7"
                resize="none"
                placeholder="请输入举报内容"
                show-word-limit
                autocomplete="off"
                maxlength="1000"
                v-model="ruleForm.reportContent">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="canalReport">取 消</el-button>
          <el-button type="primary" size="small" @click="submitForm('ruleForm')">提交</el-button>
        </div>
    </el-dialog>

      <el-drawer
          size="40%"
          :title="'评论 ' + articleInfo.commentNum"
          :visible.sync="commentDrawer"
          direction="rtl">
        <div class="comment-all scroll">
          <CommentInputArea :comment-handle="commentHandle"></CommentInputArea>
          <div class="comment-content">
            <CommentItem v-for="item in commentList" :key="item.commentId" :comment="item" :replyFunc="replyFunc" :replySuccess="replySuccess" :operaSuccess="operaSuccess"></CommentItem>
            <div style="text-align: center">
              <el-link v-if="commentBean.data && commentBean.data.length > 0" :underline="false" type="info" :disabled="moreStatus" @click.native="moreComment">{{ moreTip }}</el-link>
            </div>
          </div>
        </div>
      </el-drawer>
    </div>
</template>

<script>
import {
  cancelCollectionArticle,
  cancelLikeArticle,
  collectionArticle,
  downloadBeforeValid,
  getArticleInfo,
  likeArticle
} from "@/api/article";
import MyTags from '@/components/tags'
import UserSide from '@/components/side/user-side'
import ArticleSide from '@/components/side/article-side'
import '@/assets/icon/iconfont.css'
import {mapGetters} from 'vuex';
import {getArticleCommentList, getUserRewardInfo} from "@/api/main";
import {URL_PREFIX} from "@/api/config";
import HotSearchSide from '@/components/side/hot-search-side'
import defaultAvatar from '@/assets/default-avatar.png'
import {publishComment} from "@/api/comment";
import CommentItem from '@/components/comment-item'
import CommentInputArea from '@/components/comment/comment-input-area'
import {getToken} from "@/utils/auth";
import {addReport} from "@/api/report";
import {ARTICLE_REPORT_TYPE} from "@/utils/constant";
import sharePage from "@/pages/share/SharePage.vue";


export default {
  name: "ArticleDetail",
  components: {
    MyTags,
    UserSide,
    ArticleSide,
    HotSearchSide,
    CommentItem,
    CommentInputArea,
    sharePage
  },
  data() {
    var validatereportType = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('举报类型为必选项'));
      }
      callback()
    };
    var validatereportContent = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('举报内容为必填项'));
      } else {
        if (value.length > 1000) {
          callback(new Error('举报内容不能大于1000字符'));
        }
        callback();
      }
    };
    return {
      icons: [
        { name: '朋友圈', src: '/server/upload/20240617/636e4f11cc1b46febe941808a3a9172920240617171155.png' },
        { name: '微信', src: '/server/upload/20240617/636e4f11cc1b46febe941808a3a9172920240617171128.png' },
        { name: '微博', src: '/server/upload/20240617/636e4f11cc1b46febe941808a3a9172920240617171307.png' },
        { name: 'QQ', src: '/server/upload/20240617/636e4f11cc1b46febe941808a3a9172920240617171022.png' },
        { name: 'QQ空间', src: '/server/upload/20240617/636e4f11cc1b46febe941808a3a9172920240617171348.png' }
      ],
      defaultAvatar: defaultAvatar,
      baseUrl: URL_PREFIX,
      articleInfo: {
        articleTitle: '这是一个示例文章标题',
        articleDetail: '## 文章详情\n这是文章的详细内容。'
      },
      shareLink: '',
      articleId: 0,
      // articleInfo: {},
      slider: true,
      rewardInfo: {},
      payImg:'',
      payBorder: '',
      queryList: {
        keyWord: '',
        tag: undefined
      },
      fiexd: false,
      // 打赏
      dialogFormVisible: false,
      rewardType: 1,
      downloadVisible: false,
      loading: false,
      // 下载
      downloadLodding: false,
      commentDrawer: false,
      commentTo: null,
      commentListParam: {
        pageNum: 1,
        pageSize: 5
      },
      commentBean: {},
      commentList: [],
      toUser: '',
      moreTip: '加载更多...',
      moreStatus: false,
      isLodding: false,

      shareDialogVisible: false,

      reportDialogVisible: false,
      reportTypeList: ARTICLE_REPORT_TYPE,
      ruleForm: {
        reportType: '',
        reportContent: '',
        reportImages: [],
        reportTarget: 1,
        reportTargetId: null
      },

      image1:'',
      image2:'',
      image3:'',
      rules: {
        reportType: [
          {validator: validatereportType, trigger: 'blur'}
        ],
        reportContent: [
          {validator: validatereportContent, trigger: 'blur'}
        ]
      }
    }
  },
  watch:{
    "$route.params" :{
      handler: function (newsValue, oldValue) {
        this.articleId = newsValue.id
        this.getArticleInfoHandle()
      }
    }
  },
  computed: {
    sharePage() {
      return sharePage
    },
    ...mapGetters(['typeListArr', 'sysInfo', 'isLogin','hotSearchArticleList', 'sysRecommendArticleList', 'user']),
    xqBox() {
      return this.$refs.xqBox;
    },
    parentType() {
      if (this.articleInfo && this.articleInfo.type) {
        const obj = this.typeListArr.find(t => t.id = this.articleInfo.type.id)
        return obj ? obj.typeName : ''
      } else {
        return ''
      }
    },
    fullPath(){
      return window.location.href;
    },
    header(){
      return {
        'access_token': getToken()
      }
    }
  },
  mounted() {
    this.articleId = this.$route.params.id || 0
    this.init()
    this.$nextTick(() => {
      //监听这个dom的scroll事件
      document.documentElement.scrollTop = 0
      window.addEventListener("scroll", this.handleScroll);
    });
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    init() {
      const flag = this.$route.query.flag
      if (flag) {
        this.articleInfo.articleId = this.articleId
        this.commentDrawer = true
        this.$nextTick(() => {
          this.commentListParam.pageNum = 1
          this.moreStatus = false
          this.moreTip = '加载更多...'
          this.getCommentList()
        })
      }
      this.getArticleInfoHandle()
      // 获取该用户的打赏信息
      getUserRewardInfo(this.articleId).then(res => {
        this.rewardInfo = res.data
      })
    },
    getArticleInfoHandle(){
      this.loading = true
      getArticleInfo(this.articleId).then(res => {
        if (res.data) {
          this.articleInfo = res.data
          this.loading = false
          this.$store.dispatch('header/setbreadcrumbAppend', res.data.articleTitle)
        } else {
          this.$message.error('文章不存在')
          this.$router.back()
        }
      })
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
      if (scrollTop > sidebarHeight) {
        this.fiexd = true
        this.$forceUpdate()
      } else {
        this.fiexd = false
        this.$forceUpdate()
      }
    },

    // 文件下载处理
    download(article) {
      if (this.isLogin) {
        this.downloadVisible = true
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    // 下载
    downloadFile(){
      const FilePath = '127.0.0.1:3000/server/upload/files\\20240619\\464bbe96c2514e85ac54116013105c3420240619223849.zip"';
      // 执行下载
      window.location.href = FilePath;
      // const {articleId, fileInfo} = this.articleInfo
      // downloadBeforeValid({articleId, fileId: fileInfo.fileId}).then(res => {
      //   const { code, data, msg} = res
      //   this.downloadVisible = false
      //   if (code === 200) {
      //     if (data.flag) {
      //       window.location.href = "/server/article/download?k=" + data.downToken
      //     }else {
      //       this.$message.error(msg)
      //     }
      //   }else {
      //     this.$message.error('文件下载失败,请稍后再试')
      //   }
      // })
    },
    // 点赞处理
    likeArticleHandle(article) {
      if (this.isLogin) {
        if (!article.likeFlag) {
          likeArticle(article.articleId).then(res => {
            this.articleInfo.likeFlag = true
            this.articleInfo.likeNum = this.articleInfo.likeNum + 1
          })
        } else {
          cancelLikeArticle(article.articleId).then(res => {
            this.articleInfo.likeFlag = false
            this.articleInfo.likeNum = this.articleInfo.likeNum - 1
          })
        }
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    // 收藏处理
    collectionArticleHandle(article) {
      if (this.isLogin) {
        if (!article.collectionFlag) {
          collectionArticle(article.articleId).then(res => {
            this.articleInfo.collectionFlag = true
            this.articleInfo.collectionNum = this.articleInfo.collectionNum + 1
          })
        } else {
          cancelCollectionArticle(article.articleId).then(res => {
            this.articleInfo.collectionFlag = false
            this.articleInfo.collectionNum = this.articleInfo.collectionNum - 1
          })
        }
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    // 自定义文件树的显示内容
    renderContent(h, {node, data, store}) {
      return (
          <span class="custom-tree-node">
            <span>
              {data.children && data.children.length > 0 ? <i class='el-icon-folder' style="color: gold;"></i> :
                  <i class='el-icon-tickets' style="color: #409eff;"></i>}
              &nbsp;{data.name}

            </span>&nbsp;&nbsp;
            <span>
              {data.size && data.size > 0 ? Math.floor(data.size / 1024) + 'KB' : ''}
            </span>
          </span>);
    },
    // 打赏按钮处理
    rewardHandle(){
      this.generateShareLink();
      this.dialogFormVisible = true;
      // const { userInfo } = this.articleInfo
      // if (userInfo.settings && userInfo.settings.openReward) {
      //   this.dialogFormVisible = true
      // } else {
      //   this.$message.info('用户暂未开启打赏功能')
      // }
    },
    generateShareLink() {
      const baseUrl = window.location.origin; // 基础 URL
      const articleTitle = encodeURIComponent(this.articleInfo.articleTitle);
      const articleUrl = encodeURIComponent(`${baseUrl}/articles/123`); // 示例 URL
      this.shareLink = `${baseUrl}/share?title=${articleTitle}&url=${articleUrl}`;
    },
    handleClick(icon) {
      console.log(`分享至${icon.name}: ${this.shareLink}`);
      // 处理点击事件
    },
    copyLink() {
      console.log(`链接已复制`);
      this.$alert(`链接已复制: ${this.shareLink}`, '成功', {
        confirmButtonText: '确定'
      });
      // this.$copyText(this.shareLink).then(
      //     () => {
      //       this.$message.success('链接已复制');
      //     },
      //     () => {
      //       this.$message.error('链接已复制');
      //     }
      // );
    },
    closeDialog() {
      this.dialogFormVisible = false;
    },
    handleClose() {
      this.dialogFormVisible = false;
    },
    // 评论点击
    commentClick(){
      this.commentDrawer = true
      this.commentListParam.pageNum = 1
      this.moreStatus = false
      this.moreTip = '加载更多...'
      this.getCommentList()
    },
    // 获取评论列表
    getCommentList() {
      const obj = {
        ...this.commentListParam,
        articleId: this.articleInfo.articleId
      }
      getArticleCommentList(obj).then(res => {
        this.commentBean = res.data
        this.commentList = res.data.data
      })
    },
    moreComment(){
      if (!this.isLodding) {
        this.isLodding = true
        this.commentListParam.pageNum = this.commentListParam.pageNum + 1
        const obj = {
          ...this.commentListParam,
          articleId: this.articleInfo.articleId
        }
        getArticleCommentList(obj).then(res => {
          this.isLodding = false
          const { data:resp } = res
          if (!resp.data || resp.data.length === 0) {
            this.moreTip = '暂无更多评论'
            this.moreStatus = true
          } else {
            this.moreTip = '加载更多...'
            this.moreStatus = false
          }
          this.commentBean = res.data
          this.commentList = this.commentList.concat(res.data.data)
        })
      }
    },
    // 评论按钮处理
    commentHandle(content){
      if (this.user && this.user.userId && this.user.userId.length > 0) {
        const comment = {
          articleId: this.articleInfo.articleId,
          commentContent: content,
          commentTo: this.commentTo ? this.commentTo : this.articleInfo.articleBelongUserId,
          parentCommentId: this.parentCommentId
        }
        publishComment(comment).then(res => {
          this.$message.success('评论成功')
          this.$bus.$emit('opera-value', '')
          this.getCommentList()
          this.articleInfo.commentNum = this.articleInfo.commentNum + 1
        })
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    replyFunc(item){
      this.toUser = item.commentFrom.nickName
    },
    // 回复成功
    replySuccess(){
      this.getCommentList()
      this.articleInfo.commentNum = this.articleInfo.commentNum + 1
    },
    // 回复成功
    operaSuccess(comment, type){
      if (type === 3) {
        this.articleInfo.commentNum = this.articleInfo.commentNum - 1
      }
      this.getCommentList()
    },

    // 用户点击举报
    reportClick(){
      if (this.user && this.user.userId && this.user.userId.length > 0) {
        this.reportDialogVisible = true
        this.ruleForm.reportTarget = 1
        this.ruleForm.reportTargetId = this.articleInfo.articleId
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },

    handleAvatarSuccess1(res, file) {
      this.image1 = res.data;
    },
    handleAvatarSuccess2(res, file) {
      this.image2 = res.data;
    },
    handleAvatarSuccess3(res, file) {
      this.image3 = res.data;
    },
    // 上传之前
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG && !isPNG) {
        this.$message.error('上传头像图片只能是 jpg或png 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return (isJPG && isLt2M) || (isPNG && isLt2M) ;
    },
    canalReport(){
      this.reportDialogVisible = false
      this.resetForm('ruleForm')
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        let arr = []
        if (this.image1 && this.image1.length > 0) {
          arr.push(this.image1)
        }
        if (this.image2 && this.image2.length > 0) {
          arr.push(this.image2)
        }
        if (this.image3 && this.image3.length > 0) {
          arr.push(this.image3)
        }
        this.ruleForm.reportImages = arr
        if (valid) {
          console.log(this.ruleForm);
          addReport(this.ruleForm).then(res => {
            this.$message.success(res.msg);
            this.image1 = '';
            this.image2 = '';
            this.image3 = '';
            this.ruleForm = {}
            this.reportDialogVisible = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped lang="scss">
.sidebar-enter, .sidebar-leave-to {
  margin-right: -100%;
  opacity: 0;
}

.sidebar-enter-to, .sidebar-leave {
  margin-right: 0%;
  opacity: 1;
}

.sidebar-enter-active, .sidebar-leave-avtive {
  transition: all 0.5s linear;
}

.width-full {
  width: 100% !important;
}

.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  .content-area {
    float: left;
    width: 70.2%;
    transition-duration: .5s;
    background-color: white;

    .content-all {

      .content-header {
        height: 50px;
        background-color: white;
        position: relative;

        ol, ul, li {
          list-style: none;
        }

        .back-list {
          position: absolute;
          color: #999;
          top: 12px;
          left: 15px;
          margin: 0;
          width: 60px;
          border: 1px solid #ddd;
          background: white;
          cursor: pointer;
          outline: none;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        .article-type {
          display: flex;
          align-items: center;
          height: 50px;
          line-height: 50px;
          padding: 0 10px;

          .item {
            cursor: pointer;
            margin-right: 5px;
          }
        }

        .back-list:hover {
          background-color: #409eff;
          color: white;
        }

        .single-meta {
          position: absolute;
          top: 15px;
          right: 15px;

          a {
            cursor: pointer;
          }

          a:hover {
            background-color: #409eff;
            color: white;
          }

          .download {
            a:hover {
              background-color: #409eff;
              color: white;
            }
          }

          .slider:hover {
            background-color: #409eff;
            color: white;
          }

          .comment {
            a:hover {
              background-color: #409eff;
              color: white;
            }
          }

          li {
            float: left;

            a {
              color: #999;
              line-height: 26px;
              margin: 0 5px 0 0;
              padding: 0 8px;
              display: block;
              border: 1px solid #ddd;
              border-radius: 2px;
            }
          }

          .views {
            color: #999;
            line-height: 26px;
            margin: 0 5px 0 0;
            padding: 0 8px;
            display: block;
            border: 1px solid #ddd;
            border-radius: 2px;
            cursor: pointer;
          }
        }
      }

      .title {
        text-align: center;
        padding: 10px 0;
        box-sizing: border-box;
        font-size: 18px;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #f8f8f8;

        .self {
          color: #409eff;
          background: rgb(81 107 250 / 10%);
          text-align: center;
          width: 34px;
          height: 20px;
          line-height: 20px;
          border-radius: 2px;
          font-size: 13px;
          margin-right: 8px;
        }
      }

      .content-footer {

        .article-file {
          background-color: #f8f8f8;
          border-left: 5px solid #409eff;
          border-right: 5px solid #409eff;
          line-height: 50px;
          padding: 0 10px;

          .file-info {
            height: 50px;
            display: flex;
            justify-content: space-between;
            align-items: center;
          }
        }

        .reward {
          position: relative;
          margin: 50px auto;

          a {
            cursor: pointer;
          }

          .i-active {
            color: #409eff;
          }

          .social-main {
            position: relative;
            margin: 0 auto;
            width: 243px;

            span {
              float: left;
            }

            .like {
              a {
                background: #fff;
                width: 120px;
                display: block;
                border: 1px solid #ddd;
                float: left;
                padding-left: 15px;
                height: 37.6px;
                line-height: 37.6px;
              }

              a:hover {
                background-color: #409eff;
                color: white;
              }
            }

            .shang-p {
              a {
                position: absolute;
                background: #fff;
                left: 96px;
                top: -5px;
                width: 48px;
                height: 48px;
                font-size: 16px;
                line-height: 45px;
                display: block;
                border: 1px solid #ddd;
                border-radius: 40px;
              }

              .shang-empty {
                position: absolute;
                left: 90px;
                top: 0px;
                width: 62px;
                height: 38px;
                overflow: hidden;

                span {
                  background: #fff;
                  width: 60px;
                  height: 60px;
                  display: block;
                  margin: -10px 0 0 0;
                  border-radius: 60px;
                  border: 1px solid #ddd;
                }
              }

              .shang-s {
                height: 37px;

                a {
                  text-align: center;
                }

                a:hover {
                  background-color: #409eff;
                  color: white;
                }
              }
            }

            .share-sd {

              .share-s {

                a {
                  background: #fff;
                  width: 120px;
                  display: block;
                  border: 1px solid #ddd;
                  float: left;
                  padding-left: 40px;
                  height: 37.6px;
                  line-height: 37.6px;
                  margin-top: -25px;
                }

                a:hover {
                  background-color: #409eff;
                  color: white;
                }
              }

              #share {
                position: absolute;
                top: -60px;
                right: -29px;
                width: 302px;
                height: 68px;
                display: none;
                z-index: 999;
              }
            }
          }
        }
      }

      .article-extra {
        margin-top: 20px;
        padding: 0 10px;

        .article-type {
          height: 50px;
          line-height: 50px;
        }
        .article-tag {
          height: 50px;
          line-height: 50px;
          background-color: white;

          .cursor:first-child {
            margin-left: 10px;
          }
        }
      }

      .divider {
        height: 15px;
        background-color: #f1f1f1;
      }

      .share-notice{
        .authorbio{
          background: #fff;
          padding: 20px;
          border: 1px solid #ddd;
          box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
          border-radius: 2px;

          ol, ul, li {
            list-style: none;
          }

          .avatar {
            float: left;
            width: 40px;
            height: 40px;
            margin: 5px 10px 0 0;
            padding: 0;
          }
        }
      }
    }
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
          color: #409eff;
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

      .searchbar {
        width: 90%;
        margin: 10px auto 0;

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

  .reward-title {
    text-align: center;
  }

  .reward-model {
    .reward-img {
      margin-top: 30px;

      .img-boder {
        border: 9.02px solid rgb(60, 175, 54);
        border-radius: 29.97px;
        width: 250.89px;
        height: 250.89px;
        padding: 24.05px;
        margin: 0 auto;

        img {
          margin: 0 auto;
          width: 185px;
        }
      }

      .reward-tip {
        width: 48.13px;
        position: relative;
        margin: 0 auto;
        font-size: 12px;
        font-weight: 700;
        background: #fff;
        height: 15px;
        line-height: 15px;
        margin-top: -12px;
        color: #3caf36;
        text-align: center;
      }
    }

    .pc{
      display: flex;
      justify-content: space-between
    }

    .phone {

    }
  }

  .comment-all{
    padding: 0px 10px;


    .comment-content{

    }
  }
}

.i-active {
  color: #409eff;
}

.avatar-uploader, .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader, .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.share-dialog {
  text-align: center;
}

.share-icons {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
}

.icon-item {
  text-align: center;
  cursor: pointer;
}

.icon-item img {
  width: 50px;
  height: 50px;
  display: block;
  margin: 0 auto 10px;
}

.icon-item p {
  margin: 0;
  font-size: 14px;
  color: #666;
}
.share-link {
  margin-top: 20px;
}

.el-input {
  width: 100%;
  margin-bottom: 10px;
}
</style>
