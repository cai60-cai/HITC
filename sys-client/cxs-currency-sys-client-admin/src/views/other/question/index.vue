<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddQuestion">创建新问题</el-button>

    <el-table v-loading="listLoading" :data="questionList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="问题id" width="100px">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="问题" min-width="300px">
        <template slot-scope="scope">
          {{ scope.row.question }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否可见" width="150px">
        <template slot-scope="{row}">
          <el-tag v-if="row.isShow" type="success" effect="dark">
            可见
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            不可见
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="创建时间" width="300px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="300px" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="detail(scope)">查看</el-button>
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" width="1000px" :title="dialogType==='add'?'新增问题':'修改问题'">
      <el-form :model="question" label-width="80px" label-position="left">
        <el-form-item label="问题">
          <el-input v-model="question.question" placeholder="问题" />
        </el-form-item>

        <el-form-item label="是否可见">
          <el-switch
            v-model="question.isShow"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="可见"
            inactive-text="不可见"
          />
        </el-form-item>

        <el-form-item label="问题回答">
          <v-md-editor
            v-model="question.questionAnswer"
            height="500px"
            :disabled-menus="[]"
            @upload-image="handleUploadImage"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmQuestion">确认</el-button>
      </div>
    </el-dialog>

    <!--    问题详情-->
    <el-dialog :visible.sync="detailDialogVisible" :title="detailInfo.question">
      <div>
        <h3>问题: {{ detailInfo.question }}</h3>
        <v-md-editor :value="detailInfo.questionAnswer" mode="preview" />
      </div>
      <div style="text-align:right;">
        <el-button type="primary" @click="detailDialogVisible=false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import { getQuestionList, removeQuestion, saveOrUpdateQuestion } from '@/api/question'
import { fileUpload } from '@/api/base'
import { URL_PREFIX } from '@/api/config'

const defaultQuestion = {
  id: 0,
  question: '',
  questionAnswer: '',
  isShow: true
}

export default {
  name: 'Question',
  data() {
    return {
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        keyWord: ''
      },
      questionPage: {},
      questionList: [],
      question: Object.assign({}, defaultQuestion),
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new',
      detailInfo: {},
      detailDialogVisible: false
    }
  },
  computed: {
    routesData() {
      return this.routes
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.listLoading = true
      getQuestionList(this.listQuery).then(res => {
        this.questionPage = res.data
        this.questionList = res.data.data
        this.listLoading = false
      })
    },
    handleEdit(scope) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.question = deepClone(scope.row)
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这个问题吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await removeQuestion(row.id)
          this.questionList.splice($index, 1)
          this.$message({
            type: 'success',
            message: '问题删除成功!'
          })
        })
        .catch(err => {
          console.error(err)
        })
    },
    async confirmQuestion() {
      const isEdit = this.dialogType === 'edit'
      if (isEdit) {
        await saveOrUpdateQuestion(this.question)
        for (let index = 0; index < this.questionList.length; index++) {
          if (this.questionList[index].id === this.question.id) {
            this.questionList.splice(index, 1, Object.assign({}, this.question))
            break
          }
        }
      } else {
        this.question.id = undefined
        const { data } = await saveOrUpdateQuestion(this.question)
        this.question.id = data.id
        this.questionList.push(this.question)
      }

      const { id, question } = this.question
      this.dialogVisible = false
      this.$notify({
        title: 'Success',
        dangerouslyUseHTMLString: true,
        message: `
            <div>Question Id: ${id}</div>
            <div>Question: ${question}</div>
          `,
        type: 'success'
      })
    },
    handleAddQuestion() {
      this.dialogType = 'add'
      this.dialogVisible = true
      this.question = {}
    },
    handleUploadImage(event, insertImage, files) {
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      fileUpload(files[0]).then(res => {
        insertImage({
          url: URL_PREFIX + res.data,
          desc: '描述',
          width: 'auto',
          height: 'auto'
        })
      })
    },
    detail(row) {
      this.detailDialogVisible = true
      this.detailInfo = row.row
    }
  }
}
</script>

<style lang="scss" scoped>
h3{
  margin: 0;
  padding: 0;
}
.app-container {
  .questions-table {
    margin-top: 30px;
  }

  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
