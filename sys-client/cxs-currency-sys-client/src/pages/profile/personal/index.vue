<template>
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="70px" class="demo-ruleForm">
    <el-form-item label="登录名" prop="userName">
      <el-input type="text" placeholder="请输入登录名称，只能输入16位英文、数字、-、_" v-model="ruleForm.userName" autocomplete="off" maxlength="16" show-word-limit></el-input>
    </el-form-item>
    <el-form-item label="显示名" prop="nickName">
      <el-input type="text" v-model="ruleForm.nickName" placeholder="请输入显示名称" autocomplete="off" maxlength="10" show-word-limit></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-radio-group v-model="ruleForm.sex">
        <el-radio :label="1">男</el-radio>
        <el-radio :label="2">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="手机" prop="phone">
      <el-input type="text" v-model="ruleForm.phone" placeholder="请输入手机号" autocomplete="off" maxlength="11" show-word-limit></el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input type="text" v-model="ruleForm.email" placeholder="请输入邮箱地址"></el-input>
    </el-form-item>
    <el-form-item label="签名" prop="autograph">
      <el-input type="textarea" :rows="3" resize="none" v-model="ruleForm.autograph" maxlength="50" show-word-limit placeholder="请输入个性签名"></el-input>
    </el-form-item>
    <el-form-item label="头像" prop="avatar">
      <el-upload
          class="avatar-uploader"
          :action="baseUrl + '/base/file/upload'"
          :headers="header"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
        <img v-if="ruleForm.avatar" :src="baseUrl + ruleForm.avatar" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </el-form-item>
    <el-form-item>
      <el-button style="width: 100px;margin-right: 20px" type="primary" @click="submitForm('ruleForm')">提交</el-button>
      {{ userInfo.nickName }}注册于:{{ userInfo.createTime }}
    </el-form-item>
  </el-form>
</template>

<script>
import {getPersonal, updateSelfInfo} from "@/api/user";
  import {URL_PREFIX} from "@/api/config";
  import { getToken } from '@/utils/auth'
  import { checkUserNameExist } from '@/api/user'
import {mapGetters} from "vuex";

  export default {
    name: "Personal",
    data(){
      var checkUserName = (rule, value, callback) => {
        const pattern = /^[A-Za-z0-9-_]+$/;
        if (!value) {
          return callback(new Error('登录名不能为空'));
        }
        if (pattern.test(value) && value.length <= 16) {
          checkUserNameExist({userName: value}).then(res => {
            if (res.data) {
              callback(new Error('登录名已存在'));
            } else {
              callback();
            }
          })
        } else {
          callback(new Error('登录名格式不正确,须由数字、英文字母、-、下划线(不包含、)组成,不大于16位'));
        }
      };
      var checkNickName = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('展示名不能为空'));
        }
        if (value.length <= 10) {
          callback();
        } else {
          callback(new Error('展示名格式不正确,长度不大于10位'));
        }
      };
      var validatePhone = (rule, value, callback) => {
        const pattern = /0?(13|14|15|17|18|19)[0-9]{9}/;
        if (value && value !== '') {
          if (pattern.test(value)) {
            callback();
          } else {
            callback(new Error('手机号格式不正确'));
          }
        } else {
          callback();
        }
      };
      var validateEmail = (rule, value, callback) => {
        const pattern = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
        if (value && value !== '') {
          if (pattern.test(value)) {
            callback();
          } else {
            callback(new Error('邮箱格式不正确'));
          }
        } else {
          callback();
        }
      };

      return {
        baseUrl: URL_PREFIX,
        userInfo:{},
        ruleForm: {
          userName: '',
          nickName: '',
          avatar: '',
          sex: null,
          createTime: '',
          phone:'',
          email:'',
          autograph: ''
        },
        rules: {
          userName: [
            { validator: checkUserName, trigger: 'blur' }
          ],
          nickName: [
            { validator: checkNickName, trigger: 'blur' }
          ],
          phone: [
            { validator: validatePhone, trigger: 'blur' }
          ],
          email: [
            { validator: validateEmail, trigger: 'blur' }
          ]
        }
      }
    },
    computed:{
      header(){
        return {
          'access_token': getToken()
        }
      },
      ...mapGetters(['user'])
    },
    mounted() {
      this.init()
    },
    methods:{
      init(){
        getPersonal().then(res => {
          this.userInfo = res.data
          this.ruleForm = res.data
        })
      },
      handleAvatarSuccess(res, file) {
        this.ruleForm.avatar = res.data;
        this.$forceUpdate()
      },
      // 上传之前
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isPNG = file.type === 'image/png';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG && !isPNG) {
          this.$message.error('上传头像图片只能是 jpg或png 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return (isJPG && isLt2M) || (isPNG && isLt2M) ;
      },
      submitForm(formName) {
        const { userName } = this.user
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const { sex } = this.ruleForm
            if (sex === 0) {
              this.ruleForm.sex = null
            }
            updateSelfInfo(this.ruleForm).then(res => {
              if (userName !== this.ruleForm.userName) {
                this.$message.warning('修改成功,当前登录名已作废, 下次请按新的登录名登录')
              } else {
                this.$message.success('修改成功')
              }
              this.$store.dispatch('auth/getInfo')
              this.init()
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
