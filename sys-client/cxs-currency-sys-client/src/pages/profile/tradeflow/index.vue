<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="4"><div class="grid-content bg-purple">ID</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">积分变动值</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">交易描述</div></el-col>
      <el-col :span="8"><div class="grid-content bg-purple">交易时间</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.id">
      <el-col :span="4"><div class="grid-content bg-purple">{{item.id}}</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">
        <el-tag
            v-if="item.pointType === 1"
            type=""
            size="mini"
            style="margin-left: 5px;"
            effect="plain">
          +{{item.point}}
        </el-tag>
        <el-tag
            v-else
            type="danger"
            size="mini"
            style="margin-left: 5px;"
            effect="plain">
          -{{item.point}}
        </el-tag>
      </div></el-col>
      <el-col :span="6" v-if="item.articleId">
        <div class="grid-content bg-purple" @click="$router.push({name: 'article-detail', params: {id: item.articleId}})">
        <el-link :underline="false" title="点击可查看文章详情">{{item.tradeDesc}}</el-link>
      </div>
      </el-col>
      <el-col :span="6" v-else>
        <div class="grid-content bg-purple">
          <el-link :underline="false" :title="item.tradeDesc">{{item.tradeDesc}}</el-link>
        </div>
      </el-col>
      <el-col :span="8"><div class="grid-content bg-purple">{{item.tradeTime}}</div></el-col>
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
import {getPointTradeFlow} from "@/api/user";

export default {
  name: "TradeFlow",
  data(){
    return {
      dataBean:{},
      params:{
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  mounted() {
    this.init()
  },
  methods:{
    init(){
      getPointTradeFlow(this.params).then(res => {
        this.dataBean = res.data
      })
    },
    handleCurrentChange(val){
      this.params.pageNum = val
      this.init()
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
