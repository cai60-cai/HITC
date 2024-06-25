<template>
  <el-form>
    <el-form-item label="头像">
      <pan-thumb v-if="user.avatar" :image="baseUrl + user.avatar" />
      <pan-thumb v-else :image="user.avatar" />

      <el-button type="primary" icon="el-icon-upload" style="position: absolute;bottom: 15px;margin-left: 40px;" @click="imagecropperShow=true">
        选择头像
      </el-button>

      <image-cropper
        v-show="imagecropperShow"
        :key="imagecropperKey"
        :width="300"
        :height="300"
        field="file"
        url="/server/base/file/upload"
        lang-type="zh"
        @close="close"
        @crop-upload-success="cropSuccess"
      />
    </el-form-item>
    <el-form-item label="用户名">
      <el-input v-model.trim="user.userName" />
    </el-form-item>
    <el-form-item label="邮箱地址">
      <el-input v-model.trim="user.email" />
    </el-form-item>
    <el-form-item label="手机号">
      <el-input v-model.trim="user.phone" />
    </el-form-item>
    <el-form-item label="签名">
      <el-input
        v-model.trim="user.autograph"
        type="textarea"
        autosize
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">修改</el-button>
      <span style="font-size: 13px; padding: 0 10px">上次修改时间: {{ user.updateTime }}</span>
    </el-form-item>
  </el-form>
</template>

<script>
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'
import { getUserInfo, updateUserInfo } from '@/api/user'
import { URL_PREFIX } from '@/api/config'
export default {
  components: { ImageCropper, PanThumb },
  data() {
    return {
      user: {
        userName: 'admin',
        email: 'cxs@163.com',
        phone: '10086',
        autograph: '躺平了',
        avatar: 'http://currency-interface.oss-cn-beijing.aliyuncs.com/c3e436a66c354f7_20221117125000.jpg',
        updateTime: ''
      },
      imagecropperShow: false,
      imagecropperKey: 0,
      file: '',
      baseUrl: URL_PREFIX
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      getUserInfo().then(res => {
        this.user = res.data
      })
    },
    submit() {
      updateUserInfo(this.user).then(() => {
        this.$message({
          message: '用户信息修改成功',
          type: 'success',
          duration: 5 * 1000
        })
      })
    },
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.user.avatar = resData
    },
    close() {
      this.imagecropperShow = false
    }
  }
}
</script>
