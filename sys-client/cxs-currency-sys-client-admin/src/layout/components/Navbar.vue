<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />

        <error-log class="errLog-container right-menu-item hover-effect" />

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="Global Size" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="baseUrl + avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/profile/index">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <a @click="updatePwd()">
            <el-dropdown-item>修改密码</el-dropdown-item>
          </a>
          <router-link to="/">
            <el-dropdown-item>首页</el-dropdown-item>
          </router-link>
          <a target="_blank" href="https://gitee.com/cxs_git_com">
            <el-dropdown-item>gitee地址</el-dropdown-item>
          </a>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出系统</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog :visible.sync="dialogVisible" title="修改密码">
      <el-form ref="dataForm" :rules="rules" :model="pwdInfo" label-width="80px" label-position="left">
        <el-form-item label="旧密码" prop="oldPwd">
          <el-input v-model="pwdInfo.oldPwd" placeholder="旧密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
          <el-input v-model="pwdInfo.newPwd" placeholder="新密码" show-password />
        </el-form-item>
        <el-form-item label="旧密码" prop="new2Pwd">
          <el-input v-model="pwdInfo.new2Pwd" placeholder="确认密码" show-password />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmRole">确认修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import { checkUserPwd, updateUserPwd } from '@/api/user'
import { URL_PREFIX } from '@/api/config'
import AES from '@/utils/AES'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    SizeSelect,
    Search
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ])
  },
  data() {
    var validateOldPwd = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('旧密码不能为空'))
      }
      checkUserPwd(AES.encrypt(value)).then(res => {
        if (res.data) {
          callback()
        } else {
          callback(new Error('旧密码不正确'))
        }
      })
    }
    var validateNewPwd = (rule, value, callback) => {
      if (value === '' || value === undefined) {
        callback(new Error('请输入密码'))
      } else if (value.length < 6) {
        callback(new Error('密码长度不能小于6位'))
      } else {
        if (this.pwdInfo.newPwd !== '') {
          this.$refs.dataForm.validateField('new2Pwd')
        }
        callback()
      }
    }
    var validateNewPwd2 = (rule, value, callback) => {
      if (value === '' || value === undefined) {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.pwdInfo.newPwd) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      baseUrl: URL_PREFIX,
      pwdInfo: {
        oldPwd: '',
        newPwd: '',
        new2Pwd: ''
      },
      rules: {
        oldPwd: [{ validator: validateOldPwd, trigger: 'blur' }],
        newPwd: [{ validator: validateNewPwd, trigger: 'blur' }],
        new2Pwd: [{ validator: validateNewPwd2, trigger: 'blur' }]
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    updatePwd() {
      this.dialogVisible = true
      this.pwdInfo = {}
    },
    confirmRole() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const { oldPwd, newPwd, new2Pwd } = this.pwdInfo
          const updatePwdObj = {
            oldPwd: AES.encrypt(oldPwd),
            newPwd: AES.encrypt(newPwd),
            new2Pwd: AES.encrypt(new2Pwd)
          }
          updateUserPwd(updatePwdObj).then(() => {
            this.$message.success('密码修改成功')
            this.dialogVisible = false
            this.$refs['dataForm'].resetFields()
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
