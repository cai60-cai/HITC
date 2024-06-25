<template>
  <div class="all-data">
    <el-row :gutter="20" class="data-row">
      <el-col :span="7"><div class="grid-content bg-purple">订单ID</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">订单描述</div></el-col>
      <el-col :span="3"><div class="grid-content bg-purple">订单金额</div></el-col>
      <el-col :span="4"><div class="grid-content bg-purple">订单状态</div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple">下单时间</div></el-col>
    </el-row>
    <el-row :gutter="20" class="data-item" v-if="dataBean" v-for="(item, index) in dataBean.data" :key="item.id">
      <el-col :span="7">
        <div class="grid-content bg-purple cursor item" :title="item.orderTradeNo">
          {{item.orderTradeNo}}
        </div>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purple cursor item">
          {{item.orderDesc}}
        </div>
      </el-col>
      <el-col :span="3">
        <div class="grid-content bg-purple cursor item">
          {{item.orderMoney}}元
        </div>
      </el-col>
      <el-col :span="4">
        <div class="grid-content bg-purplev item">
          <el-tag
              v-if="item.orderStatus === 1"
              size="small"
              effect="dark">
            交易成功
          </el-tag>
          <el-tag
              v-else-if="item.orderStatus === 0"
              type="warning"
              size="small"
              effect="dark">
            待付款
          </el-tag>

          <el-tag
              v-else
              type="danger"
              size="small"
              effect="dark">
            交易失败
          </el-tag>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="grid-content bg-purple item">
          {{ item.orderTime }}
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
import {getUserOrderList} from "@/api/user";

export default {
  name: "Order",
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
      getUserOrderList(this.params).then(res => {
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
  .item{
    word-break:break-all
  }
}
</style>
