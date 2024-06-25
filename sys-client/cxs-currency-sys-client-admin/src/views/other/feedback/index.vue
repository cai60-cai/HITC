<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :model="listQuery" :inline="true" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="反馈状态">
          <el-select v-model="listQuery.feedbackStatus">
            <el-option label="待处理" :value="0" />
            <el-option label="已处理" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
            搜索
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table v-loading="listLoading" :data="list" style="width: 100%;margin-top:30px;" border>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="反馈id" width="100">
        <template slot-scope="scope">
          {{ scope.row.feedbackId }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="反馈类型" width="200">
        <template slot-scope="scope">
          {{ scope.row.feedbackType }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="反馈内容" width="400">
        <template slot-scope="scope">
          {{ scope.row.feedbackContent }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="反馈状态" width="150">
        <template slot-scope="{row}">
          <el-tag v-if="row.feedbackStatus === 1" type="success" effect="dark">
            已处理
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            待处理
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="反馈时间" width="200">
        <template slot-scope="{row}">
          {{row.feedbackTime}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" fixed="right" width="250">
        <template slot-scope="scope">
          <el-button v-if="scope.row.feedbackStatus === 0" type="primary" size="small" @click="handleEdit(scope)">处理</el-button>
          <el-button type="primary" size="small" @click="handleShow(scope)">查看</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
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

    <el-dialog width="70%" :visible.sync="dialogVisible" title="处理反馈">
      <el-form :model="type" label-width="80px" label-position="left">

        <el-form-item label="反馈类型">
          {{type.feedbackType}}
        </el-form-item>

        <el-form-item label="反馈内容">
          {{type.feedbackContent}}
        </el-form-item>

        <el-form-item label="接收邮件">
          {{type.feedbackEmail}}
        </el-form-item>

        <el-form-item v-if="type.feedbackImages && type.feedbackImages.length > 0" label="反馈图片">
          <el-image style="width: 300px; height: 200px; margin-right: 10px" v-for="item in type.feedbackImages" :key="item" :src="baseUrl + item"/>
        </el-form-item>

        <el-form-item label="邮件反馈" class="postInfo-container-item">
          <el-switch
            style="display: block;margin-left: 0;margin-top: 9px;"
            v-model="type.emailFlag"
            active-color="#13ce66"
            :active-value="1"
            :inactive-value="0"
            active-text="通知"
            inactive-text="不通知"
          >
          </el-switch>
        </el-form-item>

        <el-form-item label="回复内容">
          <el-input v-model="type.replyContent" :rows="5" maxlength="1000" resize="none"
                    show-word-limit type="textarea" class="article-textarea"
                    placeholder="请输入文章摘要，0-1000字"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmType">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog width="70%" :visible.sync="showDialogVisible" title="查看反馈">
      <el-form :model="type" label-width="80px" label-position="left">

        <el-form-item label="反馈类型">
          {{type.feedbackType}}
        </el-form-item>

        <el-form-item label="反馈内容">
          {{type.feedbackContent}}
        </el-form-item>

        <el-form-item label="接收邮件">
          {{type.feedbackEmail}}
        </el-form-item>

        <el-form-item v-if="type.feedbackImages && type.feedbackImages.length > 0" label="反馈图片">
          <el-image style="width: 300px; height: 200px; margin-right: 10px" v-for="item in type.feedbackImages" :key="item" :src="baseUrl + item"/>
        </el-form-item>

        <el-form-item label="反馈内容">
          <el-tag v-if="type.feedbackStatus === 1" type="success" effect="dark">
            已处理
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            待处理
          </el-tag>
        </el-form-item>

        <el-form-item label="回复内容" v-if="type.feedbackStatus === 1">
          {{type.replyContent}}
        </el-form-item>
        <el-form-item label="回复时间" v-if="type.feedbackStatus === 1">
          {{type.replyTime}}
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="primary" @click="showDialogVisible=false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import { removePointRechargeType, saveOrPointRechargeType } from '@/api/point'
import { adminGetFeedBackInfo, getFeedBackList, handleFeedBack, removeFeedBack } from '@/api/feedback'
import { URL_PREFIX } from '@/api/config'
import waves from '@/directive/waves'

const defaultType = {
  id: null,
  gold: 0,
  money: 0.0,
  discount: 10.0,
  shows: true,
  typeDesc: ''
}
const defaultListQuery = {
    pageNum: 1,
    pageSize: 10,
    feedbackStatus: 0
}
export default {
  name: 'Feedback',
  directives: { waves },
  data() {
    return {
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        feedbackStatus: 0
      },
      pageInfo:{},
      baseUrl: URL_PREFIX,
      list: [],
      total: 0,

      pointRechargeTypeList: [],
      listLoading: true,
      dialogVisible: false,
      showDialogVisible: false,
      dialogType: 'new',
      type: Object.assign({}, defaultType),
      qian: 0
    }
  },
  created() {
    this.getList(this.listQuery)
  },
  methods: {
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList(this.listQuery)
    },

    getList(data) {
      this.listLoading = true
      getFeedBackList(data).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.pageInfo = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    handleEdit(scope) {
      this.dialogVisible = true
      this.type = deepClone(scope.row)
    },
    handleShow(scope) {
      this.showDialogVisible = true
      adminGetFeedBackInfo(scope.row.feedbackId).then(res => {
        this.type = res.data
      })
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这条数据吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          removeFeedBack(row.feedbackId).then(res => {
            this.list.splice($index, 1)
            this.$message({
              type: 'success',
              message: '类型删除成功!'
            })
          })
        })
        .catch(err => {
          console.error(err)
        })
    },
    async confirmType() {
      const {
        feedbackId,
        replyContent,
        emailFlag
      } = this.type
      const param = {
        feedbackId,
        replyContent,
        emailFlag
      }
      handleFeedBack(param).then(res => {
        this.$message.success(res.msg)
        this.dialogVisible = false
        this.type = {}
        this.getList(this.listQuery)
      })
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
