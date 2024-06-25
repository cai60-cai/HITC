<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyWord"
        placeholder="请搜索"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-circle-plus-outline" @click="createFormVisible = true">
        创建新用户
      </el-button>
      <el-button
        v-waves
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload"
      >
        导出
      </el-button>
    </div>

    <el-table
      v-loading="listLoading"
      row-key="userId"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column :resizable="false" label="编号" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row, $index}">
          <span>{{ $index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="用户名" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="昵称" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.nickName }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="用户头像" align="center" min-width="80px">
        <template slot-scope="{row}">
          <el-avatar v-if="row.avatar" :src="baseUrl + row.avatar" />
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="联系方式" align="center" min-width="200px">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="邮箱" align="center" min-width="200px">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="用户创建时间" align="center" min-width="200px">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="状态" align="center" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag v-if="row.userStatus === 1" type="success" effect="dark">
            正常
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            禁用
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="400px" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="promHandler(row)">
            权限
          </el-button>
          <el-button type="primary" size="mini" @click="authHandler(row)">
            授权
          </el-button>
          <el-button v-if="row.userStatus === 1" type="warning" size="mini" @click="userStatusHandle(row, 2)">
            禁用
          </el-button>
          <el-button v-else type="primary" size="mini" @click="userStatusHandle(row, 1)">
            启用
          </el-button>
          <el-button size="mini" type="danger" plain @click="resetHandle(row)">
            重置
          </el-button>
          <el-button size="mini" type="danger" @click="deleteHandle(row)">
            删除
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
    <!--        授权-->
    <el-dialog
      :title="'给用户 <' + currentUser.userName + '> 授权'"
      :visible="authVisible"
      :before-close="authHandleCancel"
    >
      <el-form ref="form">
        <el-form-item label="角色">
          <el-select v-model="hasRoleIds" multiple placeholder="请选择">
            <el-option
              v-for="item in roleList"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="authHandleCancel">取 消</el-button>
        <el-button size="mini" type="primary" @click="authHandleOk">确 定</el-button>
      </span>
    </el-dialog>
    <!--        权限-->
    <el-dialog
      :title="'管理用户 <' + currentUser.userName + '> 的系统权限'"
      :visible="promVisible"
      :before-close="promHandleCancel"
    >
      <el-form ref="form" style="padding: 5px 10px">
        <el-form-item label="文件上传权限">
          <el-switch
            v-model="userAuth.uploadAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="发言评论权限">
          <el-switch
            v-model="userAuth.commentAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="打赏功能权限">
          <el-switch
            v-model="userAuth.rewardAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="文章发布权限">
          <el-switch
            v-model="userAuth.pushArticleAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="申请友链权限">
          <el-switch
            v-model="userAuth.applyExternalAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="用户反馈权限">
          <el-switch
            v-model="userAuth.feedbackAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="用户举报权限">
          <el-switch
            v-model="userAuth.reportAuth"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="允许"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="promHandleCancel">取 消</el-button>
        <el-button size="mini" type="primary" @click="promHandleOk">确 定</el-button>
      </span>
    </el-dialog>
    <!--    创建新用户-->
    <el-dialog title="创建新用户" :visible.sync="createFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="tempUser"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="tempUser.userName" />
        </el-form-item>

        <el-form-item label="分配角色">
          <el-select v-model="tempUser.roleIds" multiple placeholder="请选择">
            <el-option
              v-for="item in roleList"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="createUser()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import {
  authRoleToUser,
  createUser,
  deleteUser, getUserAuth,
  getUserHasRoleId,
  getUserList,
  resetUserPwd,
  updateStatus, updateUserAuth
} from '@/api/user'
import { getRoleList } from '@/api/role'
import { URL_PREFIX } from '@/api/config'

export default {
  name: 'User',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      baseUrl: URL_PREFIX,
      list: [],
      total: 0,
      pageInfo: {},
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        keyWord: ''
      },
      downloadLoading: false,
      hasRoleIds: [],
      currentUser: {},
      authVisible: false,
      promVisible: false,
      roleList: [],
      createFormVisible: false,
      tempUser: {
        userName: '',
        roleIds: []
      },
      rules: {
        userName: [{ required: true, message: '用户名为必填项', trigger: 'change' }]
      },
      userAuth: {}
    }
  },
  created() {
    this.getList()
    this.getRoleList()
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
    getUserAuthInfo(userId) {
      getUserAuth(userId).then(res => {
        this.userAuth = res.data
      })
    },
    getRoleList() {
      getRoleList().then(res => {
        this.roleList = res.data
      })
    },
    getList(data) {
      this.listLoading = true
      getUserList(data).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.pageInfo = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    handleChange(value) {
      this.hasRoleIds = value
    },
    async authHandler(record) {
      this.authVisible = true
      this.currentUser = record
      // 请求数据
      const { data } = await getUserHasRoleId(record.userId)
      this.hasRoleIds = data.map(d => d.roleId)
    },
    promHandler(record) {
      this.promVisible = true
      this.currentUser = record
      // 请求数据
      this.getUserAuthInfo(record.userId)
    },
    // 确认授权
    authHandleOk() {
      const userId = this.currentUser.userId
      const userRoleObj = {
        userId,
        roleIds: this.hasRoleIds
      }
      authRoleToUser(userRoleObj).then(res => {
        this.$message.success(res.msg)
        this.getList(this.listQuery)
      })
      setTimeout(() => {
        this.authVisible = false
        this.currentRole = {}
      }, 1000)
    },
    // 确认用户系统权限
    promHandleOk() {
      const userId = this.currentUser.userId
      const userAuthObj = {
        userId,
        ...this.userAuth
      }
      updateUserAuth(userAuthObj).then(res => {
        this.$message.success(res.msg)
        this.getList(this.listQuery)
      })
      setTimeout(() => {
        this.promVisible = false
        this.currentRole = {}
      }, 1000)
    },

    promHandleCancel(e) {
      this.promVisible = false
    },
    authHandleCancel(e) {
      this.authVisible = false
    },

    userStatusHandle(row, code) {
      updateStatus({ userId: row.userId, status: code }).then(res => {
        this.$message.success(res.msg)
        this.getList(this.listQuery)
      })
    },
    resetHandle(row) {
      this.$confirm('确定要重置这个用户密码吗?', '重置密码', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        resetUserPwd(row.userId).then(() => {
          this.$message.success('用户密码重置成功')
        })
      }).catch(err => {
        console.error(err)
      })
    },
    deleteHandle(row) {
      this.$confirm('确定删除这个用户吗?', '删除用户', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        deleteUser(row.userId).then(() => {
          this.$message.success('用户删除成功')
          this.getList(this.listQuery)
        })
      }).catch(err => {
        console.error(err)
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList(this.listQuery)
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.tempUser = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['userId', 'userName', 'realName', 'phone', 'email', 'create_time', 'update_time']
        const filterVal = ['userId', 'userName', 'realName', 'phone', 'email', 'create_time', 'update_time']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        return v[j]
      }))
    },
    createUser() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createUser(this.tempUser).then(() => {
            this.$message.success('用户创建成功')
            this.createFormVisible = false
            this.tempUser = {}
            this.getList(this.listQuery)
          })
        }
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
