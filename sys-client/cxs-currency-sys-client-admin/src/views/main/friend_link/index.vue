<template>
  <div class="tab-container">
    <el-button type="primary" @click="dialogFormVisible = true">创建新链接</el-button>
    <el-tabs v-model="activeName" style="margin-top:15px;" type="border-card">
      <el-tab-pane v-for="item in tabMapOptions" :key="item.key" :label="item.label" :name="item.key">
        <keep-alive>
          <tab-pane v-if="activeName==item.key" :type="item.key" @create="showCreatedTimes" />
        </keep-alive>
      </el-tab-pane>
    </el-tabs>
    <el-dialog title="创建" :visible.sync="dialogFormVisible">
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
        <el-button type="primary" @click="createData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import TabPane from './components/TabPane'
import { saveOrUpdateExternalFriendLink } from '@/api/friend_link'
const defaultTemp = {
  externalLinkBlank: '',
  externalLinkDesc: '',
  externalLinkIcon: '',
  externalLinkId: 1,
  externalLinkLink: '',
  externalLinkName: '',
  externalLinkStatus: 1
}
export default {
  name: 'FriendLink',
  components: { TabPane },
  data() {
    return {
      tabMapOptions: [
        { label: '待审核', key: '0' },
        { label: '已通过', key: '1' },
        { label: '已拒绝', key: '2' }
      ],
      activeName: '1',
      createdTimes: 0,
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
      rules: {
        externalLinkName: [{ required: true, message: '链接名称为必填项', trigger: 'change' }],
        externalLinkLink: [{ required: true, message: '链接路径为必传项', trigger: 'blur' }]
      }
    }
  },
  watch: {
    activeName(val) {
      this.$router.push(`${this.$route.path}?tab=${val}`)
    }
  },
  created() {
    // init the default selected tab
    const tab = this.$route.query.tab
    if (tab) {
      this.activeName = tab
    }
  },
  methods: {
    showCreatedTimes() {
      this.createdTimes = this.createdTimes + 1
    },
    menuStatusChange(event) {
      this.$forceUpdate()
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          saveOrUpdateExternalFriendLink(this.temp).then(() => {
            this.dialogFormVisible = false
            this.temp = defaultTemp
            this.$notify({
              title: 'OK',
              message: '链接新增成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.tab-container {
  margin: 30px;
}
</style>
