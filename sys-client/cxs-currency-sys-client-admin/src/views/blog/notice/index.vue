<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddNotice">创建新公告</el-button>

    <el-table v-loading="listLoading" :data="noticePage.data" style="width: 100%;margin-top:30px;" border>
      <el-table-column :resizable="false" align="center" label="序号" width="80">
        <template slot-scope="{scope, $index}">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="公告标题" width="350">
        <template slot-scope="scope">
          {{ scope.row.noticeTitle }}
        </template>
      </el-table-column>
      <el-table-column align="center" :resizable="false" label="创建人" width="250">
        <template slot-scope="scope">
          {{ scope.row.user.nickName }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="是否推送" width="100">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.isPush === 1"
            active-color="#13ce66"
            inactive-color="#ccc"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" :resizable="false" label="创建时间" width="250">
        <template slot-scope="scope">
          {{ scope.row.publishTime }}
        </template>
      </el-table-column>

      <el-table-column align="center" :resizable="false" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleDetail(scope)">详情</el-button>
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="detailVisible" :title="currentNotice.noticeTitle" width="60%">
      <div>
        <v-md-editor :value="currentNotice.noticeContent" mode="preview"></v-md-editor>
      </div>
    </el-dialog>

    <!--    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑任务':'新增任务'">-->
    <!--      <el-form :model="task" label-width="80px" label-position="left">-->
    <!--        <el-form-item label="任务标识">-->
    <!--          <el-input v-model="task.taskKey" placeholder="任务标识" />-->
    <!--        </el-form-item>-->
    <!--        <el-form-item label="任务名称">-->
    <!--          <el-input v-model="task.taskName" placeholder="任务名称" />-->
    <!--        </el-form-item>-->

    <!--        <el-form-item label="任务描述">-->
    <!--          <el-input v-model="task.taskDesc" placeholder="任务描述" />-->
    <!--        </el-form-item>-->

    <!--        <el-form-item label="执行周期">-->
    <!--          <el-input v-model="task.taskCron" placeholder="执行周期" />-->
    <!--        </el-form-item>-->

    <!--        <el-form-item label="任务状态">-->
    <!--          <el-select v-model="task.taskStatus" filterable placeholder="请选择任务状态" @change="taskTypeChange($event)">-->
    <!--            <el-option-->
    <!--              label="开启"-->
    <!--              :value="1"-->
    <!--            />-->
    <!--            <el-option-->
    <!--              label="禁用"-->
    <!--              :value="0"-->
    <!--            />-->
    <!--          </el-select>-->
    <!--        </el-form-item>-->
    <!--      </el-form>-->
    <!--      <div style="text-align:right;">-->
    <!--        <el-button type="danger" @click="dialogVisible=false">取消</el-button>-->
    <!--        <el-button type="primary" @click="confirmTask">确认</el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->
  </div>
</template>

<script>
import { deleteNotice, getNoticeList } from '@/api/notice'
import { deepClone } from '@/utils'
import { removeTask, saveOrUpdateTask, taskOperaHandle } from '@/api/task'

export default {
  name: 'Notice',
  data() {
    return {
      listLoading: false,
      noticePage: {},
      noticeList: [],
      pageQuery: {
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        isPush: null
      },
      currentNotice: {},
      detailVisible: false,
      detailContent: ''
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getNoticeList(this.pageQuery).then(res => {
        this.listLoading = false
        this.noticePage = res.data
      })
    },
    handleAddNotice() {
      this.$router.push({ name: 'notice-add-etit' })
    },
    handleEdit(scope) {
      this.$router.push({ name: 'notice-add-etit', params: { id: scope.row.id }})
    },
    handleDetail(scope) {
      this.currentNotice = deepClone(scope.row)
      this.detailVisible = true
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这个公告吗?', '删除公告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await deleteNotice(row.id)
          this.$message({
            type: 'success',
            message: '公告删除成功!'
          })
          this.getList()
        })
        .catch(err => {
          console.error(err)
        })
    },
    operaHandle(row, val) {
      const data = {
        taskId: row.row.taskId,
        operaType: val
      }
      taskOperaHandle(data).then((res) => {
        this.$message({
          type: 'success',
          message: res.msg
        })
        this.getList()
      })
    },
    async confirmTask() {
      const isEdit = this.dialogType === 'edit'
      if (isEdit) {
        await saveOrUpdateTask(this.task)
      } else {
        this.task.taskId = undefined
        const { data } = await saveOrUpdateTask(this.task)
        this.task.taskId = data.taskId
        this.taskList.push(this.task)
      }

      const { taskDesc, taskId, taskName } = this.task
      this.dialogVisible = false
      this.$notify({
        title: 'Success',
        dangerouslyUseHTMLString: true,
        message: `
            <div>task Id: ${taskId}</div>
            <div>task Name: ${taskName}</div>
            <div>Description: ${taskDesc}</div>
          `,
        type: 'success'
      })
      this.getList()
    }
  }
}
</script>

<style scoped>

</style>
