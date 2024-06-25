<template>
  <div class="app-container">

    <el-form :inline="true" :model="queryPage" class="demo-form-inline">
      <el-form-item label="关键字">
        <el-input v-model="queryPage.keyword" placeholder="请输入关键字"></el-input>
      </el-form-item>
      <el-form-item label="操作状态">
        <el-select v-model="queryPage.operaResult" placeholder="请选择">
          <el-option label="操作成功" :value="1"/>
          <el-option label="操作失败" :value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker
          v-model="queryPage.timeRange"
          type="datetimerange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="listLoading" :data="pageInfo.data" style="width: 100%;margin-top:30px;" border>
      <el-table-column :show-overflow-tooltip="true" :resizable="false" align="center" label="序号" width="80">
        <template slot-scope="{scope, $index}">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" :resizable="false" align="center" label="方法名" width="200">
        <template slot-scope="scope">
          {{ scope.row.operaMethod }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" :resizable="false" align="center" label="描述" width="200">
        <template slot-scope="scope">
          {{ scope.row.operaDesc }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" :resizable="false" align="center" label="入参" width="200">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <json-viewer
              v-if="scope.row.param"
              style="max-width: 500px"
              :value="scope.row.param"
              :expand-depth="5"
              boxed
              sort
              copyable
            ></json-viewer>
            <div slot="reference" class="name-wrapper">
              {{ scope.row.param }}
            </div>
          </el-popover>
        </template>

      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="出参" width="200">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <json-viewer
              v-if="scope.row.response"
              style="max-width: 500px"
              :value="scope.row.response"
              :expand-depth="5"
              boxed
              sort
              copyable
            ></json-viewer>
            <div slot="reference" class="name-wrapper">
              {{ scope.row.response }}
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="操作人" width="200">
        <template slot-scope="scope">
          {{ scope.row.operaUser.nickName }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="操作状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.operaResult === 1" type="success" effect="dark">
            操作成功
          </el-tag>
          <el-tag v-else type="danger">
            操作失败
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" :resizable="false" label="操作时间" width="200">
        <template slot-scope="scope">
          {{ scope.row.operaTime }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      style="text-align: end"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageInfo.pageNum"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="pageInfo.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pageInfo.total">
    </el-pagination>

  </div>
</template>

<script>
import { getLogList } from '@/api/log'
import JsonViewer from 'vue-json-viewer'

export default {
  name: 'Log',
  components: {
    JsonViewer
  },
  data() {
    return {
      pageInfo: {},
      queryPage: {
        pageNum: 1,
        pageSize: 10,
        timeRange: null,
        operaResult: 1,
        keyword: ''
      },
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  watch: {
    queryPage: {
      deep: true,
      immediate: true,
      handler: function(val, oldVal) {
        this.getList(val)
      }
    }
  },
  created() {
    // this.getList()
  },
  methods: {
    getList(data) {
      this.listLoading = true
      getLogList(data).then(res => {
        this.listLoading = false
        this.pageInfo = res.data
      })
    },
    handleSizeChange(val) {
      this.queryPage.pageSize = val
    },
    handleCurrentChange(val) {
      this.queryPage.pageNum = val
    },
    onSubmit() {
      this.getList(this.queryPage)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }

  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
