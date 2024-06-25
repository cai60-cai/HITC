<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="10"><div class="grid-content bg-purple">文章标题</div></el-col>
      <el-col :span="5"><div class="grid-content bg-purple">文章状态</div></el-col>
      <el-col :span="5"><div class="grid-content bg-purple">文章评分</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">操作</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.id">
      <el-col :span="10">
        <div v-if="item.articleStatus === 1" class="grid-content bg-purple cursor" @click="$router.push({name: 'article-detail', params: {id: item.articleId}})">
          <el-link :underline="false">
            {{item.articleTitle}}
            <el-tag
                v-if="item.articleIsSelf === 1"
                type=""
                size="mini"
                style="margin-left: 5px;"
                effect="plain">
              原创
            </el-tag>
          </el-link>
        </div>
        <div v-else class="grid-content bg-purple cursor">
          <el-link :underline="false">
            {{item.articleTitle}}
            <el-tag
                v-if="item.articleIsSelf === 1"
                type=""
                size="mini"
                style="margin-left: 5px;"
                effect="plain">
              原创
            </el-tag>
          </el-link>
        </div>
      </el-col>
      <el-col :span="5">
        <div class="grid-content bg-purple">
          <el-tag
              v-if="item.articleStatus === 1"
              size="small"
              effect="dark">
            已通过
          </el-tag>
          <el-tag
              v-else-if="item.articleStatus === 0"
              type="warning"
              size="small"
              effect="dark">
            待审核
          </el-tag>
          <el-tag
              v-else
              :title="item.articleDesc"
              size="small"
              type="danger"
              effect="dark">
            审核失败
          </el-tag>
        </div>
      </el-col>
      <el-col :span="5">
        <div class="grid-content bg-purple">
          {{ item.articleRate }} 分
        </div>
      </el-col>
      <el-col :span="4">
        <div v-if="item.articleStatus === 1" class="grid-content bg-purple">
          <el-button slot="reference" type="primary" size="small" title="详情" icon="el-icon-tickets" circle @click="detailHandle(item)"></el-button>
        </div>
        <div v-else-if="item.articleStatus === 0 || item.articleStatus === 2" class="grid-content bg-purple">
          <el-button slot="reference" type="primary" size="small" title="编辑" icon="el-icon-edit" circle @click="editHandle(item)"></el-button>
          <el-button slot="reference" type="danger" size="small" title="删除" icon="el-icon-delete" circle @click="delHandle(item)"></el-button>
        </div>
        <div v-else class="grid-content bg-purple">
          <el-button slot="reference" type="danger" size="small" title="删除" icon="el-icon-delete" circle @click="delHandle(item)"></el-button>
        </div>
      </el-col>
    </el-row>
    <el-pagination
        class="page"
        layout="prev, pager, next"
        @current-change="handleCurrentChange"
        :page-size="dataBean.pageSize"
        :total="dataBean.total">
    </el-pagination>
  </div>
</template>

<script>
import {getArticlePublishList, removeMyArticle} from "@/api/user";
import {cancelCollectionArticle} from "@/api/article";

export default {
  name: "PushInfo",
  data(){
    return {
      dataBean: {},
      params:{
        pageNum:1,
        pageSize:10
      }
    }
  },
  mounted() {
    this.init()
  },
  methods:{
    init(){
      getArticlePublishList(this.params).then(res => {
        this.dataBean = res.data
      })
    },
    handleCurrentChange(val){
      this.params.pageNum = val
      this.init()
    },
    detailHandle(item) {
      this.$router.push({name: 'article-detail', params: {id: item.articleId}})
    },
    editHandle(item) {
      this.$router.push({name: 'article-update', params: {id: item.articleId}})
    },
    delHandle(item) {
      this.$confirm('确定删除这条博客吗?', '删除博客', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        removeMyArticle(item.articleId).then(res => {
          this.$message.success('文章删除成功')
          this.init()
        })
      }).catch(err => {
        console.error(err)
      })

    }
  }
}
</script>

<style scoped lang="less">
.all-data{
  .data-row{
    border-bottom: 1px solid #cccccc;
    font-weight: 600;
    height: 40px;
  }
  .data-item{
    border-bottom: 1px solid #cccccc;
    min-height: 40px;
    display: flex;
    align-items: center;
  }
  .page{
    text-align: right;
    margin-top: 10px;
  }
}
</style>
