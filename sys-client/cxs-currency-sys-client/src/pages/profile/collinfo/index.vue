<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="10"><div class="grid-content bg-purple">文章标题</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">作者</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">文章评分</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">操作</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.id">
      <el-col :span="10">
        <div v-if="item.status && item.status === 1" class="grid-content bg-purple cursor" @click="$router.push({name: 'article-detail', params: {id: item.articleId}})">
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
          {{item.author}}
        </div>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple">
          {{ item.articleRate }} 分
        </div>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple">
          <el-button slot="reference" type="danger" size="small" title="取消收藏" icon="el-icon-delete" circle @click="canalColl(item)"></el-button>
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
import {getArticleCollList, removeArticleColl} from "@/api/user";

  export default {
    name: "CollInfo",
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
        getArticleCollList(this.params).then(res => {
          this.dataBean = res.data
        })
      },
      handleCurrentChange(val){
        this.params.pageNum = val
        this.init()
      },
      canalColl(item) {
        removeArticleColl(item.id).then(res => {
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
