<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="4"><div class="grid-content bg-purple">举报类型</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">举报内容类型</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">举报状态</div></el-col>
      <el-col :span="5"><div class="grid-content bg-purple">举报时间</div></el-col>
      <el-col :span="7"><div class="grid-content bg-purple">操作</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.id">
      <el-col :span="4">
        <div class="grid-content bg-purple cursor">
          {{item.reportType}}
        </div>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple cursor">
          {{item.reportTarget === 1 ? '博客资源' : '评论'}}
        </div>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple">
          <el-tag
              v-if="item.reportStatus === 1"
              size="small"
              effect="dark">
            已处理
          </el-tag>
          <el-tag
              v-else
              type="warning"
              size="small"
              effect="dark">
            待处理
          </el-tag>
        </div>
      </el-col>
      <el-col :span="5">
        <div class="grid-content bg-purple">
          {{ item.reportTime }}
        </div>
      </el-col>
      <el-col :span="7">
        <div class="grid-content bg-purple">
          <el-button slot="reference" type="primary" size="small" title="查看" icon="el-icon-more" circle @click="info(item)"></el-button>
          <el-button slot="reference" type="danger" size="small" title="删除" icon="el-icon-delete" circle @click="del(item)"></el-button>
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
import {getArticlePublishList, getReportList} from "@/api/user";
import {cancelCollectionArticle} from "@/api/article";
import {delReport} from "@/api/report";

export default {
  name: "Report",
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
      getReportList(this.params).then(res => {
        this.dataBean = res.data
      })
    },
    handleCurrentChange(val){
      this.params.pageNum = val
      this.init()
    },
    info(item) {
      this.$router.push({name: "reportInfo", params: {id: item.reportId}})
    },
    del(item) {
      delReport(item.reportId).then(res => {
        this.init()
      })
    },
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
