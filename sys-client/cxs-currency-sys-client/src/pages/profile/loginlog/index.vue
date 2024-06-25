<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="6"><div class="grid-content bg-purple">日志ID</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">登录系统</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">登录地址</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">登录时间</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.logId">
      <el-col :span="6"><div class="grid-content bg-purple">{{item.logId}}</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">{{item.loginMode === 1 ? '前台系统' : '后台管理系统'}}</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">{{item.loginAddress ? item.loginAddress : item.loginIp}}</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">{{item.loginTime}}</div></el-col>
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
  import {getLoginLog} from "@/api/user";

  export default {
    name: "LoginLog",
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
        getLoginLog(this.params).then(res => {
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
