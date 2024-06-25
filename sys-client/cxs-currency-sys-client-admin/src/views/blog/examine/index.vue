<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :model="listQuery" :inline="true" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="是否原创">
          <el-select v-model="listQuery.articleIsSelf" placeholder="请选择">
            <el-option
              label="请选择"
              :value="null"
            />
            <el-option
              label="原创"
              :value="1"
            />
            <el-option
              label="转载"
              :value="0"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="listQuery.articleType" filterable placeholder="请选择">
            <el-option-group
              v-for="group in typeTreeList"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="item in group.options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-option-group>
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
      row-key="articleId"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column :resizable="false" label="博客ID" align="center" width="100px">
        <template slot-scope="{row}">
          <span>{{ row.articleId }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="博客标题" width="300px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.articleTitle }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="类别" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.typeName }}</span>
        </template>
      </el-table-column>

      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="是否原创" align="center" min-width="100px">
        <template slot-scope="{row}">
          <el-tag v-if="row.articleIsSelf === 1" type="success" effect="dark">
            原创
          </el-tag>
          <el-tag v-else type="info" effect="dark">
            转载
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="发布时间" align="center" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="400px" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="editHandler(row)">
            编辑
          </el-button>
          <el-button type="primary" size="mini" @click="examineHandler(row)">
            审核
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
    <!--        编辑-->
    <el-dialog
      width="80%"
      :title="'编辑文章:' + currentBlob.articleTitle"
      :visible="showVisible"
      :before-close="editHandleCancel"
    >
      <div>
        <div class="content">
          <el-form ref="form" :model="articleInfo" label-width="80px">
            <el-form-item label="文章标题">
              <el-input v-model="articleInfo.articleTitle" style="width: 400px" :rows="1" type="textarea" maxlength="20"
                        show-word-limit class="article-textarea" autosize placeholder="请输入文章标题,不超过20字"
              />
            </el-form-item>
            <el-form-item label="文章摘要">
              <el-input v-model="articleInfo.articleAbstract" style="width: 600px" :rows="1" maxlength="130"
                        show-word-limit type="textarea" class="article-textarea" autosize
                        placeholder="请输入文章摘要，0-130字"
              />
            </el-form-item>
            <el-form-item label="是否原创" class="postInfo-container-item">
              <el-switch
                style="display: block;margin-left: 0;margin-top: 9px;"
                v-model="articleInfo.articleIsSelf"
                active-color="#13ce66"
                :active-value="1"
                :inactive-value="0"
                active-text="原创"
                inactive-text="转载"
              >
              </el-switch>
            </el-form-item>
            <el-form-item style="margin-bottom: 20px;" label="原文地址" v-if="articleInfo.articleIsSelf == 0">
              <el-input v-model="articleInfo.originalLink" style="width: 600px" :rows="1" maxlength="80"
                        show-word-limit type="textarea" class="article-textarea" autosize
                        placeholder="转载请输入原文地址"
              />
            </el-form-item>
            <el-form-item v-if="articleInfo.type" label="文章分类" class="postInfo-container-item" prop="articleType">
              <el-select v-model="articleInfo.type.id" filterable default-first-option placeholder="选择分类"
                         style="width: 400px"
              >
                <el-option-group
                  v-for="group in teachnologyList"
                  :key="group.id"
                  :label="group.typeName"
                >
                  <el-option
                    v-for="item in group.children"
                    :key="item.id"
                    :label="item.typeName"
                    :value="item.id"
                  >
                  </el-option>
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <v-md-editor :value="articleInfo.articleDetail" mode="preview"></v-md-editor>
        <div class="content">
          <div class="article-tag" v-if="(articleInfo && articleInfo.tags)">
            <span class="lable-tip">文章封面:</span>
            <el-image style="width: 300px; height: 250px; border: 1px solid #ccc"
                      :src="baseUrl + articleInfo.articleCover"
            ></el-image>
          </div>
          <div class="article-tag" v-if="(articleInfo && articleInfo.tags)">
            <span class="lable-tip">标签:</span>
            <el-tag style="margin-right:10px" v-for="tag in articleInfo.tags" :key="tag.tagId">
              {{ tag.tagName }}
            </el-tag>
          </div>

          <div class="article-file" id="position-download"
               v-if="articleInfo.fileInfo && articleInfo.fileInfo.fileSize !== undefined && articleInfo.fileInfo.fileName"
          >
            <div class="file-info">
              <div>
                文件名：
                <el-link :underline="false" class="item" @click="download(articleInfo)">
                  {{ articleInfo.fileInfo.fileName }}
                </el-link>
                文件大小: {{ Math.floor(articleInfo.fileInfo.fileSize / 1024) }}KB
              </div>
              <el-button type="primary" size="small" icon="el-icon-download"
                         @click="download(articleInfo.fileInfo.fileId)"
              >下载文件
              </el-button>
            </div>
          </div>

          <div class="file-structure" v-if="(articleInfo.fileInfo && articleInfo.fileInfo.fileStructure)">
            <el-tree :render-content="renderContent" :default-expand-all="false" :expand-on-click-node="false"
                     :data="articleInfo.fileInfo.fileStructure" :props="{children: 'children',label: 'name'}"
            ></el-tree>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editHandleCancel">取 消</el-button>
        <el-button size="mini" type="primary" @click="editHandleOk">确 定</el-button>
      </span>
    </el-dialog>
    <!--        审核-->
    <el-dialog
      :title="'审核文章:' + currentBlob.articleTitle"
      :visible="examineVisible"
      :before-close="examineHandleCancel"
    >
            <el-form ref="form" style="padding: 5px 10px">

<!--              <el-form-item label="下载积分" v-if="currentBlob.downLoadFlag">-->
<!--                <el-input-number :precision="0" :min="0" v-model="examineInfo.articleDownloadPoint" />-->
<!--              </el-form-item>-->

<!--              <el-form-item label="文章评分" v-if="currentBlob.downLoadFlag">-->
<!--                <el-input-number :precision="1" :min="0" :max="5" v-model="examineInfo.articleRate" />-->
<!--              </el-form-item>-->

              <el-form-item label="官方推荐" class="postInfo-container-item">
                <el-switch
                  style="display: block;margin-left: 0;margin-top: 9px;"
                  v-model="examineInfo.articleIsSelf"
                  active-color="#13ce66"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="推荐"
                  inactive-text="不推荐">
                </el-switch>
              </el-form-item>

              <el-form-item label="审核状态">
                <el-select v-model="examineInfo.articleStatus" placeholder="请选择">
                  <el-option
                    label="通过"
                    :value="1"
                  />
                  <el-option
                    label="不通过"
                    :value="2"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="文章备注">
                <el-input v-model="examineInfo.articleDesc" style="width: 600px" :rows="1" maxlength="200"
                          show-word-limit type="textarea" class="article-textarea" autosize
                          placeholder="请输入文章备注，0-200字"
                />
              </el-form-item>
            </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="examineHandleCancel">取 消</el-button>
        <el-button size="mini" type="primary" @click="examineHandleOk">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import { deleteArticle, getArticleInfo, getReviewedList, reviewedArticle, updateArticle } from '@/api/article'
import { URL_PREFIX } from '@/api/config'
import { getAdminTechnologyTypeList } from '@/api/technology'

const defaultExamineInfo = {
  articleDesc: '',
  articleDownloadPoint: null,
  articleId: null,
  articleIsRecommend: 0,
  articleRate: null,
  articleStatus: null
}
export default {
  name: 'BlogExamine',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      baseUrl: URL_PREFIX,
      list: [],
      typeTreeList: [],
      total: 0,
      pageInfo: {},
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        articleIsSelf: null,
        articleType: null
      },
      currentBlob: {
        title: ''
      },
      articleInfo: {},
      article: {},
      teachnologyList: [],
      downloadLoading: false,
      currentUser: {},
      // 查看状态
      showVisible: false,
      // 审核状态
      examineVisible: false,
      examineInfo: {
        articleDesc: '',
        articleDownloadPoint: null,
        articleId: null,
        articleIsRecommend: 0,
        articleRate: null,
        articleStatus: null
      }
    }
  },
  mounted() {
    this.getList(this.listQuery)
    this.getTypeTreeList()
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
    getList(data) {
      this.listLoading = true
      getReviewedList(data).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.pageInfo = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    getTypeTreeList() {
      getAdminTechnologyTypeList().then(response => {
        const typeList = response.data
        this.teachnologyList = typeList
        this.typeTreeList = this.dataListHandle(typeList)
      })
    },
    // 处理树形分类
    dataListHandle(list) {
      const result = []
      list.forEach(l => {
        const temp = {
          label: l.typeName,
          options: []
        }
        if (l.children && l.children.length > 0) {
          l.children.forEach(r => {
            const tempChild = {
              label: r.typeName,
              value: r.id
            }
            temp.options.push(tempChild)
          })
        }
        result.push(temp)
      })
      const temp = {
        label: '默认',
        options: [
          {
            label: '请选择',
            value: null
          }
        ]
      }
      result.unshift(temp)
      return result
    },
    // 查看
    async editHandler(record) {
      this.showVisible = true
      this.currentBlob = record
      // 请求数据
      const { data } = await getArticleInfo(record.articleId)
      this.articleInfo = data
    },
    // 审核
    examineHandler(record) {
      this.examineVisible = true
      this.currentBlob = record
      this.examineInfo.articleId = record.articleId
    },
    // 确认修改
    editHandleOk() {
      const {
        articleId,
        articleTitle,
        articleAbstract,
        articleType,
        articleIsSelf,
        originalLink
      } = this.articleInfo
      const article = {
        articleId,
        articleTitle,
        articleAbstract,
        articleType,
        articleIsSelf,
        originalLink
      }
      updateArticle(article).then(res => {
        this.$message.success(res.msg)
        this.getList(this.listQuery)
      })
      setTimeout(() => {
        this.showVisible = false
        this.currentBlob = {}
      }, 1000)
    },
    download(fileId) {
      window.location.href = this.baseUrl + '/admin/article/download?fileId=' + fileId
    },
    // 确认审核
    examineHandleOk() {
      reviewedArticle(this.examineInfo).then(res => {
        this.$message.success(res.msg)
        this.getList(this.listQuery)
      })
      setTimeout(() => {
        this.examineVisible = false
        this.currentBlob = {}
        this.examineInfo = defaultExamineInfo
      }, 1000)
      this.examineVisible = false
    },

    examineHandleCancel(e) {
      this.examineVisible = false
    },
    editHandleCancel(e) {
      this.showVisible = false
    },
    deleteHandle(row) {
      this.$confirm('确定删除这条博客吗?', '删除博客', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        deleteArticle(row.articleId).then(() => {
          this.$message.success('文章删除成功')
          this.getList(this.listQuery)
        })
      }).catch(err => {
        console.error(err)
      })
    },
    // 搜索按钮触发
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList(this.listQuery)
    },
    // 自定义文件树的显示内容
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
            <span>
              {data.children && data.children.length > 0 ? <i class="el-icon-folder" style="color: gold;"></i> :
                <i class="el-icon-tickets" style="color: #409eff;"></i>}
              &nbsp;{data.name}

            </span>&nbsp;&nbsp;
          <span>
              {data.size && data.size > 0 ? Math.floor(data.size / 1024) + 'KB' : ''}
            </span>
          </span>)
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
