<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :model="listQuery" :inline="true" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="举报状态">
          <el-select v-model="listQuery.reportStatus">
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
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="举报id" width="100">
        <template slot-scope="scope">
          {{ scope.row.reportId }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="举报类型" width="200">
        <template slot-scope="scope">
          {{ scope.row.reportType }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="举报对象" width="150">
        <template slot-scope="{row}">
          <el-tag v-if="row.reportTarget === 1" type="success" effect="dark">
            博客
          </el-tag>
          <el-tag v-else type="success">
            评论
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="举报内容" width="400">
        <template slot-scope="scope">
          {{ scope.row.reportContent }}
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="举报状态" width="150">
        <template slot-scope="{row}">
          <el-tag v-if="row.reportStatus === 1" type="success" effect="dark">
            已处理
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            待处理
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" :show-overflow-tooltip="true" align="center" label="举报时间" width="200">
        <template slot-scope="{row}">
          {{row.reportTime}}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button v-if="scope.row.reportStatus === 0" type="primary" size="small" @click="reportClickHandle(scope)">处理</el-button>
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

    <el-dialog width="70%" :visible.sync="handleVisible" title="处理举报">
      <el-form :model="type" label-width="80px" label-position="left">

        <el-form-item label="举报类型">
          {{type.reportType}}
        </el-form-item>

        <el-form-item label="举报内容">
          {{type.reportContent}}
        </el-form-item>

        <el-form-item label="举报对象">
          <el-tag title="点击查看详情" class="cursor" v-if="type.reportTarget === 1" type="success" effect="dark">
            <span v-if="type.articleInfoVO" @click="targetInfo(type)">
              博客:{{type.articleInfoVO.articleTitle}}
            </span>
            <span v-else>
              该文章已删除~
            </span>
          </el-tag>
          <el-tag title="点击查看详情" class="cursor" v-if="type.reportTarget === 2" type="success">
            <span v-if="type.commentVO" @click="targetInfo(type)">
              评论:{{type.commentVO.commentContent}}
            </span>
            <span v-else>
              该评论已删除~
            </span>
          </el-tag>
        </el-form-item>

        <el-form-item v-if="type.reportImages && type.reportImages.length > 0" label="举报图片">
          <el-image style="width: 300px; height: 200px; margin-right: 10px" v-for="item in type.reportImages" :key="item" :src="baseUrl + item"/>
        </el-form-item>

        <el-form-item label="处理方式">
          <el-select v-model="handleInfo.handleNo" style="width: 30%">
            <el-option :label="item.handleDesc" :value="item.handleNo" v-for="item in typeList" :key="item.handleNo"/>
          </el-select>
        </el-form-item>

        <el-form-item label="禁止时长">
          <el-input-number style="width: 30%" :min="0" v-model="handleInfo.banMinute" :precision="0" placeholder="请输入权限禁止时长，单位/分钟"/>
        </el-form-item>

        <el-form-item label="接收邮件">
          <el-input v-model="handleInfo.reportEmail" style="width: 30%" />
        </el-form-item>

        <el-form-item label="邮件通知" class="postInfo-container-item">
          <el-switch
            style="display: block;margin-left: 0;margin-top: 9px;"
            v-model="handleInfo.emailFlag"
            active-color="#13ce66"
            :active-value="1"
            :inactive-value="0"
            active-text="通知"
            inactive-text="不通知"
          >
          </el-switch>
        </el-form-item>

        <el-form-item label="处理描述">
          <el-input v-model="handleInfo.replyContent" :rows="5" maxlength="1000" resize="none"
                    show-word-limit type="textarea" class="article-textarea"
                    placeholder="请输入举报处理描述，0-1000字"
          />
        </el-form-item>
      </el-form>

      <el-dialog
        width="70%"
        title="被举报资源详情"
        :visible.sync="innerVisible"
        append-to-body>
        <div v-if="type.reportTarget === 1">
          <div v-if="type.articleInfoVO">
            <div class="content">
              <el-form ref="form" label-width="80px">
                <el-form-item label="文章标题">
                  <el-input
                    v-model="type.articleInfoVO.articleTitle"
                    disabled
                    style="width: 400px"
                    :rows="1"
                    type="textarea"
                    maxlength="20"
                    show-word-limit
                    class="article-textarea"
                    autosize
                    placeholder="请输入文章标题,不超过20字"
                  />
                </el-form-item>
                <el-form-item label="文章摘要">
                  <el-input
                    v-model="type.articleInfoVO.articleAbstract"
                    disabled
                    style="width: 600px"
                    :rows="1"
                    maxlength="130"
                    show-word-limit
                    type="textarea"
                    class="article-textarea"
                    autosize
                    placeholder="请输入文章摘要，0-130字"
                  />
                </el-form-item>
                <el-form-item label="是否原创" class="postInfo-container-item">
                  <el-switch
                    v-model="type.articleInfoVO.articleIsSelf"
                    disabled
                    style="display: block;margin-left: 0;margin-top: 9px;"
                    active-color="#13ce66"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="原创"
                    inactive-text="转载"
                  />
                </el-form-item>
                <el-form-item v-if="type.articleInfoVO.articleIsSelf == 0" style="margin-bottom: 20px;" label="原文地址">
                  <el-input
                    v-model="type.articleInfoVO.originalLink"
                    disabled
                    style="width: 600px"
                    :rows="1"
                    maxlength="80"
                    show-word-limit
                    type="textarea"
                    class="article-textarea"
                    autosize
                    placeholder="转载请输入原文地址"
                  />
                </el-form-item>
                <el-form-item v-if="type.articleInfoVO.type" label="文章分类" class="postInfo-container-item" prop="articleType">
                  <el-select
                    v-model="type.articleInfoVO.type.id"
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
            <v-md-editor :value="type.articleInfoVO.articleDetail" mode="preview" />
            <div class="content">
              <div v-if="(type.articleInfoVO && type.articleInfoVO.tags)" class="article-tag">
                <span class="lable-tip">文章封面:</span>
                <el-image
                  style="width: 300px; height: 250px; border: 1px solid #ccc"
                  :src="baseUrl + type.articleInfoVO.articleCover"
                />
              </div>
              <div v-if="(type.articleInfoVO && type.articleInfoVO.tags)" class="article-tag">
                <span class="lable-tip">标签:</span>
                <el-tag v-for="tag in type.articleInfoVO.tags" :key="tag.tagId" style="margin-right:10px">
                  {{ tag.tagName }}
                </el-tag>
              </div>

              <div
                v-if="type.articleInfoVO.fileInfo && type.articleInfoVO.fileInfo.fileSize !== undefined && type.articleInfoVO.fileInfo.fileName"
                class="article-file"
              >
                <div class="file-info">
                  <div>
                    文件名：
                    <el-link :underline="false" class="item">
                      {{ type.articleInfoVO.fileInfo.fileName }}
                    </el-link>
                    文件大小: {{ Math.floor(type.articleInfoVO.fileInfo.fileSize / 1024) }}KB
                  </div>
                </div>
              </div>

              <div v-if="(type.articleInfoVO.fileInfo && type.articleInfoVO.fileInfo.fileStructure)" class="file-structure">
                <el-tree
                  :render-content="renderContent"
                  :default-expand-all="false"
                  :expand-on-click-node="false"
                  :data="type.articleInfoVO.fileInfo.fileStructure"
                  :props="{children: 'children',label: 'name'}"
                />
              </div>
            </div>
          </div>
          <span v-else>该文章已删除~</span>
        </div>
        <div v-if="type.reportTarget === 2">
          <el-tree
            v-if="type.commentVO"
            :default-expand-all="true"
            :data="[type.commentVO]"
            :props="defaultProps"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>{{ data.commentTime }}</span>
            </span>
          </el-tree>
        </div>
      </el-dialog>
      <div style="text-align:right;">
        <el-button type="danger" @click="handleVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmType">确认</el-button>
      </div>
    </el-dialog>


    <el-dialog width="70%" :visible.sync="showVisible" title="查看举报">
      <el-form :model="type" label-width="80px" label-position="left">

        <el-form-item label="举报类型">
          {{type.reportType}}
        </el-form-item>

        <el-form-item label="举报内容">
          {{type.reportContent}}
        </el-form-item>

        <el-form-item label="举报对象">
          <el-tag title="点击查看详情" class="cursor" v-if="type.reportTarget === 1" type="success" effect="dark">
            <span v-if="type.articleInfoVO" @click="targetInfo(type)">
              博客:{{type.articleInfoVO.articleTitle}}
            </span>
            <span v-else>
              该文章已删除~
            </span>
          </el-tag>
          <el-tag title="点击查看详情" class="cursor" v-if="type.reportTarget === 2" type="success">
            <span v-if="type.commentVO" @click="targetInfo(type)">
              评论:{{type.commentVO.commentContent}}
            </span>
            <span v-else>
              该评论已删除~
            </span>
          </el-tag>
        </el-form-item>

        <el-form-item v-if="type.reportImages && type.reportImages.length > 0" label="举报图片">
          <el-image style="width: 300px; height: 200px; margin-right: 10px" v-for="item in type.reportImages" :key="item" :src="baseUrl + item"/>
        </el-form-item>

        <el-form-item label="处理方式">
          <el-tag v-if="type.reportStatus === 1" type="success" effect="dark">
            已处理
          </el-tag>
          <el-tag v-else type="danger" effect="dark">
            待处理
          </el-tag>
        </el-form-item>

        <el-form-item label="处理方式" v-if="type.reportStatus == 1">
          {{type.reportHandleResult}}
        </el-form-item>


        <el-form-item label="处理描述" v-if="type.reportStatus == 1">
          {{type.reportHandleDesc}}
        </el-form-item>

        <el-form-item label="处理时间" v-if="type.reportHandleTime && type.reportStatus == 1">
          {{type.reportHandleTime}}
        </el-form-item>
      </el-form>

      <el-dialog
        width="70%"
        title="被举报资源详情"
        :visible.sync="innerVisible"
        append-to-body>
        <div v-if="type.reportTarget === 1">
          <div v-if="type.articleInfoVO">
            <div class="content">
              <el-form ref="form" label-width="80px">
                <el-form-item label="文章标题">
                  <el-input
                    v-model="type.articleInfoVO.articleTitle"
                    disabled
                    style="width: 400px"
                    :rows="1"
                    type="textarea"
                    maxlength="20"
                    show-word-limit
                    class="article-textarea"
                    autosize
                    placeholder="请输入文章标题,不超过20字"
                  />
                </el-form-item>
                <el-form-item label="文章摘要">
                  <el-input
                    v-model="type.articleInfoVO.articleAbstract"
                    disabled
                    style="width: 600px"
                    :rows="1"
                    maxlength="130"
                    show-word-limit
                    type="textarea"
                    class="article-textarea"
                    autosize
                    placeholder="请输入文章摘要，0-130字"
                  />
                </el-form-item>
                <el-form-item label="是否原创" class="postInfo-container-item">
                  <el-switch
                    v-model="type.articleInfoVO.articleIsSelf"
                    disabled
                    style="display: block;margin-left: 0;margin-top: 9px;"
                    active-color="#13ce66"
                    :active-value="1"
                    :inactive-value="0"
                    active-text="原创"
                    inactive-text="转载"
                  />
                </el-form-item>
                <el-form-item v-if="type.articleInfoVO.articleIsSelf == 0" style="margin-bottom: 20px;" label="原文地址">
                  <el-input
                    v-model="type.articleInfoVO.originalLink"
                    disabled
                    style="width: 600px"
                    :rows="1"
                    maxlength="80"
                    show-word-limit
                    type="textarea"
                    class="article-textarea"
                    autosize
                    placeholder="转载请输入原文地址"
                  />
                </el-form-item>
                <el-form-item v-if="type.articleInfoVO.type" label="文章分类" class="postInfo-container-item" prop="articleType">
                  <el-select
                    v-model="type.articleInfoVO.type.id"
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
            <v-md-editor :value="type.articleInfoVO.articleDetail" mode="preview" />
            <div class="content">
              <div v-if="(type.articleInfoVO && type.articleInfoVO.tags)" class="article-tag">
                <span class="lable-tip">文章封面:</span>
                <el-image
                  style="width: 300px; height: 250px; border: 1px solid #ccc"
                  :src="baseUrl + type.articleInfoVO.articleCover"
                />
              </div>
              <div v-if="(type.articleInfoVO && type.articleInfoVO.tags)" class="article-tag">
                <span class="lable-tip">标签:</span>
                <el-tag v-for="tag in type.articleInfoVO.tags" :key="tag.tagId" style="margin-right:10px">
                  {{ tag.tagName }}
                </el-tag>
              </div>

              <div
                v-if="type.articleInfoVO.fileInfo && type.articleInfoVO.fileInfo.fileSize !== undefined && type.articleInfoVO.fileInfo.fileName"
                id="position-download"
                class="article-file"
              >
                <div class="file-info">
                  <div>
                    文件名：
                    <el-link :underline="false" class="item">
                      {{ type.articleInfoVO.fileInfo.fileName }}
                    </el-link>
                    文件大小: {{ Math.floor(type.articleInfoVO.fileInfo.fileSize / 1024) }}KB
                  </div>
                </div>
              </div>

              <div v-if="(type.articleInfoVO.fileInfo && type.articleInfoVO.fileInfo.fileStructure)" class="file-structure">
                <el-tree
                  :render-content="renderContent"
                  :default-expand-all="false"
                  :expand-on-click-node="false"
                  :data="type.articleInfoVO.fileInfo.fileStructure"
                  :props="{children: 'children',label: 'name'}"
                />
              </div>
            </div>
          </div>
          <span v-else>该文章已删除~</span>
        </div>
        <div v-if="type.reportTarget === 2">
          <el-tree
            v-if="type.commentVO"
            :default-expand-all="true"
            :data="[type.commentVO]"
            :props="defaultProps"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>{{ data.commentTime }}</span>
            </span>
          </el-tree>
        </div>
      </el-dialog>
      <div style="text-align:right;">
        <el-button type="primary" @click="showVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import {
  adminGetHandleType,
  adminGetReportInfo,
  handleReport,
  removeReport
} from '@/api/report'
import { URL_PREFIX } from '@/api/config'
import waves from '@/directive/waves'
import { adminGetReportList } from '@/api/report'
import { getAdminTechnologyTypeList } from '@/api/technology'

const defaultType = {
  banMinute: null,
  emailFlag: null,
  handleNo: '',
  reportHandleDesc: '',
  reportId: null
}
const defaultListQuery = {
  pageNum: 1,
  pageSize: 10,
  reportStatus: 0
}
export default {
  name: 'Report',
  directives: { waves },
  data() {
    return {
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        reportStatus: 0
      },
      pageInfo:{},
      baseUrl: URL_PREFIX,
      list: [],
      total: 0,

      pointRechargeTypeList: [],
      listLoading: true,
      handleVisible: false,
      showVisible: false,
      dialogType: 'new',
      type: Object.assign({}, defaultType),
      qian: 0,
      typeList: [],
      handleInfo: Object.assign({}, defaultType),
      innerVisible: false,
      currentInnerTitle: '',
      teachnologyList: [],
      typeTreeList: [],
      defaultProps:{
        children: 'childCommentList',
        label: 'commentContent'
      }
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
      adminGetReportList(data).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.pageInfo = response.data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    reportClickHandle(scope) {
      this.handleInfo.reportId = scope.row.reportId
      adminGetHandleType(scope.row.reportTarget).then(res =>{
        this.typeList = res.data
      })
      this.$nextTick(() => {
        adminGetReportInfo(scope.row.reportId).then(res => {
          this.type = res.data
          this.handleVisible = true
        })
      })
    },
    handleShow(scope) {
      this.showVisible = true
      adminGetReportInfo(scope.row.reportId).then(res => {
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
          removeReport(row.reportId).then(res => {
            this.getList(this.listQuery)
            this.$message({
              type: 'success',
              message: res.msg
            })
          })
        })
        .catch(err => {
          console.error(err)
        })
    },
    // 确认处理举报
    async confirmType() {
      handleReport(this.handleInfo).then(res => {
        this.$message.success(res.msg)
        this.handleVisible = false
        this.type = {}
        this.getList(this.listQuery)
      })
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
    },
    targetInfo(item) {
      this.innerVisible = true
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

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
