<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddTask">创建新任务</el-button>

    <el-table v-loading="listLoading" :data="taskList" style="width: 100%;margin-top:30px;" border>
      <el-table-column :resizable="false" align="center" label="序号" width="80">
        <template slot-scope="{scope, $index}">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="任务标识" width="200">
        <template slot-scope="scope">
          {{ scope.row.taskKey }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="任务名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.taskName }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="任务描述" width="200">
        <template slot-scope="scope">
          {{ scope.row.taskDesc }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="执行周期" width="200">
        <template slot-scope="scope">
          {{ scope.row.taskCron }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="任务状态" width="120">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.taskStatus === 1"
            active-color="#13ce66"
            inactive-color="#ccc"
          />
        </template>
      </el-table-column>
      <el-table-column :resizable="false" align="center" label="运行状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.runStatus" type="success" effect="dark">
            运行中
          </el-tag>
          <el-tag v-else type="info">
            已停止
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" :resizable="false" label="创建时间" width="200">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>

      <el-table-column align="center" :resizable="false" min-width="500" label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="primary" :disabled="scope.row.runStatus" size="small" @click="operaHandle(scope, 1)">启动</el-button>
          <el-button type="primary" :disabled="!scope.row.runStatus" size="small" @click="operaHandle(scope, 3)">重启</el-button>
          <el-button type="primary" :disabled="!scope.row.runStatus" size="small" @click="operaHandle(scope, 2)">停止</el-button>
          <el-button type="primary" :disabled="scope.row.taskStatus !== 1" size="small" @click="execute(scope)">执行一次</el-button>
          <el-button type="primary" size="small" @click="showLog(scope)">日志</el-button>
          <el-button type="danger" :disabled="scope.row.runStatus" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑任务':'新增任务'">
      <el-form :model="task" label-width="80px" label-position="left">
        <el-form-item label="任务标识">
          <el-input v-model="task.taskKey" placeholder="任务标识" />
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input v-model="task.taskName" placeholder="任务名称" />
        </el-form-item>

        <el-form-item label="任务描述">
          <el-input v-model="task.taskDesc" placeholder="任务描述" />
        </el-form-item>

        <el-form-item label="执行周期">
          <el-input v-model="task.taskCron" placeholder="执行周期" />
        </el-form-item>

        <el-form-item label="任务状态">
          <el-select v-model="task.taskStatus" filterable placeholder="请选择任务状态" @change="taskTypeChange($event)">
            <el-option
              label="开启"
              :value="1"
            />
            <el-option
              label="禁用"
              :value="0"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmTask">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="logDialogVisible" width="70%" :title="currentTask.taskName">
      <el-form :inline="true" :model="queryPage" class="demo-form-inline">
        <el-form-item label="执行状态">
          <el-select v-model="queryPage.executeStatus" placeholder="请选择">
            <el-option label="请选择" :value="null" />
            <el-option label="执行成功" :value="true" />
            <el-option label="执行失败" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间">
          <el-date-picker
            v-model="queryPage.timeRange"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            align="right"
          />
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

        <el-table-column :show-overflow-tooltip="true" :resizable="false" align="center" label="任务名称" width="250">
          <template slot-scope="scope">
            {{ currentTask.taskName }}
          </template>
        </el-table-column>

        <el-table-column :resizable="false" align="center" label="执行状态" width="150">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.executeStatus" type="success" effect="dark">
              执行成功
            </el-tag>
            <el-tag v-else type="danger">
              执行失败
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column align="center" :resizable="false" :show-overflow-tooltip="true" label="执行说明" width="300">
          <template slot-scope="scope">
            {{ scope.row.executeDesc }}
          </template>
        </el-table-column>

        <el-table-column align="center" :resizable="false" label="执行时间" width="250">
          <template slot-scope="scope">
            {{ scope.row.executeTime }}
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

      <div style="text-align:right;">
        <el-button type="primary" size="small" @click="logDialogVisible=false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import { executeTask, getTaskList, getTaskLog, removeTask, saveOrUpdateTask, taskOperaHandle } from '@/api/task'
import Clone from '../../../../mock/utils'

const defaultTask = {
  taskId: undefined,
  taskKey: '',
  taskName: '',
  taskDesc: '',
  taskStatus: 0,
  taskCron: ''
}
const defaultQueryPage = {
  pageNum: 1,
  pageSize: 5,
  timeRange: null,
  executeStatus: null,
  taskId: null
}
export default {
  name: 'Task',
  data() {
    return {
      task: Object.assign({}, defaultTask),
      taskList: [],
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new',

      currentTask: {},
      logDialogVisible: false,

      pageInfo: {},
      queryPage: {
        pageNum: 1,
        pageSize: 5,
        timeRange: null,
        executeStatus: null,
        taskId: null
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
        if (this.logDialogVisible) {
          this.getLogList(val)
        }
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    taskTypeChange(event) {
      this.$forceUpdate()
    },
    getList() {
      this.listLoading = true
      getTaskList().then(res => {
        this.listLoading = false
        this.taskList = res.data
      })
    },
    handleAddTask() {
      this.task = Object.assign({}, defaultTask)
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleEdit(scope) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.task = deepClone(scope.row)
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这个任务吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await removeTask(row.taskId)
          this.$message({
            type: 'success',
            message: '任务删除成功!'
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

    showLog(row) {
      this.queryPage = Clone.deepClone(defaultQueryPage)
      this.currentTask = row.row
      this.queryPage.taskId = row.row.taskId
      this.logDialogVisible = true
    },
    execute(row) {
      executeTask(row.row.taskId).then((res) => {
        this.$message({
          type: 'success',
          message: res.msg
        })
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
    },

    getLogList(data) {
      this.listLoading = true
      getTaskLog(data).then(res => {
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
