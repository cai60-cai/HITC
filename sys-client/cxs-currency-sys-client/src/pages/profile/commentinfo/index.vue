<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="8"><div class="grid-content bg-purple">评论内容</div></el-col>
      <el-col :span="8"><div class="grid-content bg-purple">评论对象</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">评论时间</div></el-col>
      <el-col :span="2"><div class="grid-content bg-purple">操作</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.id">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          {{item.commentContent}}
        </div>
      </el-col>
      <el-col :span="8">
        <div v-if="item.article && item.article.articleTitle" class="grid-content bg-purple cursor" @click="$router.push({name: 'article-detail', params: {id: item.article.articleId}})">
          <el-link :underline="false">
            {{item.article.articleTitle}}
          </el-link>
        </div>
        <div v-else class="grid-content bg-purple">
          <el-tag
              type="danger"
              size="mini"
              style="margin-left: 5px;">
            原文章已删除
          </el-tag>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple">
          {{ item.commentTime }}
        </div>
      </el-col>
      <el-col :span="2">
        <div class="grid-content bg-purple">
          <el-button slot="reference" type="danger" size="small" title="删除评论" icon="el-icon-delete" circle @click="delComment(item)"></el-button>
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
import {getMyCommentList} from "@/api/user";
import {deleteComment} from "@/api/comment";

export default {
  name: "CommentInfo",
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
      getMyCommentList(this.params).then(res => {
        this.dataBean = res.data
      })
    },
    handleCurrentChange(val){
      this.params.pageNum = val
      this.init()
    },
    delComment(item) {
      deleteComment(item.commentId).then(res => {
        this.$message.success(res.msg)
        this.init()
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
