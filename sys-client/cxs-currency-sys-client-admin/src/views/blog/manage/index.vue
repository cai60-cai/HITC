<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form ref="ruleForm" :model="listQuery" :inline="true" label-width="100px" class="demo-ruleForm">
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

        <el-form-item label="文章状态">
          <el-select v-model="listQuery.articleStatus" placeholder="请选择">
            <el-option
              label="请选择"
              :value="null"
            />
            <el-option
              label="待审核"
              :value="0"
            />
            <el-option
              label="已通过"
              :value="1"
            />
            <el-option
              label="未通过"
              :value="2"
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
              />
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
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="文章状态" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag
            v-if="row.articleStatus === 1"
            type="success"
            size="mini"
            style="margin-left: 5px;"
          >
            已通过
          </el-tag>
          <el-tag
            v-else
            type="info"
            style="margin-left: 5px;"
            size="mini"
          >
            待审核
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" label="发布时间" align="center" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250px" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="{row}">
          <el-button type="success" size="mini" @click="editHandler(row)">
            查看
          </el-button>
          <el-button size="mini" type="danger" @click="deleteHandle(row)">
            删除
          </el-button>
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
    <!--        查看-->
    <el-dialog
      width="80%"
      :title="'查看文章:' + currentBlob.articleTitle"
      :visible="showVisible"
      :before-close="editHandleCancel"
    >
      <div>
        <div class="content">
          <el-form ref="form" :model="articleInfo" label-width="80px">
            <el-form-item label="文章标题">
              {{articleInfo.articleTitle}}
            </el-form-item>
            <el-form-item label="文章摘要">
              {{articleInfo.articleAbstract}}
            </el-form-item>
            <el-form-item label="是否原创" class="postInfo-container-item">
              <el-tag type="success" v-if="articleInfo.articleIsSelf === 1">
                原创
              </el-tag>
              <el-tag type="info" v-else>
                转载
              </el-tag>
            </el-form-item>
            <el-form-item v-if="articleInfo.articleIsSelf == 0" style="margin-bottom: 20px;" label="原文地址">
              {{articleInfo.originalLink}}
            </el-form-item>
            <el-form-item v-if="articleInfo.type" label="文章分类" class="postInfo-container-item" prop="articleType">
              <el-select
                v-model="articleInfo.type.id"
                disabled
                filterable
                default-first-option
                placeholder="选择分类"
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
                  />
                </el-option-group>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <v-md-editor :value="articleInfo.articleDetail" mode="preview" />
        <div class="content">
          <div v-if="(articleInfo && articleInfo.tags)" class="article-tag">
            <span class="lable-tip">文章封面:</span>
            <el-image
              style="width: 300px; height: 250px; border: 1px solid #ccc"
              :src="baseUrl + articleInfo.articleCover"
            />
          </div>
          <div v-if="(articleInfo && articleInfo.tags)" class="article-tag">
            <span class="lable-tip">标签:</span>
            <el-tag v-for="tag in articleInfo.tags" :key="tag.tagId" style="margin-right:10px">
              {{ tag.tagName }}
            </el-tag>
          </div>

          <div
            v-if="articleInfo.fileInfo && articleInfo.fileInfo.fileSize !== undefined && articleInfo.fileInfo.fileName"
            id="position-download"
            class="article-file"
          >
            <div class="file-info">
              <div>
                文件名：
                <el-link :underline="false" class="item" @click="download(articleInfo)">
                  {{ articleInfo.fileInfo.fileName }}
                </el-link>
                文件大小: {{ Math.floor(articleInfo.fileInfo.fileSize / 1024) }}KB
              </div>
              <el-button
                type="primary"
                size="small"
                icon="el-icon-download"
                @click="download(articleInfo.fileInfo.fileId)"
              >下载文件
              </el-button>
            </div>
          </div>

          <div v-if="(articleInfo.fileInfo && articleInfo.fileInfo.fileStructure)" class="file-structure">
            <el-tree
              :render-content="renderContent"
              :default-expand-all="false"
              :expand-on-click-node="false"
              :data="articleInfo.fileInfo.fileStructure"
              :props="{children: 'children',label: 'name'}"
            />
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editHandleCancel">取 消</el-button>
        <el-button size="mini" type="primary" @click="editHandleOk">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination'
import { deleteArticle, getArticleInfo, getArticleList, reviewedArticle, updateArticle } from '@/api/article'
import { URL_PREFIX } from '@/api/config'
import { getAdminTechnologyTypeList } from '@/api/technology'

export default {
  name: 'BlogManage',
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
        articleType: null,
        articleStatus: null
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
    this.getTypeTreeList()
  },
  methods: {
    getList(data) {
      this.listLoading = true
      getArticleList(data).then(response => {
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
    // 确认修改
    editHandleOk() {
      this.showVisible = false
    },
    download(fileId) {
      window.location.href = this.baseUrl + '/admin/article/download?fileId=' + fileId
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
        <span class='custom-tree-node'>
          <span>
            {data.children && data.children.length > 0 ? <i class='el-icon-folder' style='color: gold;'></i>
              : <i class='el-icon-tickets' style='color: #409eff;'></i>}
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
