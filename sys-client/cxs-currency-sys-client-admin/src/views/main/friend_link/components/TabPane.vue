<template>
  <div>
    <el-table :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column
        v-loading="loading"
        align="center"
        label="ID"
        width="65"
        element-loading-text="请给我点时间！"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.externalLinkId }}</span>
        </template>
      </el-table-column>

      <el-table-column width="180px" :show-overflow-tooltip="true" align="center" label="链接名称">
        <template slot-scope="scope">
          <span>{{ scope.row.externalLinkName }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" :show-overflow-tooltip="true" align="center" label="链接图标">
        <template slot-scope="scope">
          <span>
            <span>{{ scope.row.externalLinkIcon }}</span>
          </span>
        </template>
      </el-table-column>

      <el-table-column width="180px" :show-overflow-tooltip="true" align="center" label="链接地址">
        <template slot-scope="scope">
          <span>{{ scope.row.externalLinkLink }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" label="打开方式">
        <template slot-scope="scope">
          <span>{{ scope.row.externalLinkBlank }}</span>
        </template>
      </el-table-column>

      <el-table-column width="150px" :show-overflow-tooltip="true" align="center" label="链接描述">
        <template slot-scope="scope">
          <span>{{ scope.row.externalLinkDesc }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" align="center" label="所属用户">
        <template slot-scope="scope">
          <el-popover
            placement="top-start"
            :title="scope.row.user.userName"
            width="200"
            trigger="hover"
            :content="scope.row.user.autograph"
          >
            <span slot="reference">
              <el-avatar class="cursor" :src="baseUrl + scope.row.user.avatar" />
            </span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="状态" width="120">
        <template slot-scope="{row}">
          <el-tag v-if="row.externalLinkStatus === 1" type="success" effect="dark">
            已通过
          </el-tag>
          <el-tag v-else-if="row.externalLinkStatus === 0" type="warning" effect="dark">
            待审核
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            已拒绝
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="210" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-if="row.externalLinkStatus === 1" type="primary" size="mini" @click="updateHandle(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.externalLinkStatus === 1 || row.externalLinkStatus === 2"
            size="mini"
            type="danger"
            @click="deleteHandle(row)"
          >
            删除
          </el-button>
          <el-button v-if="row.externalLinkStatus === 0" type="warning" size="mini" @click="approveHandle(row)">
            审核
          </el-button>
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

    <el-dialog title="修改" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="链接名称" prop="externalLinkName">
          <el-input v-model="temp.externalLinkName" />
        </el-form-item>
        <el-form-item label="链接地址" prop="externalLinkLink">
          <el-input v-model="temp.externalLinkLink" />
        </el-form-item>
        <el-form-item label="链接描述">
          <el-input v-model="temp.externalLinkDesc" />
        </el-form-item>
        <el-form-item label="链接图标">
          <el-input v-model="temp.externalLinkIcon" />
        </el-form-item>
        <el-form-item label="链接状态">
          <el-select v-model="temp.externalLinkStatus" placeholder="请选择" @change="menuStatusChange($event)">
            <el-option
              label="待审核"
              :value="0"
            />
            <el-option
              label="已通过"
              :value="1"
            />
            <el-option
              label="已拒绝"
              :value="2"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="打开方式">
          <el-select v-model="temp.externalLinkBlank" placeholder="请选择" @change="menuStatusChange($event)">
            <el-option
              label="空白页面"
              value="_blank"
            />
            <el-option
              label="父页面"
              value="_parent"
            />
            <el-option
              label="当前页面"
              value="_self"
            />
            <el-option
              label="顶层页面"
              value="_top"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog title="审核" :visible.sync="approveFormVisible">
      <el-form
        ref="dataForm"
        :model="tempApproveForm"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="链接状态">
          <el-select v-model="tempApproveForm.externalLinkStatus" placeholder="请选择" @change="menuStatusChange($event)">
            <el-option
              label="通过"
              :value="1"
            />
            <el-option
              label="拒绝"
              :value="2"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="链接描述">
          <el-input
            v-model="tempApproveForm.externalLinkDesc"
            type="textarea"
            :rows="2"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approveFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="approveLink()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {
  approveExternalFriendLink,
  deleteExternalFriendLink,
  getExternalFriendLinkList,
  saveOrUpdateExternalFriendLink
} from '@/api/friend_link'
import Pagination from '@/components/Pagination'
import { URL_PREFIX } from '@/api/config'
export default {
  components: { Pagination },
  props: {
    type: {
      type: String,
      default: '0'
    }
  },
  data() {
    return {
      baseUrl: URL_PREFIX,
      list: [],
      total: 0,
      pageInfo: {},
      listQuery: {
        pageNum: 1,
        pageSize: 5,
        type: Number.parseInt(this.type)
      },
      loading: false,
      dialogFormVisible: false,
      dialogStatus: '',
      temp: {
        externalLinkBlank: '',
        externalLinkDesc: '',
        externalLinkIcon: '',
        externalLinkId: undefined,
        externalLinkLink: '',
        externalLinkName: '',
        externalLinkStatus: 1
      },
      tempApproveForm: {
        externalLinkId: undefined,
        externalLinkStatus: 1
      },
      rules: {
        externalLinkName: [{ required: true, message: '链接名称为必填项', trigger: 'change' }],
        externalLinkLink: [{ required: true, message: '链接路径为必传项', trigger: 'blur' }]
      },
      approveFormVisible: false
    }
  },
  created() {
    this.getList()
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
  methods: {
    getList() {
      this.loading = true
      this.$emit('create') // for test
      getExternalFriendLinkList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.pageInfo = response.data
        this.loading = false
      })
    },
    // 审核
    approveHandle(row) {
      this.tempApproveForm.externalLinkId = row.externalLinkId
      this.approveFormVisible = true
    },
    // 删除
    deleteHandle(row) {
      this.$confirm('确定删除这个链接吗?', '删除链接', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        deleteExternalFriendLink(row.externalLinkId).then(() => {
          this.$message.success('链接删除成功')
          this.getList()
        })
      }).catch(err => {
        console.error(err)
      })
    },
    // 创建 or 修改
    updateHandle(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    menuStatusChange(event) {
      this.$forceUpdate()
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          saveOrUpdateExternalFriendLink(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: 'OK',
              message: '链接修改成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    // 审核链接
    approveLink() {
      approveExternalFriendLink(this.tempApproveForm).then((res) => {
        this.$message.success(res.msg)
        this.approveFormVisible = false
        this.tempApproveForm = {}
        this.getList()
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

