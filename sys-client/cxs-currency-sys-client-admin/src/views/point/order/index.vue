<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form ref="ruleForm" :model="listQuery" :inline="true" label-width="100px" class="demo-ruleForm">
        <el-form-item label="下单用户">
          <el-select
            filterable
            reserve-keyword
            v-model="listQuery.orderUser"
            placeholder="请选择">
            <el-option
              label="请选择"
              :value="null"
            />
            <el-option
              v-for="item in userList"
              :key="item.userId"
              :label="`${item.userName}(${item.nickName ? item.nickName : ''})`"
              :value="item.userId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="订单状态">
          <el-select v-model="listQuery.orderStstus" placeholder="请选择">
            <el-option
              label="请选择"
              :value="null"
            />
            <el-option
              label="待付款"
              :value="0"
            />
            <el-option
              label="交易成功"
              :value="1"
            />
            <el-option
              label="交易超时"
              :value="2"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
            搜索
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table
      v-loading="listLoading"
      row-key="orderId"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="订单ID" align="center" width="450px">
        <template slot-scope="{row}">
          <span>{{ row.orderTradeNo }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="订单描述" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.pointTypeDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="交易金额" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.orderMoney }}元</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="下单用户" width="200px" align="center">
        <template slot-scope="{row}">
          <span v-if="row.orderUser">{{ row.orderUser.userName }}({{row.orderUser.nickName}})</span>
        </template>
      </el-table-column>

      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="订单状态" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag
            v-if="row.orderStatus === 1"
            type="success"
            size="mini"
            style="margin-left: 5px;"
          >
            交易成功
          </el-tag>
          <el-tag
            v-else-if="row.orderStatus === 0"
            type="info"
            style="margin-left: 5px;"
            size="mini"
          >
            待付款
          </el-tag>
          <el-tag
            v-else
            type="danger"
            style="margin-left: 5px;"
            size="mini"
          >
            交易超时
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="交易时间" align="center" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.orderTime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="text-align: end"
      :current-page="pageInfo.pageNum"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="pageInfo.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pageInfo.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import { URL_PREFIX } from '@/api/config'
import { getPointTradeOrderList } from '@/api/point'
import { adminGetSimpleUserList } from '@/api/user'

export default {
  name: 'TradeOrder',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      baseUrl: URL_PREFIX,
      userQuery: {
        keyword: ''
      },
      userList: [],
      list: [],
      typeTreeList: [],
      total: 0,
      pageInfo: {},
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        orderUser: '',
        orderStstus: null
      }
    }
  },
  watch: {
    listQuery: {
      deep: true,
      immediate: true,
      handler: function(val, oldVal) {
        this.getList(val)
      }
    }
  },
  mounted() {
    this.getList(this.listQuery)
    this.getUserList(this.userQuery)
  },
  methods: {
    getList(data) {
      this.listLoading = true
      getPointTradeOrderList(data).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.pageInfo = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    getUserList(params) {
      adminGetSimpleUserList(params).then(response => {
        this.userList = response.data
      })
    },
    // 搜索按钮触发
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList(this.listQuery)
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
    }
  }
}
</script>

<style lang="scss">
.content {
  padding: 0 40px;

  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }

  .lable-tip {
    width: 80px;
    display: inline-block;
    line-height: 36px;
    font-size: 14px;
    color: #606266;
    padding: 0 12px 0 0;
    box-sizing: border-box;
    font-weight: 700;
    text-align: right;
  }

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
}

.article-textarea ::v-deep {

}
</style>
