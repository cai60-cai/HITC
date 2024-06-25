<template>
  <el-form :model="noticeInfo" status-icon :rules="rules" ref="ruleForm" class="content-all clearfix">
    <el-form-item label-width="50px" label="标题" prop="noticeTitle">
      <el-input v-model="noticeInfo.noticeTitle" maxlength="20" show-word-limit/>
    </el-form-item>
    <el-form-item label-width="50px" label="推送">
      <el-switch
        v-model="noticeInfo.isPush"
        active-color="#13ce66"
        inactive-color="#ccc"
        :active-value="1"
        :inactive-value="0"
      />
    </el-form-item>
    <el-form-item label-width="1px" prop="noticeContent">
      <v-md-editor
        v-model="noticeInfo.noticeContent"
        height="500px"
        @upload-image="handleUploadImage"
        :disabled-menus="[]"
      ></v-md-editor>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="$router.back()">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getNoticeInfo, saveOrUpdateNotice } from '@/api/notice'
import { URL_PREFIX } from '@/api/config'
import { fileUpload } from '@/api/base'

export default {
  name: 'NoticeEdit',
  data() {
    return {
      id: null,
      title: '',
      noticeInfo: {
        id: null,
        noticeTitle: '',
        noticeContent: '',
        isPush: null
      },
      rules: {
        noticeTitle: [
          { message: '公告标题为必传项', required: true, trigger: 'blur' }
        ],
        noticeContent: [
          { message: '公告内容为必传项', required: true, trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      const id = this.$route.params.id
      if (id && id !== '') {
        this.title = '编辑公告'
        getNoticeInfo(id).then(res => {
          this.noticeInfo = res.data
        })
      } else {
        this.title = '新增公告'
      }
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
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          saveOrUpdateNotice(this.noticeInfo).then(res => {
            this.$message.success('公告操作成功')
            this.$refs[formName].resetFields()
            this.$router.back()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.content-all{
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
}
</style>
