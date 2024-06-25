<template>
  <el-form ref="ruleForm" :model="systemInfo" status-icon class="content-all clearfix">
    <el-form-item label-width="100px" label="标题">
      <el-input v-model="systemInfo.sysTitle" maxlength="20" show-word-limit />
    </el-form-item>

    <el-form-item label-width="100px" label="标题说明">
      <el-input v-model="systemInfo.sysLogoTitle" maxlength="20" show-word-limit />
    </el-form-item>

    <el-form-item label-width="100px" label="邮箱">
      <el-input v-model="systemInfo.sysEmail" maxlength="100" show-word-limit />
    </el-form-item>

    <el-form-item label-width="100px" label="Logo">
      <div class="item-all">
        <div class="item-left">
          <el-image style="width: 150px; height: 150px" v-if="systemInfo.sysLogo" :src="baseUrl + systemInfo.sysLogo">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </div>
        <div class="item-right">
          <el-upload
            class="upload-demo"
            :action="baseUrl + '/base/file/upload'"
            :on-success="successLogo"
            :show-file-list="false"
            :before-upload="fileCheck"
            :headers="{access_token: token}">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传.jpg,.png文件</div>
          </el-upload>
        </div>
      </div>
    </el-form-item>

    <el-form-item label-width="100px" label="Logo签名">
      <el-input v-model="systemInfo.sysGraph" maxlength="20" show-word-limit />
    </el-form-item>

    <el-form-item label-width="100px" label="微信">
      <div class="item-all">
        <div class="item-left">
          <el-image style="width: 150px; height: 150px" v-if="systemInfo.sysWeixin" :src="baseUrl + systemInfo.sysWeixin">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </div>
        <div class="item-right">
          <el-upload
            class="upload-demo"
            :action="baseUrl + '/base/file/upload'"
            :on-success="successWeixin"
            :show-file-list="false"
            :before-upload="fileCheck"
            :headers="{access_token: token}">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传.jpg,.png文件</div>
          </el-upload>
        </div>
      </div>
    </el-form-item>

    <el-form-item label-width="100px" label="微信公众号">
      <div class="item-all">
        <div class="item-left">
          <el-image style="width: 150px; height: 150px" v-if="systemInfo.sysWenxinPublic" :src="baseUrl + systemInfo.sysWenxinPublic">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </div>
        <div class="item-right">
          <el-upload
            class="upload-demo"
            :action="baseUrl + '/base/file/upload'"
            :on-success="successWeixinPublic"
            :show-file-list="false"
            :before-upload="fileCheck"
            :headers="{access_token: token}">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
            <div slot="tip" class="el-upload__tip">只能上传.jpg,.png文件</div>
          </el-upload>
        </div>
      </div>
    </el-form-item>
    <el-form-item label-width="1px">
      <v-md-editor
        v-model="systemInfo.sysContent"
        height="500px"
        :disabled-menus="[]"
        @upload-image="handleUploadImage"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="$router.back()">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getSystemInfoInfo, updateSystemInfo } from '@/api/system'
import { URL_PREFIX } from '@/api/config'
import { fileUpload } from '@/api/base'
import { getToken } from '@/utils/auth'
import { saveOrUpdateNotice } from '@/api/notice'
export default {
  name: 'SystemInfo',
  data() {
    return {
      systemInfo: {},
      baseUrl: URL_PREFIX,
      fileList: []
    }
  },
  computed: {
    token() {
      return getToken()
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      getSystemInfoInfo().then(res => {
        this.systemInfo = res.data
      })
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
    successLogo(response, file, fileList) {
      this.systemInfo.sysLogo = response.data
      this.$forceUpdate()
    },
    successWeixin(response, file, fileList) {
      this.systemInfo.sysWeixin = response.data
      this.$forceUpdate()
    },
    successWeixinPublic(response, file, fileList) {
      this.systemInfo.sysWenxinPublic = response.data
      this.$forceUpdate()
    },
    fileCheck(file) {
      const { name } = file
      if (name) {
        const postFix = name.substring(name.lastIndexOf('.'))
        const arr = ['.jpg', '.png']
        if (arr.includes(postFix)) {
          return true
        } else {
          this.$message.error('文件类型不允许')
          return false
        }
      } else {
        this.$message.error('请选择文件')
        return false
      }
    },
    submitForm(formName) {
      updateSystemInfo(this.systemInfo).then(res => {
        this.$message.success('更新成功')
        this.init()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.content-all {
  margin-top: 10px;
  .item-all{
    width: 50%;
    height: 200px;
    display: flex;
    justify-content: space-between;
  }
}
</style>
