<template>
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="75px" class="demo-ruleForm">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input type="password" v-model="ruleForm.oldPassword" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="password">
      <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="确认密码" prop="checkPass">
      <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {checkOldPassword, updatePassword} from "@/api/user";
import AES from "@/utils/AES";

  export default {
    name: "UpdatePwd",
    data() {
      var validateOldPass = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('旧密码不能为空'));
        }
        checkOldPassword({oldPassword: AES.encrypt(value)}).then(res => {
          if (res.data) {
            callback();
          } else {
            return callback(new Error('旧密码不正确'));
          }
        })
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else if (value.length < 6) {
          callback(new Error('密码不能少于6位'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        ruleForm: {
          password: '',
          checkPass: '',
          oldPassword: ''
        },
        rules: {
          password: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          oldPassword: [
            { validator: validateOldPass, trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const {checkPass,oldPassword,password} = this.ruleForm;
            const updatePasswordObj = {
              checkPass: AES.encrypt(checkPass),
              oldPassword: AES.encrypt(oldPassword),
              password: AES.encrypt(password)
            }
            updatePassword(updatePasswordObj).then(res => {
              this.$message.success('密码修改成功,请重新登录')
              this.$store.dispatch('auth/logout')
              this.$nextTick(() => {
                this.$router.replace("/")
              })
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

</style>
